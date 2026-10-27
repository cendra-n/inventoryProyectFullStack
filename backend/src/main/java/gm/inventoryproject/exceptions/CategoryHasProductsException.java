package gm.inventoryproject.exceptions;

/**
 * Excepción lanzada cuando se intenta eliminar una categoría que
 * tiene productos asociados.
 * <p>
 * Garantiza la integridad referencial impidiendo la eliminación
 * de categorías en uso.
 * </p>
 */
public class CategoryHasProductsException extends RuntimeException {

  /**
   * Constructor de la excepción.
   * @param message Mensaje descriptivo del error.
   */
  public CategoryHasProductsException(String message) {
    super(message);
  }
}
