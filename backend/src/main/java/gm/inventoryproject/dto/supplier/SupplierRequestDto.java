package gm.inventoryproject.dto.supplier;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Email(message = "El email debe ser válido")
    private String email;

    @Pattern(
            regexp = "\\d{7,15}",
            message = "El teléfono debe tener solo números (7 a 15 dígitos)"
    )
    private String phone;
}
