package gm.inventoryproject.service;

import gm.inventoryproject.model.Product;

import java.util.List;

public interface IProductService {

    List<Product> getAll();

    Product getById(Long id);

    Product findByName(String name);

    Product create(Product product);

    Product update(Long id, Product product);

    void delete(Long id);
}

