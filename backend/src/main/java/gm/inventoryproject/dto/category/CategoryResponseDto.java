package gm.inventoryproject.dto.category;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryResponseDto {

    @Schema(description = "ID de la categoría")
    private Long id;

    @Schema(description = "Nombre de la categoría")
    private String name;

    @Schema(description = "Descripción de la categoría")
    private String description;
}

