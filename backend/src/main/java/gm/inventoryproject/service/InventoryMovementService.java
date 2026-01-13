package gm.inventoryproject.service;

import gm.inventoryproject.dto.inventorymovement.InventoryMovementRequestDto;
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
    public InventoryMovement createFromDto(InventoryMovementRequestDto dto) {

        // 1) Validar cantidad
        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        // 2) Obtener producto
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + dto.getProductId()
                ));

        // 3) Validar tipo de movimiento
        InventoryMovement.MovementType type;
        try {
            type = InventoryMovement.MovementType.valueOf(dto.getType());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Tipo de movimiento inválido. Debe ser IN o OUT");
        }

        // 4) Lógica de stock
        if (type == InventoryMovement.MovementType.IN) {
            // entrada → sumar
            product.setStock(product.getStock() + dto.getQuantity());
        }

        if (type == InventoryMovement.MovementType.OUT) {

            // No permitir OUT con stock 0
            if (product.getStock() == 0) {
                throw new IllegalArgumentException(
                        "El producto no tiene stock disponible para realizar una salida"
                );
            }

            // No permitir restar más de lo que hay
            if (product.getStock() < dto.getQuantity()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente. Disponible: " + product.getStock()
                );
            }

            // salida → restar
            product.setStock(product.getStock() - dto.getQuantity());
        }

        // Guardar nuevo stock
        productRepository.save(product);

        // 5) Registrar movimiento
        InventoryMovement movement = InventoryMovement.builder()
                .quantity(dto.getQuantity())
                .type(type)
                .product(product)
                .date(LocalDateTime.now())
                .build();

        return movementRepository.save(movement);
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


}
