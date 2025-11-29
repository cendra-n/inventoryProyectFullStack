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
    @Schema(description = "Nombre de la categoría", example = "Electrónica")
    private String name;

    @Size(max = 200, message = "La descripción no puede superar los 255 caracteres")
    @Schema(description = "Descripción de la categoría", example = "Productos relacionados con tecnología")
    private String description;
}

