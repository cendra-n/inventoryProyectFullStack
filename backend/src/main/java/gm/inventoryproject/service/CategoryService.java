package gm.inventoryproject.service;

import gm.inventoryproject.model.Category;
import gm.inventoryproject.repository.CategoryRepository;
import gm.inventoryproject.service.ICategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Category findByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() ->
                        new RuntimeException("Category not found with name: " + name)
                );
    }

    @Override
    public Category create(Category category) {

        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException(
                    "Category with name '" + category.getName() + "' already exists"
            );
        }

        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, Category updatedCategory) {

        Category existing = getById(id);

        if (!existing.getName().equals(updatedCategory.getName())
                && categoryRepository.existsByName(updatedCategory.getName())) {

            throw new RuntimeException(
                    "Another category with name '" + updatedCategory.getName() + "' already exists"
            );
        }

        existing.setName(updatedCategory.getName());
        existing.setDescription(updatedCategory.getDescription());

        return categoryRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }

        categoryRepository.deleteById(id);
    }
}


