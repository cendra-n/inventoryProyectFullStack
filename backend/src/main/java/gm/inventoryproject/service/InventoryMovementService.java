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

    @Override
    @Transactional(readOnly = true)
    public List<InventoryMovement> getAll() {
        return movementRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryMovement getById(Long id) {
        return movementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movement not found with id: " + id));
    }

    @Override
    @Transactional
    public InventoryMovement create(InventoryMovement movement) {
        // Si usas createFromDto en todos los lugares, este método puede seguir existiendo para usos internos
        if (movement.getProduct() == null || movement.getProduct().getId() == null) {
            throw new IllegalArgumentException("Product must be provided");
        }
        // Asegurar que el producto exista y se adjunte la entidad gestionada
        Product product = productRepository.findById(movement.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + movement.getProduct().getId()));
        movement.setProduct(product);

        if (movement.getDate() == null) {
            movement.setDate(LocalDateTime.now());
        }

        return movementRepository.save(movement);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!movementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movement not found with id: " + id);
        }
        movementRepository.deleteById(id);
    }

    // 🔍 Búsqueda por producto
    @Override
    @Transactional(readOnly = true)
    public List<InventoryMovement> getByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
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

    // ================================
    // NUEVO: crear desde DTO (seguro)
    // ================================
    @Override
    @Transactional
    public InventoryMovement createFromDto(gm.inventoryproject.dto.inventorymovement.InventoryMovementRequestDto dto) {

        // 1) Validar y obtener producto
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + dto.getProductId()));

        // 2) Convertir tipo (String -> Enum) de forma robusta
        InventoryMovement.MovementType type;
        try {
            type = InventoryMovement.MovementType.valueOf(dto.getType());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Tipo de movimiento inválido. Debe ser IN o OUT");
        }

        // 3) Construir entidad
        InventoryMovement movement = InventoryMovement.builder()
                .quantity(dto.getQuantity())
                .type(type)
                .product(product)
                .date(LocalDateTime.now())
                .build();

        // 4) Guardar
        return movementRepository.save(movement);
    }
}
