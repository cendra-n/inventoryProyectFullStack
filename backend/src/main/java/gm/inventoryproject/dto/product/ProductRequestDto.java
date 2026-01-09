package gm.inventoryproject.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 40, message = "El nombre debe tener entre 2 y 40 caracteres")
    @Pattern(
            regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]+$",
            message = "El nombre solo puede contener letras, números y espacios"
    )
    @Schema(description = "Nombre del producto", example = "Mouse inalámbrico")
    private String name;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 240, message = "La descripción no puede superar los 240 caracteres")
    @Pattern(
            regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]+$",
            message = "La descripción solo puede contener letras, números y espacios"
    )
    @Schema(description = "Descripción del producto", example = "Mouse óptico inalámbrico de 1600 DPI")
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que 0")
    @Schema(description = "Precio del producto", example = "2999.99")
    private Double price;

    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    @Schema(description = "Stock disponible", example = "15")
    private Integer stock;

    @NotNull(message = "La categoría es obligatoria")
    @Schema(description = "ID de la categoría", example = "1")
    private Long categoryId;

    @NotNull(message = "El proveedor es obligatorio")
    @Schema(description = "ID del proveedor", example = "4")
    private Long supplierId;
}

