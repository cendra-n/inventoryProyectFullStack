package gm.inventoryproject.repository;

import gm.inventoryproject.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    //Optional<Supplier> findByNameIgnoreCase(String name);

    // Parcial (LIKE %name%)
    List<Supplier> findByNameContainingIgnoreCase(String name);

    // Para validar si existe por nombre
    boolean existsByName(String name);

    boolean existsById(Long id);




}

