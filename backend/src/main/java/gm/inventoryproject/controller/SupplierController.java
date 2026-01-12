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
@Tag(name = "Supplier", description = "Endpoints para administrar proveedores")
@CrossOrigin(origins = "http://localhost:4200")
public class SupplierController {

    private final ISupplierService supplierService;

    public SupplierController(ISupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // ---------------------------------------------------------
    // MAPPERS
    // ---------------------------------------------------------
    private SupplierResponseDto toDto(Supplier s) {
        return new SupplierResponseDto(
                s.getId(),
                s.getName(),
                s.getEmail(),
                s.getPhone()
        );
    }

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------
    @Operation(summary = "Obtener todos los proveedores")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<SupplierResponseDto>> getAll() {

        List<SupplierResponseDto> response = supplierService.getAll()
                .stream()
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------
    @Operation(summary = "Obtener proveedor por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Proveedor encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un proveedor con ese ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> getById(@PathVariable Long id) {

        Supplier s = supplierService.getById(id);
        return ResponseEntity.ok(toDto(s));
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------
    @Operation(summary = "Crear un nuevo proveedor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Proveedor creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<SupplierResponseDto> create(
            @Valid @RequestBody SupplierRequestDto request
    ) {
        Supplier saved = supplierService.createFromDto(request);
        return ResponseEntity.status(201).body(toDto(saved));
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------
    @Operation(summary = "Actualizar proveedor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Proveedor actualizado"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequestDto request
    ) {
        Supplier updated = supplierService.updateFromDto(id, request);
        return ResponseEntity.ok(toDto(updated));
    }

    @Operation(
            summary = "Eliminar un proveedor",
            description = "Elimina un proveedor existente. No se permite eliminar un proveedor que tenga productos asociados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Proveedor eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe el proveedor a eliminar"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar el proveedor porque tiene productos asociados")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        supplierService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------------------------------------------
    // GET BY NAME
    // ------------------------------------------------------

    @Operation(
            summary = "Buscar proveedor por nombre (parcial)",
            description = "Devuelve una lista de proveedor que contengan el texto enviado"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @ApiResponse(responseCode = "404", description = "No existe un proveedor con ese nombre")
    @GetMapping("/search")
    public ResponseEntity<List<SupplierResponseDto>> search(@RequestParam String name) {

        List<SupplierResponseDto> list = supplierService.searchByName(name)
                .stream()
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(list);
    }

}
