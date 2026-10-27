package gm.inventoryproject.model;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Representa un movimiento de inventario de un producto.
 * <p>
 * Registra entradas (IN) o salidas (OUT) de stock, actualizando
 * automáticamente el inventario del producto asociado.
 * </p>
 */
@Entity
@Table(name = "inventory_movements")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@ToString(onlyExplicitlyIncluded = true)
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    @ToString.Include
    private Integer quantity; // cantidad

    @Enumerated(EnumType.STRING)
    @ToString.Include
    private MovementType type; // tipo

    private LocalDateTime date = LocalDateTime.now(); // fecha actual

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product; // producto

    /**
     * Tipo de movimiento de inventario.
     * <p>
     * IN: entrada de stock (incrementa inventario).
     * OUT: salida de stock (decrementa inventario).
     * </p>
     */
    public enum MovementType {
        IN, OUT // ENTRADA / SALIDA
    }
}

