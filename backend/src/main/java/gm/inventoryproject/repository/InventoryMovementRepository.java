package gm.inventoryproject.repository;

import gm.inventoryproject.model.InventoryMovement;
import gm.inventoryproject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {

    // Buscar todos los movimientos de un producto
    List<InventoryMovement> findByProduct(Product product);

    // Buscar todos los movimientos entre fechas
    List<InventoryMovement> findByDateBetween(LocalDateTime start, LocalDateTime end);

    // Buscar movimientos por producto y fecha (más útil)
    @Query("SELECT m FROM InventoryMovement m WHERE m.product.id = :productId AND m.date BETWEEN :start AND :end")
    List<InventoryMovement> findByProductIdAndDateBetween(
            @Param("productId") Long productId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}


