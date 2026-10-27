package gm.inventoryproject.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa un proveedor de productos en el sistema.
 * <p>
 * Los proveedores suministran productos al inventario.
 * El email y teléfono deben ser únicos en el sistema.
 * </p>
 */
@Entity
@Table(name = "suppliers")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@ToString(onlyExplicitlyIncluded = true)
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    @Column(nullable = false)
    @ToString.Include
    private String name; // nombre

    @Column(unique = true)
    private String phone; // telefono

    @Column(unique = true)
    private String email; // email
}

