package gm.inventoryproject.controller;

import gm.inventoryproject.dto.InventoryMovementDto;
import gm.inventoryproject.model.InventoryMovement;
import gm.inventoryproject.model.Product;
import gm.inventoryproject.service.IInventoryMovementService;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movements")
public class InventoryMovementController {

    private final IInventoryMovementService movementService;

    public InventoryMovementController(IInventoryMovementService movementService) {
        this.movementService = movementService;
    }

    private InventoryMovementDto toDto(InventoryMovement m) {
        return new InventoryMovementDto(
                m.getId(),
                m.getQuantity(),
                m.getType().name(),
                m.getDate().toString(),
                m.getProduct().getId()
        );
    }

    // 🔹 Listar todo
    @GetMapping
    public List<InventoryMovementDto> getAll() {
        return movementService.getAll()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    // 🔹 Obtener por ID
    @GetMapping("/{id}")
    public InventoryMovementDto getById(@PathVariable Long id) {
        return toDto(movementService.getById(id));
    }

    // 🔹 Crear movimiento
    @PostMapping
    public InventoryMovementDto create(@RequestBody InventoryMovementDto dto) {

        InventoryMovement movement = InventoryMovement.builder()
                .quantity(dto.getQuantity())
                .type(InventoryMovement.MovementType.valueOf(dto.getType()))
                .product(Product.builder().id(dto.getProductId()).build())
                .build();

        return toDto(movementService.create(movement));
    }

    // 🔹 Eliminar
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        movementService.delete(id);
    }

    // 🔹 Buscar por producto
    @GetMapping("/product/{productId}")
    public List<InventoryMovementDto> getByProduct(@PathVariable Long productId) {
        return movementService.getByProduct(productId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    // 🔹 Buscar por rango de fecha
    @GetMapping("/date")
    public List<InventoryMovementDto> getByDateRange(
            @RequestParam String start,
            @RequestParam String end
    ) {
        return movementService.getByDateRange(
                LocalDateTime.parse(start),
                LocalDateTime.parse(end)
        ).stream().map(this::toDto).collect(Collectors.toList());
    }

    // 🔹 Buscar por producto + fecha
    @GetMapping("/product/{productId}/date")
    public List<InventoryMovementDto> getByProductAndDate(
            @PathVariable Long productId,
            @RequestParam String start,
            @RequestParam String end
    ) {
        return movementService.getByProductAndDate(
                productId,
                LocalDateTime.parse(start),
                LocalDateTime.parse(end)
        ).stream().map(this::toDto).collect(Collectors.toList());
    }
}

