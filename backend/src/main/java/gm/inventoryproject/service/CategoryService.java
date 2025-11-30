package gm.inventoryproject.service;

import gm.inventoryproject.exceptions.DuplicateFieldException;
import gm.inventoryproject.exceptions.ResourceNotFoundException;
import gm.inventoryproject.model.Category;
import gm.inventoryproject.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoría no encontrada con id: " + id)
                );
    }

    // ---------------------------------------------------------
    // GET BY NAME
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public Category findByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoría no encontrada con nombre: " + name)
                );
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------
    @Override
    @Transactional
    public Category create(Category category) {

        if (categoryRepository.existsByName(category.getName())) {
            throw new DuplicateFieldException("Ya existe una categoría con ese nombre");
        }

        return categoryRepository.save(category);
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------
    @Override
    @Transactional
    public Category update(Long id, Category updatedCategory) {

        Category existing = getById(id);

        // Si cambia el nombre, validar duplicado
        if (!existing.getName().equalsIgnoreCase(updatedCategory.getName())
                && categoryRepository.existsByName(updatedCategory.getName())) {

            throw new DuplicateFieldException("El nombre de categoría ya existe");
        }

        existing.setName(updatedCategory.getName());
        existing.setDescription(updatedCategory.getDescription());

        return categoryRepository.save(existing);
    }

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------
    @Override
    @Transactional
    public void delete(Long id) {

        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría no encontrada con id: " + id);
        }

        categoryRepository.deleteById(id);
    }
}



