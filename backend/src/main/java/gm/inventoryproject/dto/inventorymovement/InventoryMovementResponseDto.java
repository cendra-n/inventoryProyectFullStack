package gm.inventoryproject.dto.inventorymovement;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class InventoryMovementResponseDto {

    @Schema(description = "ID del movimiento")
    private Long id;

    @Schema(description = "Cantidad movida")
    private Integer quantity;

    @Schema(description = "Tipo de movimiento (IN / OUT)")
    private String type;

    @Schema(description = "Fecha del movimiento")
    private LocalDateTime date;

    @Schema(description = "ID del producto asociado")
    private Long productId;

    @Schema(description = "Nombre del producto")
    private String productName;
}

