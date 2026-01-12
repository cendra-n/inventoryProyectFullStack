package gm.inventoryproject.service;

import gm.inventoryproject.dto.supplier.SupplierRequestDto;
import gm.inventoryproject.exceptions.CategoryHasProductsException;
import gm.inventoryproject.exceptions.DuplicateFieldException;
import gm.inventoryproject.exceptions.ResourceNotFoundException;
import gm.inventoryproject.exceptions.SupplierHasProductsException;
import gm.inventoryproject.model.Supplier;
import gm.inventoryproject.repository.ProductRepository;
import gm.inventoryproject.repository.SupplierRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierService implements ISupplierService {

    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;


    public SupplierService(SupplierRepository supplierRepository,
                           ProductRepository productRepository ) {

        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
    }

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getAll() {
        return supplierRepository.findAll();
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public Supplier getById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proveedor no encontrado con id: " + id
                ));
    }

    // ---------------------------------------------------------
    // GET BY NAME
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public Supplier findByName(String name) {
        return supplierRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proveedor no encontrado con nombre: " + name
                ));
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------
    @Override
    @Transactional
    public Supplier createFromDto(SupplierRequestDto dto) {

        // Validaciones únicas
        if (supplierRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateFieldException("El email ya está registrado");
        }

        if (supplierRepository.existsByPhone(dto.getPhone())) {
            throw new DuplicateFieldException("El teléfono ya está registrado");
        }

        Supplier supplier = Supplier.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();

        return supplierRepository.save(supplier);
    }

    // ---------------------------------------------------------
    // UPDATE FROM DTO
    // ---------------------------------------------------------
    @Override
    @Transactional
    public Supplier updateFromDto(Long id, SupplierRequestDto dto) {

        Supplier existing = getById(id);

        // Validación: EMAIL duplicado solo si cambió
        if (!existing.getEmail().equalsIgnoreCase(dto.getEmail())
                && supplierRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateFieldException("El email ya está registrado");
        }

        // Validación: PHONE duplicado solo si cambió
        if (!existing.getPhone().equals(dto.getPhone())
                && supplierRepository.existsByPhone(dto.getPhone())) {
            throw new DuplicateFieldException("El teléfono ya está registrado");
        }

        // Setear campos
        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());

        return supplierRepository.save(existing);
    }

// ---------------------------------------------------------
// DELETE
// ---------------------------------------------------------
@Override
@Transactional
public void delete(Long id) {

    if (!supplierRepository.existsById(id)) {
        throw new ResourceNotFoundException(
                "Proveedor no encontrado con id: " + id
        );
    }

    if (productRepository.existsBySupplierId(id)) {
        throw new SupplierHasProductsException(
                "No se puede eliminar el proveedor porque tiene productos asociados"
        );
    }

    supplierRepository.deleteById(id);
}






}
