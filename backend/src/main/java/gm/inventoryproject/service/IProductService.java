package gm.inventoryproject.service;

import gm.inventoryproject.dto.product.ProductRequestDto;
import gm.inventoryproject.model.Product;

import java.util.List;

public interface IProductService {

    List<Product> getAll();

    Product getById(Long id);

    Product findByName(String name);

    Product create(Product entity);

    Product update(Long id, ProductRequestDto dto);

    void delete(Long id);
}


