package gm.inventoryproject.controller;

import gm.inventoryproject.dto.CategoryDto;
import gm.inventoryproject.model.Category;
import gm.inventoryproject.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {

    private final ICategoryService categoryService;

    // Convert Entity -> DTO
    private CategoryDto toDto(Category c) {
        return new CategoryDto(c.getId(), c.getName());
    }

    // Convert DTO -> Entity
    private Category toEntity(CategoryDto dto) {
        Category c = new Category();
        c.setId(dto.getId());     // important for update
        c.setName(dto.getName());
        c.setDescription(null);   // your DTO does not include description
        return c;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto dto) {
        Category saved = categoryService.create(toEntity(dto));
        return ResponseEntity.status(201).body(toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAll() {
        List<CategoryDto> list = categoryService.getAll()
                .stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> findById(@PathVariable Long id) {
        Category c = categoryService.getById(id);
        return ResponseEntity.ok(toDto(c));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryDto> findByName(@PathVariable String name) {
        Category c = categoryService.findByName(name);
        return ResponseEntity.ok(toDto(c));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(
            @PathVariable Long id,
            @RequestBody CategoryDto dto
    ) {
        Category updated = categoryService.update(id, toEntity(dto));
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

