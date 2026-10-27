package gm.inventoryproject.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa una categoría de productos en el sistema de inventario.
 * <p>
 * Cada categoría agrupa productos similares. El nombre debe ser único
 * en el sistema.
 * </p>
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    @Column(nullable = false, unique = true)
    @ToString.Include
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;   // ✔️ ESTE CAMPO DEBE EXISTIR
}

