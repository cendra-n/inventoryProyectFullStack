package gm.inventoryproject.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponseDto {

    @Schema(description = "ID del producto")
    private Long id;

    @Schema(description = "Nombre del producto")
    private String name;

    @Schema(description = "Descripción del producto")
    private String description;

    @Schema(description = "Precio del producto")
    private Double price;

    @Schema(description = "Stock disponible")
    private Integer stock;

    @Schema(description = "ID de la categoría del producto")
    private Long categoryId;

    @Schema(description = "ID del proveedor del producto")
    private Long supplierId;

    @Schema(description = "Nombre de la categoría")
    private String categoryName;

    @Schema(description = "Nombre del proveedor")
    private String supplierName;
}

