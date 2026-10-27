package gm.inventoryproject.service;

import gm.inventoryproject.model.InventoryMovement;
import gm.inventoryproject.dto.inventorymovement.InventoryMovementRequestDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz que define el contrato de operaciones para la gestión de movimientos de inventario.
 * <p>
 * Proporciona métodos para registrar movimientos de entrada y salida de stock,
 * así como consultas por producto, rango de fechas o combinaciones de ambos.
 * </p>
 */
public interface IInventoryMovementService {

    /**
     * Obtiene todos los movimientos de inventario registrados.
     * @return Lista de todos los movimientos.
     */
    List<InventoryMovement> getAll();

    /**
     * Busca un movimiento de inventario por su identificador.
     * @param id Identificador del movimiento.
     * @return El movimiento encontrado.
     * @throws ResourceNotFoundException si no existe un movimiento con el ID proporcionado.
     */
    InventoryMovement getById(Long id);

    // NUEVO: Crear desde DTO (firma pública)
    /**
     * Registra un nuevo movimiento de inventario desde un DTO.
     * <p>
     * Actualiza automáticamente el stock del producto según el tipo de movimiento:
     * - IN: suma la cantidad al stock actual.
     * - OUT: resta la cantidad del stock actual (valida que haya stock suficiente).
     * </p>
     * @param dto Datos del movimiento a registrar.
     * @return El movimiento registrado con fecha actual.
     * @throws ResourceNotFoundException si el producto especificado no existe.
     * @throws IllegalArgumentException si la cantidad es menor o igual a 0.
     * @throws IllegalArgumentException si el tipo de movimiento no es válido (debe ser IN o OUT).
     * @throws IllegalArgumentException si se intenta una salida con stock insuficiente o cero.
     */
    InventoryMovement createFromDto(gm.inventoryproject.dto.inventorymovement.InventoryMovementRequestDto dto);


    // 🔍 Búsquedas
    /**
     * Busca todos los movimientos de inventario de un producto específico.
     * @param productId Identificador del producto.
     * @return Lista de movimientos del producto.
     * @throws ResourceNotFoundException si no existe un producto con el ID proporcionado.
     */
    List<InventoryMovement> getByProduct(Long productId);

    /**
     * Busca movimientos de inventario dentro de un rango de fechas.
     * @param start Fecha y hora de inicio del rango.
     * @param end Fecha y hora de fin del rango.
     * @return Lista de movimientos en el rango especificado.
     */
    List<InventoryMovement> getByDateRange(LocalDateTime start, LocalDateTime end);

    /**
     * Busca movimientos de un producto dentro de un rango de fechas.
     * @param productId Identificador del producto.
     * @param start Fecha y hora de inicio del rango.
     * @param end Fecha y hora de fin del rango.
     * @return Lista de movimientos del producto en el rango especificado.
     */
    List<InventoryMovement> getByProductAndDate(Long productId, LocalDateTime start, LocalDateTime end);


}


