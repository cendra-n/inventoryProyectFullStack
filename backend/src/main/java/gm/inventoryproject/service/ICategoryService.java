package gm.inventoryproject.service;

import gm.inventoryproject.model.Category;

import java.util.List;

public interface ICategoryService {

    List<Category> getAll();

    Category getById(Long id);

    Category findByName(String name);

    Category create(Category category);

    Category update(Long id, Category category);

    void delete(Long id);
}

