package gm.inventoryproject.dto.inventorymovement;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryMovementRequestDto {

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @Schema(description = "Cantidad movida", example = "10")
    private Integer quantity;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    @Pattern(
            regexp = "IN|OUT",
            message = "El tipo debe ser IN o OUT"
    )
    @Schema(description = "Tipo de movimiento", example = "IN")
    private String type;

    @NotNull(message = "El ID del producto es obligatorio")
    @Schema(description = "ID del producto asociado", example = "3")
    private Long productId;
}

