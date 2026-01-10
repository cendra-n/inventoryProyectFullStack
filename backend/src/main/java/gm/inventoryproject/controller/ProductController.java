package gm.inventoryproject.controller;

import gm.inventoryproject.dto.product.ProductRequestDto;
import gm.inventoryproject.dto.product.ProductResponseDto;
import gm.inventoryproject.model.Category;
import gm.inventoryproject.model.Product;
import gm.inventoryproject.model.Supplier;
import gm.inventoryproject.service.ICategoryService;
import gm.inventoryproject.service.IProductService;
import gm.inventoryproject.service.ISupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("inventory-app/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Endpoints para manejar productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    private final IProductService productService;
    private final ICategoryService categoryService;
    private final ISupplierService supplierService;

    // --------------------------
    // MAPPING MANUAL ENTITY->DTO
    // --------------------------
    private ProductResponseDto toDto(Product p) {
        return new ProductResponseDto(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStock(),
                p.getCategory().getId(),
                p.getSupplier().getId(),
                p.getCategory().getName(),
                p.getSupplier().getName()
        );
    }

    // --------------------------
    // MAPPING MANUAL DTO->ENTITY
    // --------------------------
    private Product toEntity(ProductRequestDto dto) {
        Category category = categoryService.getById(dto.getCategoryId());
        Supplier supplier = supplierService.getById(dto.getSupplierId());

        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .category(category)
                .supplier(supplier)
                .build();
    }

    // -------------------------------------------------------
    @Operation(summary = "Crear un nuevo producto")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@RequestBody @Valid ProductRequestDto dto) {
        Product saved = productService.create(toEntity(dto));
        return ResponseEntity.status(201).body(toDto(saved));
    }

    // -------------------------------------------------------
    @Operation(summary = "Obtener todos los productos")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> findAll() {
        List<ProductResponseDto> list = productService.getAll()
                .stream()
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(list);
    }

    // -------------------------------------------------------
    @Operation(summary = "Obtener un producto por ID")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable Long id) {
        Product p = productService.getById(id);
        return ResponseEntity.ok(toDto(p));
    }

    // -------------------------------------------------------
    @Operation(summary = "Buscar producto por nombre")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDto>> searchByName(
            @RequestParam String name) {

        List<ProductResponseDto> list = productService.searchByName(name)
                .stream()
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(list);
    }


    // -------------------------------------------------------
    @Operation(summary = "Actualizar un producto existente")
    @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid ProductRequestDto dto) {

        Product updated = productService.update(id, dto);

        return ResponseEntity.ok(toDto(updated));
    }


    // -------------------------------------------------------
    @Operation(summary = "Eliminar un producto")
    @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
