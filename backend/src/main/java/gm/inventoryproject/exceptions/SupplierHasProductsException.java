package gm.inventoryproject.exceptions;

/**
 * Excepción lanzada cuando se intenta eliminar un proveedor que
 * tiene productos asociados.
 * <p>
 * Garantiza la integridad referencial impidiendo la eliminación
 * de proveedores en uso.
 * </p>
 */
public class SupplierHasProductsException extends RuntimeException {
    /**
     * Constructor de la excepción.
     * @param message Mensaje descriptivo del error.
     */
    public SupplierHasProductsException(String message) {
        super(message);
    }
}

