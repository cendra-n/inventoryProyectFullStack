package gm.inventoryproject.service;

import gm.inventoryproject.dto.product.ProductRequestDto;
import gm.inventoryproject.model.Product;

import java.util.List;

/**
 * Interfaz que define el contrato de operaciones para la gestión de productos.
 * <p>
 * Proporciona métodos para realizar operaciones CRUD y búsquedas
 * sobre productos, con restricciones basadas en movimientos de inventario.
 * </p>
 */
public interface IProductService {

    /**
     * Obtiene todos los productos registrados en el sistema.
     * @return Lista de todos los productos.
     */
    List<Product> getAll();

    /**
     * Busca un producto por su identificador.
     * @param id Identificador del producto.
     * @return El producto encontrado.
     * @throws ResourceNotFoundException si no existe un producto con el ID proporcionado.
     */
    Product getById(Long id);

    /**
     * Busca productos cuyo nombre contenga el texto especificado (búsqueda parcial, case-insensitive).
     * @param name Texto a buscar en el nombre de los productos.
     * @return Lista de productos que coinciden con la búsqueda.
     */
    List<Product> searchByName(String name);

    /**
     * Crea un nuevo producto en el sistema.
     * @param entity Datos del producto a crear.
     * @return El producto creado con su ID generado.
     * @throws DuplicateFieldException si ya existe un producto con el mismo nombre.
     */
    Product create(Product entity);

    /**
     * Actualiza un producto existente.
     * <p>
     * Si el producto tiene movimientos de inventario registrados, solo permite
     * modificar nombre, descripción y precio. La categoría y proveedor no pueden cambiarse.
     * </p>
     * @param id Identificador del producto a actualizar.
     * @param dto Datos actualizados del producto.
     * @return El producto actualizado.
     * @throws ResourceNotFoundException si no existe un producto con el ID proporcionado.
     * @throws DuplicateFieldException si el nuevo nombre ya está en uso por otro producto.
     */
    Product update(Long id, ProductRequestDto dto);

    /**
     * Elimina un producto del sistema.
     * @param id Identificador del producto a eliminar.
     * @throws ResourceNotFoundException si no existe un producto con el ID proporcionado.
     * @throws ProductHasMovementsException si el producto tiene movimientos de inventario asociados.
     */
    void delete(Long id);
}


