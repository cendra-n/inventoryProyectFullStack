package gm.inventoryproject.dto.supplier;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierRequestDto {

    @NotBlank(message = "El nombre del proveedor no puede estar vacío")
    @Size(min = 2, max = 30, message = "El nombre debe tener entre 2 y 30 caracteres")
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚÑáéíóúñ ]+$",
            message = "El nombre solo puede contener letras y espacios"
    )
    @Schema(description = "Nombre del proveedor", example = "Joaquin Gonzales")
    private String name;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Formato de email inválido"
    )
    @Schema(description = "Email del proveedor", example = "Joaquin@simeflat.com")
    private String email;


    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(
            regexp = "^[0-9]{8,15}$",
            message = "El teléfono debe contener solo números (8 a 15 dígitos)"
    )
    @Schema(description = "Teléfono del proveedor", example = "1144151093 / 01147335520/ 02320684512")
    private String phone;
}
