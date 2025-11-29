package gm.inventoryproject.controller;

import gm.inventoryproject.dto.supplier.SupplierRequestDto;
import gm.inventoryproject.dto.supplier.SupplierResponseDto;
import gm.inventoryproject.model.Supplier;
import gm.inventoryproject.service.ISupplierService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/inventory-app/suppliers")
@Tag(
        name = "Supplier",
        description = "Endpoints para administrar proveedores del sistema"
)
@CrossOrigin(origins = "https://localhost:4200")
public class SupplierController {

    private final ISupplierService supplierService;

    public SupplierController(ISupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------
    @Operation(
            summary = "Obtener todos los proveedores",
            description = "Devuelve un listado con todos los proveedores registrados."
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<SupplierResponseDto>> getAll() {
        List<SupplierResponseDto> response = supplierService.getAll()
                .stream()
                .map(s -> new SupplierResponseDto(
                        s.getId(),
                        s.getName(),
                        s.getEmail(),
                        s.getPhone()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------
    @Operation(
            summary = "Obtener proveedor por ID",
            description = "Devuelve un proveedor en base a su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Proveedor encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un proveedor con ese ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> getById(@PathVariable Long id) {
        Supplier s = supplierService.getById(id);

        SupplierResponseDto dto = new SupplierResponseDto(
                s.getId(),
                s.getName(),
                s.getEmail(),
                s.getPhone()
        );

        return ResponseEntity.ok(dto);
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------
    @Operation(
            summary = "Crear un nuevo proveedor",
            description = "Registra un nuevo proveedor utilizando los datos enviados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Proveedor creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<SupplierResponseDto> create(
            @Valid @RequestBody SupplierRequestDto request
    ) {

        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());

        Supplier saved = supplierService.create(supplier);

        SupplierResponseDto response = new SupplierResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getPhone()
        );

        return ResponseEntity.status(201).body(response);
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------
    @Operation(
            summary = "Actualizar un proveedor",
            description = "Modifica los datos de un proveedor existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Proveedor actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe un proveedor con ese ID")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequestDto request
    ) {

        Supplier updated = new Supplier();
        updated.setName(request.getName());
        updated.setEmail(request.getEmail());
        updated.setPhone(request.getPhone());

        Supplier saved = supplierService.update(id, updated);

        SupplierResponseDto response = new SupplierResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getPhone()
        );

        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------
    @Operation(
            summary = "Eliminar un proveedor",
            description = "Elimina un proveedor existente usando su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Proveedor eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe un proveedor con ese ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        supplierService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
