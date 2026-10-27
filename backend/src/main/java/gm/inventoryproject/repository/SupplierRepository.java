package gm.inventoryproject.repository;

import gm.inventoryproject.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Supplier.
 * <p>
 * Proporciona métodos de consulta personalizados para búsquedas
 * por nombre y validaciones de unicidad en email y teléfono.
 * </p>
 */
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    /**
     * Verifica si existe un proveedor con el email especificado.
     * @param email Email a verificar.
     * @return true si existe un proveedor con ese email, false en caso contrario.
     */
    boolean existsByEmail(String email);
    /**
     * Verifica si existe un proveedor con el teléfono especificado.
     * @param phone Teléfono a verificar.
     * @return true si existe un proveedor con ese teléfono, false en caso contrario.
     */
    boolean existsByPhone(String phone);
    //Optional<Supplier> findByNameIgnoreCase(String name);

    // Parcial (LIKE %name%)
    /**
     * Busca proveedores cuyo nombre contenga el texto especificado (case-insensitive).
     * @param name Texto a buscar en el nombre.
     * @return Lista de proveedores que coinciden con la búsqueda.
     */
    List<Supplier> findByNameContainingIgnoreCase(String name);

    // Para validar si existe por nombre
    /**
     * Verifica si existe un proveedor con el nombre especificado.
     * @param name Nombre a verificar.
     * @return true si existe un proveedor con ese nombre, false en caso contrario.
     */
    boolean existsByName(String name);

    boolean existsById(Long id);




}

