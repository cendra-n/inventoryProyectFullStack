package gm.inventoryproject.controller;

import gm.inventoryproject.dto.category.CategoryRequestDto;
import gm.inventoryproject.dto.category.CategoryResponseDto;
import gm.inventoryproject.model.Category;
import gm.inventoryproject.service.ICategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("inventory-app/categories")
@RequiredArgsConstructor
@Tag(
        name = "Category",
        description = "Endpoints para manejar las categorías de los productos"
)
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    private final ICategoryService categoryService;

    // ------------------------------------------------------
    // MAPPERS (manuales, sin ModelMapper)
    // ------------------------------------------------------
    private CategoryResponseDto toResponseDto(Category c) {
        return new CategoryResponseDto(
                c.getId(),
                c.getName(),
                c.getDescription()
        );
    }

    private Category toEntity(CategoryRequestDto dto) {
        Category c = new Category();
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());
        return c;
    }

    // ------------------------------------------------------
    // CREATE
    // ------------------------------------------------------
    @Operation(
            summary = "Crear una nueva categoría",
            description = "Recibe un CategoryRequestDto y crea una nueva categoría en el sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o nombre duplicado")
    })
    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(
            @Valid @RequestBody CategoryRequestDto dto
    ) {
        Category saved = categoryService.create(toEntity(dto));
        return ResponseEntity.status(201).body(toResponseDto(saved));
    }

    // ------------------------------------------------------
    // GET ALL
    // ------------------------------------------------------
    @Operation(
            summary = "Obtener todas las categorías",
            description = "Devuelve una lista con todas las categorías registradas."
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> findAll() {
        List<CategoryResponseDto> list = categoryService.getAll()
                .stream()
                .map(this::toResponseDto)
                .toList();

        return ResponseEntity.ok(list);
    }

    // ------------------------------------------------------
    // GET BY ID
    // ------------------------------------------------------
    @Operation(
            summary = "Buscar categoría por ID",
            description = "Devuelve una categoría en base a su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "No existe una categoría con ese ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> findById(@PathVariable Long id) {
        Category c = categoryService.getById(id);
        return ResponseEntity.ok(toResponseDto(c));
    }

    // ------------------------------------------------------
    // GET BY NAME
    // ------------------------------------------------------

    @Operation(
            summary = "Buscar categorías por nombre (parcial)",
            description = "Devuelve una lista de categorías que contengan el texto enviado"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @ApiResponse(responseCode = "404", description = "No existe una categoría con ese nombre")
    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponseDto>> search(@RequestParam String name) {

        List<CategoryResponseDto> list = categoryService.searchByName(name)
                .stream()
                .map(this::toResponseDto)
                .toList();

        return ResponseEntity.ok(list);
    }


    // ------------------------------------------------------
    // UPDATE
    // ------------------------------------------------------
    @Operation(
            summary = "Actualizar una categoría",
            description = "Modifica una categoría existente en base al ID proporcionado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o nombre duplicado"),
            @ApiResponse(responseCode = "404", description = "No existe la categoría a actualizar")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDto dto
    ) {
        Category updatedData = toEntity(dto);
        Category updated = categoryService.update(id, updatedData);
        return ResponseEntity.ok(toResponseDto(updated));
    }

    // ------------------------------------------------------
    // DELETE
    // ------------------------------------------------------
    @Operation(
            summary = "Eliminar una categoría",
            description = "Elimina una categoría existente en base al ID enviado. " +
                    "No se permite eliminar una categoría que tenga productos asociados."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Categoría eliminada correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe la categoría a eliminar"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "No se puede eliminar la categoría porque tiene productos asociados"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
