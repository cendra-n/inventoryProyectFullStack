package gm.inventoryproject.repository;

import gm.inventoryproject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Product.
 * <p>
 * Proporciona métodos de consulta personalizados para búsquedas
 * por nombre y validaciones de existencia por categoría o proveedor.
 * </p>
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Busca productos cuyo nombre contenga el texto especificado (case-insensitive).
     * @param name Texto a buscar en el nombre.
     * @return Lista de productos que coinciden con la búsqueda.
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Verifica si existe un producto con el nombre especificado.
     * @param name Nombre a verificar.
     * @return true si existe un producto con ese nombre, false en caso contrario.
     */
    boolean existsByName(String name);

    /**
     * Verifica si existen productos asociados a una categoría.
     * @param categoryId Identificador de la categoría.
     * @return true si existen productos con esa categoría, false en caso contrario.
     */
    boolean existsByCategoryId(Long categoryId);

    /**
     * Verifica si existen productos asociados a un proveedor.
     * @param supplierId Identificador del proveedor.
     * @return true si existen productos con ese proveedor, false en caso contrario.
     */
    boolean existsBySupplierId(Long supplierId);

}


