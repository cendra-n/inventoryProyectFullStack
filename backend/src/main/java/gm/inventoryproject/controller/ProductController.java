package gm.inventoryproject.controller;

import gm.inventoryproject.dto.ProductDto;
import gm.inventoryproject.model.Category;
import gm.inventoryproject.model.Product;
import gm.inventoryproject.model.Supplier;
import gm.inventoryproject.repository.CategoryRepository;
import gm.inventoryproject.repository.SupplierRepository;
import gm.inventoryproject.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final IProductService productService;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    // Entity -> DTO
    private ProductDto toDto(Product p) {
        return ProductDto.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .stock(p.getStock())
                .categoryId(p.getCategory() != null ? p.getCategory().getId() : null)
                .supplierId(p.getSupplier() != null ? p.getSupplier().getId() : null)
                .build();
    }

    // DTO -> Entity (busca category y supplier por id)
    private Product toEntity(ProductDto dto) {
        Product p = new Product();
        p.setId(dto.getId()); // para update
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setStock(dto.getStock());

        if (dto.getCategoryId() != null) {
            Category cat = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));
            p.setCategory(cat);
        } else {
            p.setCategory(null);
        }

        if (dto.getSupplierId() != null) {
            Supplier sup = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + dto.getSupplierId()));
            p.setSupplier(sup);
        } else {
            p.setSupplier(null);
        }

        return p;
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto dto) {
        Product saved = productService.create(toEntity(dto));
        return ResponseEntity.status(201).body(toDto(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        Product p = productService.getById(id);
        return ResponseEntity.ok(toDto(p));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll() {
        List<ProductDto> list = productService.getAll().stream().map(this::toDto).toList();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductDto dto) {
        Product updated = productService.update(id, toEntity(dto));
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

