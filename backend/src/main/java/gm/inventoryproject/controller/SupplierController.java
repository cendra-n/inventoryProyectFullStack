package gm.inventoryproject.controller;

import gm.inventoryproject.dto.SupplierDto;
import gm.inventoryproject.model.Supplier;
import gm.inventoryproject.service.ISupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SupplierController {

    private final ISupplierService supplierService;

    // Entity -> DTO
    private SupplierDto toDto(Supplier s) {
        return SupplierDto.builder()
                .id(s.getId())
                .name(s.getName())
                .email(s.getEmail())
                .phone(s.getPhone())
                .build();
    }

    // DTO -> Entity
    private Supplier toEntity(SupplierDto dto) {
        Supplier s = new Supplier();
        s.setId(dto.getId()); // helpful for update flows
        s.setName(dto.getName());
        s.setEmail(dto.getEmail());
        s.setPhone(dto.getPhone());
        return s;
    }

    @PostMapping
    public ResponseEntity<SupplierDto> create(@RequestBody SupplierDto dto) {
        Supplier saved = supplierService.create(toEntity(dto));
        return ResponseEntity.status(201).body(toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<SupplierDto>> getAll() {
        List<SupplierDto> list = supplierService.getAll()
                .stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toDto(supplierService.getById(id)));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SupplierDto> getByName(@PathVariable String name) {
        return ResponseEntity.ok(toDto(supplierService.findByName(name)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDto> update(@PathVariable Long id, @RequestBody SupplierDto dto) {
        Supplier updated = supplierService.update(id, toEntity(dto));
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        supplierService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
