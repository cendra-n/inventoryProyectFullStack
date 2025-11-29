package gm.inventoryproject.service;

import gm.inventoryproject.model.InventoryMovement;
import gm.inventoryproject.model.Product;
import gm.inventoryproject.repository.InventoryMovementRepository;
import gm.inventoryproject.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryMovementService implements IInventoryMovementService {

    private final InventoryMovementRepository movementRepository;
    private final ProductRepository productRepository;

    public InventoryMovementService(InventoryMovementRepository movementRepository,
                                    ProductRepository productRepository) {
        this.movementRepository = movementRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryMovement> getAll() {
        return movementRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryMovement getById(Long id) {
        return movementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movement not found with id: " + id));
    }

    @Override
    public InventoryMovement create(InventoryMovement movement) {

        Product product = productRepository.findById(movement.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + movement.getProduct().getId()));

        movement.setProduct(product);

        return movementRepository.save(movement);
    }

    @Override
    public void delete(Long id) {
        if (!movementRepository.existsById(id)) {
            throw new RuntimeException("Movement not found with id: " + id);
        }
        movementRepository.deleteById(id);
    }

    // 🔍 Búsqueda por producto
    @Override
    @Transactional(readOnly = true)
    public List<InventoryMovement> getByProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        return movementRepository.findByProduct(product);
    }

    // 🔍 Búsqueda por rango de fecha
    @Override
    @Transactional(readOnly = true)
    public List<InventoryMovement> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return movementRepository.findByDateBetween(start, end);
    }

    // 🔍 Búsqueda combinada producto + fecha
    @Override
    @Transactional(readOnly = true)
    public List<InventoryMovement> getByProductAndDate(Long productId, LocalDateTime start, LocalDateTime end) {
        return movementRepository.findByProductIdAndDateBetween(productId, start, end);
    }
}

