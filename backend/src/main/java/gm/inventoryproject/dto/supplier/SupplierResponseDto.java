package gm.inventoryproject.dto.supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SupplierResponseDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
}

