package gm.inventoryproject.service;

import gm.inventoryproject.exceptions.DuplicateFieldException;
import gm.inventoryproject.exceptions.ResourceNotFoundException;
import gm.inventoryproject.model.Supplier;
import gm.inventoryproject.repository.SupplierRepository;
import gm.inventoryproject.service.ISupplierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierService implements ISupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getAll() {
        return supplierRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Supplier getById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Supplier findByName(String name) {

        return supplierRepository.findAll().stream()
                .filter(s -> name.equalsIgnoreCase(s.getName()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con nombre: " + name));
    }

    @Override
    @Transactional
    public Supplier create(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    @Transactional
    public Supplier update(Long id, Supplier updatedSupplier) {
        Supplier existing = getById(id);

        existing.setName(updatedSupplier.getName());
        existing.setEmail(updatedSupplier.getEmail());
        existing.setPhone(updatedSupplier.getPhone());


        return supplierRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Proveedor no encontrado con id: " + id);
        }
        supplierRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Supplier createFromDto(gm.inventoryproject.dto.supplier.SupplierRequestDto dto) {

        if (dto.getEmail() != null && supplierRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateFieldException("El email ya está registrado");
        }

        if (dto.getPhone() != null && supplierRepository.existsByPhone(dto.getPhone())) {
            throw new DuplicateFieldException("El teléfono ya está registrado");
        }

        Supplier supplier = Supplier.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();

        return supplierRepository.save(supplier);
    }

}

