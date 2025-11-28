package gm.inventoryproject.model;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory-movements")
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

    public enum MovementType {
        IN, OUT // ENTRADA / SALIDA
    }
}

