package gm.inventoryproject.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestDto {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 40, message = "El nombre debe tener entre 2 y 40 caracteres")
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚÑáéíóúñ ]+$",
            message = "El nombre solo puede contener letras y espacios"
    )
    @Schema(description = "Nombre de la categoría", example = "No perecederos")
    private String name;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    @Pattern(
            regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s.,;\\-]+$",
            message = "La descripción solo puede contener letras, números, espacios y signos de puntuación básicos (.,;-)"
    )
    @Schema(description = "Descripción del producto", example = "Productos comestibles con " +
            "larga vida útil que no requieren refrigeración para su conservación")
    private String description;


}

