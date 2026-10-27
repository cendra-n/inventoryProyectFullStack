package gm.inventoryproject.exceptions;

/**
 * Excepción lanzada cuando no se encuentra un recurso solicitado.
 * <p>
 * Se utiliza para indicar que una entidad no existe en el sistema
 * cuando se intenta acceder a ella por su identificador.
 * </p>
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Constructor de la excepción.
     * @param message Mensaje descriptivo del error.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

