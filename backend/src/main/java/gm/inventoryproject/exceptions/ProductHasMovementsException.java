package gm.inventoryproject.exceptions;

/**
 * Excepción lanzada cuando se intenta eliminar un producto que
 * tiene movimientos de inventario asociados.
 * <p>
 * Garantiza la integridad del historial de movimientos impidiendo
 * la eliminación de productos con transacciones registradas.
 * </p>
 */
public class ProductHasMovementsException extends RuntimeException {
    /**
     * Constructor de la excepción.
     * @param message Mensaje descriptivo del error.
     */
    public ProductHasMovementsException(String message) {
        super(message);
    }
}
