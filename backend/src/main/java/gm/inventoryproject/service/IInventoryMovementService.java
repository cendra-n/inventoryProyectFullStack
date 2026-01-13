package gm.inventoryproject.service;

import gm.inventoryproject.model.InventoryMovement;
import gm.inventoryproject.dto.inventorymovement.InventoryMovementRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface IInventoryMovementService {

    List<InventoryMovement> getAll();

    InventoryMovement getById(Long id);

    // NUEVO: Crear desde DTO (firma pública)
    InventoryMovement createFromDto(gm.inventoryproject.dto.inventorymovement.InventoryMovementRequestDto dto);


    // 🔍 Búsquedas
    List<InventoryMovement> getByProduct(Long productId);

    List<InventoryMovement> getByDateRange(LocalDateTime start, LocalDateTime end);

    List<InventoryMovement> getByProductAndDate(Long productId, LocalDateTime start, LocalDateTime end);


}


