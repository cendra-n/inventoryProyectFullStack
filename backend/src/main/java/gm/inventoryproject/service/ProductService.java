package gm.inventoryproject.service;

import gm.inventoryproject.dto.product.ProductRequestDto;
import gm.inventoryproject.exceptions.DuplicateFieldException;
import gm.inventoryproject.exceptions.ResourceNotFoundException;
import gm.inventoryproject.model.Category;
import gm.inventoryproject.model.Product;
import gm.inventoryproject.model.Supplier;
import gm.inventoryproject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final ICategoryService categoryService;
    private final ISupplierService supplierService;

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No se encontró un producto con ID: " + id));
    }

    // ---------------------------------------------------------
    // GET BY NAME
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public Product findByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No se encontró un producto con nombre: " + name));
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------
    @Override
    @Transactional
    public Product create(Product entity) {

        // Validar duplicado por nombre
        if (productRepository.existsByName(entity.getName())) {
            throw new DuplicateFieldException("Ya existe un producto con el nombre: " + entity.getName());
        }

        return productRepository.save(entity);
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------
    @Override
    @Transactional
    public Product update(Long id, ProductRequestDto dto) {

        Product existing = getById(id);

        // Validar duplicado de nombre si cambió
        if (!existing.getName().equalsIgnoreCase(dto.getName())
                && productRepository.existsByName(dto.getName())) {
            throw new DuplicateFieldException("Ya existe un producto con el nombre: " + dto.getName());
        }

        // Setear campos del DTO
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setStock(dto.getStock());

        // Reasignar relaciones
        Category category = categoryService.getById(dto.getCategoryId());
        Supplier supplier = supplierService.getById(dto.getSupplierId());

        existing.setCategory(category);
        existing.setSupplier(supplier);

        return productRepository.save(existing);
    }

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------
    @Override
    @Transactional
    public void delete(Long id) {
        Product p = getById(id);
        productRepository.delete(p);
    }
}
