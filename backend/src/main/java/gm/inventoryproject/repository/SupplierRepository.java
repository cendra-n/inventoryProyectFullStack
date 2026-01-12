package gm.inventoryproject.repository;

import gm.inventoryproject.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<Supplier> findByNameIgnoreCase(String name);





}

