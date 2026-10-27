package gm.inventoryproject.service;

import gm.inventoryproject.model.Category;

import java.util.List;

/**
 * Interfaz que define el contrato de operaciones para la gestión de categorías.
 * <p>
 * Proporciona métodos para realizar operaciones CRUD y búsquedas
 * sobre categorías de productos.
 * </p>
 */
public interface ICategoryService {

    /**
     * Obtiene todas las categorías registradas en el sistema.
     * @return Lista de todas las categorías.
     */
    List<Category> getAll();

    /**
     * Busca una categoría por su identificador.
     * @param id Identificador de la categoría.
     * @return La categoría encontrada.
     * @throws ResourceNotFoundException si no existe una categoría con el ID proporcionado.
     */
    Category getById(Long id);

    /**
     * Busca categorías cuyo nombre contenga el texto especificado (búsqueda parcial, case-insensitive).
     * @param name Texto a buscar en el nombre de las categorías.
     * @return Lista de categorías que coinciden con la búsqueda.
     */
    List<Category> searchByName(String name);

    //Category findByName(String name);

    /**
     * Crea una nueva categoría en el sistema.
     * @param category Datos de la categoría a crear.
     * @return La categoría creada con su ID generado.
     * @throws DuplicateFieldException si ya existe una categoría con el mismo nombre.
     */
    Category create(Category category);

    /**
     * Actualiza una categoría existente.
     * @param id Identificador de la categoría a actualizar.
     * @param category Datos actualizados de la categoría.
     * @return La categoría actualizada.
     * @throws ResourceNotFoundException si no existe una categoría con el ID proporcionado.
     * @throws DuplicateFieldException si el nuevo nombre ya está en uso por otra categoría.
     */
    Category update(Long id, Category category);

    /**
     * Elimina una categoría del sistema.
     * @param id Identificador de la categoría a eliminar.
     * @throws ResourceNotFoundException si no existe una categoría con el ID proporcionado.
     * @throws CategoryHasProductsException si la categoría tiene productos asociados.
     */
    void delete(Long id);
}

