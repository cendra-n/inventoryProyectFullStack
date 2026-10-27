package gm.inventoryproject.dto.supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object para la respuesta de proveedores.
 * <p>
 * Contiene la información de un proveedor que se envía al cliente
 * después de una consulta, creación o actualización.
 * </p>
 */
@Getter
@Setter
@AllArgsConstructor
public class SupplierResponseDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
}

