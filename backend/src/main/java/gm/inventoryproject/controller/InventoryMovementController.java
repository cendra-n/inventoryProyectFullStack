package gm.inventoryproject.controller;

import gm.inventoryproject.dto.inventorymovement.InventoryMovementRequestDto;
import gm.inventoryproject.dto.inventorymovement.InventoryMovementResponseDto;
import gm.inventoryproject.model.InventoryMovement;
import gm.inventoryproject.model.Product;
import gm.inventoryproject.service.IInventoryMovementService;
import gm.inventoryproject.service.IProductService;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/inventory-app/movements")
@Tag(name = "inventory movements", description = "Endpoints para gestionar movimientos de inventario")
@CrossOrigin(origins = "http://localhost:4200")
public class InventoryMovementController {

    private final IInventoryMovementService movementService;
    private final IProductService productService;

    public InventoryMovementController(
            IInventoryMovementService movementService,
            IProductService productService
    ) {
        this.movementService = movementService;
        this.productService = productService;
    }

    // -------------------------------------------
    // MAPPER
    // -------------------------------------------
    private InventoryMovementResponseDto toResponse(InventoryMovement m) {
        return new InventoryMovementResponseDto(
                m.getId(),
                m.getQuantity(),
                m.getType().name(),
                m.getDate(),
                m.getProduct().getId(),
                m.getProduct().getName()
        );
    }

    // -------------------------------------------
    // GET ALL
    // -------------------------------------------
    @Operation(summary = "Obtener todos los movimientos de inventario")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<InventoryMovementResponseDto>> getAll() {
        List<InventoryMovementResponseDto> list = movementService.getAll()
                .stream().map(this::toResponse).toList();

        return ResponseEntity.ok(list);
    }

    // -------------------------------------------
    // GET BY ID
    // -------------------------------------------
    @Operation(summary = "Obtener un movimiento por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un movimiento con ese ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InventoryMovementResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(movementService.getById(id)));
    }

    // -------------------------------------------
// CREATE
// -------------------------------------------
    @Operation(summary = "Registrar un nuevo movimiento de inventario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movimiento creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<InventoryMovementResponseDto> create(
            @Valid @RequestBody InventoryMovementRequestDto request
    ) {
        InventoryMovement saved = movementService.createFromDto(request);
        return ResponseEntity.status(201).body(toResponse(saved));
    }



    // -------------------------------------------
    // GET MOVEMENTS BY PRODUCT
    // -------------------------------------------
    @Operation(summary = "Obtener movimientos filtrados por producto")
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<InventoryMovementResponseDto>> getByProduct(@PathVariable Long productId) {
        List<InventoryMovementResponseDto> list = movementService.getByProduct(productId)
                .stream().map(this::toResponse).toList();

        return ResponseEntity.ok(list);
    }

    // -------------------------------------------
    // GET MOVEMENTS BY DATE RANGE
    // -------------------------------------------
    @Operation(summary = "Obtener movimientos filtrados por rango de fechas")
    @GetMapping("/dates")
    public ResponseEntity<List<InventoryMovementResponseDto>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<InventoryMovementResponseDto> list = movementService.getByDateRange(start, end)
                .stream().map(this::toResponse).toList();

        return ResponseEntity.ok(list);
    }

    // -------------------------------------------
    // GET BY PRODUCT + DATE
    // -------------------------------------------
    @Operation(summary = "Obtener movimientos por producto y rango de fecha")
    @GetMapping("/product/{productId}/dates")
    public ResponseEntity<List<InventoryMovementResponseDto>> getByProductAndDate(
            @PathVariable Long productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<InventoryMovementResponseDto> list = movementService.getByProductAndDate(productId, start, end)
                .stream().map(this::toResponse).toList();

        return ResponseEntity.ok(list);
    }
}
