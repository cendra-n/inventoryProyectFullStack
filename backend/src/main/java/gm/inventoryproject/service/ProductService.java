package gm.inventoryproject.service;

import gm.inventoryproject.exceptions.DuplicateFieldException;
import gm.inventoryproject.exceptions.ResourceNotFoundException;
import gm.inventoryproject.model.Product;
import gm.inventoryproject.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Product findByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con nombre: " + name));
    }

    @Override
    @Transactional
    public Product create(Product product) {

        if (productRepository.existsByName(product.getName())) {
            throw new DuplicateFieldException("El nombre del producto ya está registrado");
        }

        return productRepository.save(product);
    }


    @Override
    @Transactional
    public Product update(Long id, Product updatedProduct) {
        Product existing = getById(id);

        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setStock(updatedProduct.getStock());
        existing.setCategory(updatedProduct.getCategory());
        existing.setSupplier(updatedProduct.getSupplier());

        return productRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
        productRepository.deleteById(id);
    }
}
