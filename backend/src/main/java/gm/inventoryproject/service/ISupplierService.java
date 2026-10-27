package gm.inventoryproject.service;

import gm.inventoryproject.dto.supplier.SupplierRequestDto;
import gm.inventoryproject.model.Category;
import gm.inventoryproject.model.Supplier;

import java.util.List;

/**
 * Interfaz que define el contrato de operaciones para la gestión de proveedores.
 * <p>
 * Proporciona métodos para realizar operaciones CRUD y búsquedas
 * sobre proveedores, con validaciones de unicidad en email y teléfono.
 * </p>
 */
public interface ISupplierService {

    /**
     * Obtiene todos los proveedores registrados en el sistema.
     * @return Lista de todos los proveedores.
     */
    List<Supplier> getAll();

    /**
     * Busca un proveedor por su identificador.
     * @param id Identificador del proveedor.
     * @return El proveedor encontrado.
     * @throws ResourceNotFoundException si no existe un proveedor con el ID proporcionado.
     */
    Supplier getById(Long id);

    /**
     * Busca proveedores cuyo nombre contenga el texto especificado (búsqueda parcial, case-insensitive).
     * @param name Texto a buscar en el nombre de los proveedores.
     * @return Lista de proveedores que coinciden con la búsqueda.
     */
    List<Supplier> searchByName(String name);

    /**
     * Crea un nuevo proveedor en el sistema desde un DTO.
     * @param dto Datos del proveedor a crear.
     * @return El proveedor creado con su ID generado.
     * @throws DuplicateFieldException si el email o teléfono ya están registrados.
     */
    Supplier createFromDto(SupplierRequestDto dto);

    /**
     * Actualiza un proveedor existente desde un DTO.
     * @param id Identificador del proveedor a actualizar.
     * @param dto Datos actualizados del proveedor.
     * @return El proveedor actualizado.
     * @throws ResourceNotFoundException si no existe un proveedor con el ID proporcionado.
     * @throws DuplicateFieldException si el nuevo email o teléfono ya están en uso por otro proveedor.
     */
    Supplier updateFromDto(Long id, SupplierRequestDto dto);

    /**
     * Elimina un proveedor del sistema.
     * @param id Identificador del proveedor a eliminar.
     * @throws ResourceNotFoundException si no existe un proveedor con el ID proporcionado.
     * @throws SupplierHasProductsException si el proveedor tiene productos asociados.
     */
    void delete(Long id);
}
