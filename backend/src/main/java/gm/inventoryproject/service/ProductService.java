package gm.inventoryproject.service;

import gm.inventoryproject.dto.product.ProductRequestDto;
import gm.inventoryproject.exceptions.DuplicateFieldException;
import gm.inventoryproject.exceptions.ProductHasMovementsException;
import gm.inventoryproject.exceptions.ResourceNotFoundException;
import gm.inventoryproject.model.Category;
import gm.inventoryproject.model.Product;
import gm.inventoryproject.model.Supplier;
import gm.inventoryproject.repository.InventoryMovementRepository;
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
    private final InventoryMovementRepository inventoryMovementRepository;


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
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
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

        // Validar duplicado de nombre
        if (!existing.getName().equalsIgnoreCase(dto.getName())
                && productRepository.existsByName(dto.getName())) {
            throw new DuplicateFieldException("Ya existe un producto con el nombre: " + dto.getName());
        }

        boolean hasMovements = inventoryMovementRepository.existsByProductId(id);

        // Siempre editable
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());

        // Solo si NO tiene movimientos permite cambiar categoría y proveedor
        if (!hasMovements) {
            Category category = categoryService.getById(dto.getCategoryId());
            Supplier supplier = supplierService.getById(dto.getSupplierId());
            existing.setCategory(category);
            existing.setSupplier(supplier);
        }

        return productRepository.save(existing);
    }



    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------
    @Override
    @Transactional
    public void delete(Long id) {
        Product p = getById(id);

        if (inventoryMovementRepository.existsByProductId(id)) {
            throw new ProductHasMovementsException(
                    "No se puede eliminar un producto con movimientos de inventario"
            );
        }

        productRepository.delete(p);
    }

}
