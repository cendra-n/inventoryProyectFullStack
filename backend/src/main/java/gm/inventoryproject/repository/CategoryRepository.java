package gm.inventoryproject.repository;

import gm.inventoryproject.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Para buscar por nombre (opcional, muy útil)
    Optional<Category> findByName(String name);

    // Para validar si existe por nombre
    boolean existsByName(String name);
}

