package gm.inventoryproject.service;

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
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Supplier findByName(String name) {
        // If you later add a method in repository like findByName, use it.
        // For now we'll search by name in memory (inefficient) — better to add repo method later.
        return supplierRepository.findAll().stream()
                .filter(s -> name.equalsIgnoreCase(s.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Supplier not found with name: " + name));
    }

    @Override
    @Transactional
    public Supplier create(Supplier supplier) {
        // optional: check duplicates (email/phone) if you add existsByEmail / existsByPhone in repo
        return supplierRepository.save(supplier);
    }

    @Override
    @Transactional
    public Supplier update(Long id, Supplier updatedSupplier) {
        Supplier existing = getById(id);

        existing.setName(updatedSupplier.getName());
        existing.setEmail(updatedSupplier.getEmail());
        existing.setPhone(updatedSupplier.getPhone());
        // if your Supplier has address or other fields, set them here
        // existing.setAddress(updatedSupplier.getAddress());

        return supplierRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new RuntimeException("Supplier not found with id: " + id);
        }
        supplierRepository.deleteById(id);
    }
}

