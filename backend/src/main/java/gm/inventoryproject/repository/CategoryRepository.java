package gm.inventoryproject.repository;

import gm.inventoryproject.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Category.
 * <p>
 * Proporciona métodos de consulta personalizados para búsquedas
 * por nombre y validaciones de existencia.
 * </p>
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Para buscar por nombre (opcional, muy útil)
    /**
     * Busca una categoría por su nombre exacto.
     * @param name Nombre de la categoría a buscar.
     * @return Optional con la categoría encontrada, o vacío si no existe.
     */
    Optional<Category> findByName(String name);

    // Parcial (LIKE %name%)
    /**
     * Busca categorías cuyo nombre contenga el texto especificado (case-insensitive).
     * @param name Texto a buscar en el nombre.
     * @return Lista de categorías que coinciden con la búsqueda.
     */
    List<Category> findByNameContainingIgnoreCase(String name);

    // Para validar si existe por nombre
    /**
     * Verifica si existe una categoría con el nombre especificado.
     * @param name Nombre a verificar.
     * @return true si existe una categoría con ese nombre, false en caso contrario.
     */
    boolean existsByName(String name);

    boolean existsById(Long id);


}

