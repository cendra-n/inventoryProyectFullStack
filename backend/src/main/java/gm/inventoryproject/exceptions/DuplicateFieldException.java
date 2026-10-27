package gm.inventoryproject.exceptions;

/**
 * Excepción lanzada cuando se intenta crear o actualizar un recurso
 * con un valor que ya existe en el sistema.
 * <p>
 * Se utiliza para validar unicidad de campos como nombres, emails o teléfonos.
 * </p>
 */
public class DuplicateFieldException extends RuntimeException {
    /**
     * Constructor de la excepción.
     * @param message Mensaje descriptivo del error.
     */
    public DuplicateFieldException(String message) {
        super(message);
    }
}

