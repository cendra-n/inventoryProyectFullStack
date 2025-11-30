package gm.inventoryproject.service;

import gm.inventoryproject.exceptions.ResourceNotFoundException;
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

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<InventoryMovement> getAll() {
        return movementRepository.findAll();
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public InventoryMovement getById(Long id) {
        return movementRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Movimiento no encontrado con id: " + id)
                );
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------
    @Override
    @Transactional
    public InventoryMovement create(InventoryMovement movement) {

        Long productId = movement.getProduct().getId();

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado con id: " + productId)
                );

        movement.setProduct(product);

        return movementRepository.save(movement);
    }

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------
    @Override
    @Transactional
    public void delete(Long id) {

        if (!movementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movimiento no encontrado con id: " + id);
        }

        movementRepository.deleteById(id);
    }

    // ---------------------------------------------------------
    // FILTER BY PRODUCT
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<InventoryMovement> getByProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado con id: " + productId)
                );

        return movementRepository.findByProduct(product);
    }

    // ---------------------------------------------------------
    // FILTER BY DATE RANGE
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<InventoryMovement> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return movementRepository.findByDateBetween(start, end);
    }

    // ---------------------------------------------------------
    // FILTER BY PRODUCT + DATE RANGE
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<InventoryMovement> getByProductAndDate(Long productId, LocalDateTime start, LocalDateTime end) {
        return movementRepository.findByProductIdAndDateBetween(productId, start, end);
    }
}
