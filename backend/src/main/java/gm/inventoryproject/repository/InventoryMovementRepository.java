package gm.inventoryproject.repository;

import gm.inventoryproject.model.InventoryMovement;
import gm.inventoryproject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para la entidad InventoryMovement.
 * <p>
 * Proporciona métodos de consulta personalizados para búsquedas
 * por producto, rango de fechas y combinaciones de ambos.
 * </p>
 */
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {

    /**
     * Verifica si existen movimientos de inventario para un producto.
     * @param productId Identificador del producto.
     * @return true si existen movimientos para ese producto, false en caso contrario.
     */
    boolean existsByProductId(Long productId);

    // Buscar todos los movimientos de un producto
    /**
     * Busca todos los movimientos de inventario de un producto específico.
     * @param product Producto cuyos movimientos se buscan.
     * @return Lista de movimientos del producto.
     */
    List<InventoryMovement> findByProduct(Product product);

    // Buscar todos los movimientos entre fechas
    /**
     * Busca movimientos de inventario dentro de un rango de fechas.
     * @param start Fecha y hora de inicio del rango.
     * @param end Fecha y hora de fin del rango.
     * @return Lista de movimientos en el rango especificado.
     */
    List<InventoryMovement> findByDateBetween(LocalDateTime start, LocalDateTime end);

    // Buscar movimientos por producto y fecha (más útil)
    /**
     * Busca movimientos de un producto dentro de un rango de fechas.
     * @param productId Identificador del producto.
     * @param start Fecha y hora de inicio del rango.
     * @param end Fecha y hora de fin del rango.
     * @return Lista de movimientos del producto en el rango especificado.
     */
    @Query("SELECT m FROM InventoryMovement m WHERE m.product.id = :productId AND m.date BETWEEN :start AND :end")
    List<InventoryMovement> findByProductIdAndDateBetween(
            @Param("productId") Long productId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}


