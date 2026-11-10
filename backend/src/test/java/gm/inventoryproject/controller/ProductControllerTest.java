package gm.inventoryproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gm.inventoryproject.dto.product.ProductRequestDto;
import gm.inventoryproject.dto.product.ProductResponseDto;
import gm.inventoryproject.exceptions.DuplicateFieldException;
import gm.inventoryproject.exceptions.ProductHasMovementsException;
import gm.inventoryproject.exceptions.ResourceNotFoundException;
import gm.inventoryproject.model.Category;
import gm.inventoryproject.model.Product;
import gm.inventoryproject.model.Supplier;
import gm.inventoryproject.service.ICategoryService;
import gm.inventoryproject.service.IProductService;
import gm.inventoryproject.service.ISupplierService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitarios para ProductController.
 * <p>
 * Verifica los endpoints HTTP utilizando @WebMvcTest y MockMvc.
 * </p>
 */
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IProductService productService;

    @MockBean
    private ICategoryService categoryService;

    @MockBean
    private ISupplierService supplierService;

    // ------------------------------------------------------
    // CREATE
    // ------------------------------------------------------
    @Test
    void testCreateSuccess() throws Exception {
        // Arrange
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("Botella de agua");
        requestDto.setDescription("Botella de agua no gasificada de 500ml");
        requestDto.setPrice(2999.99);
        requestDto.setStock(15);
        requestDto.setCategoryId(1L);
        requestDto.setSupplierId(4L);

        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder().id(4L).name("Simeflat SA").build();
        Product savedProduct = Product.builder()
                .id(1L)
                .name("Botella de agua")
                .description("Botella de agua no gasificada de 500ml")
                .price(2999.99)
                .stock(15)
                .category(category)
                .supplier(supplier)
                .build();

        when(categoryService.getById(1L)).thenReturn(category);
        when(supplierService.getById(4L)).thenReturn(supplier);
        when(productService.create(any(Product.class))).thenReturn(savedProduct);

        // Act & Assert
        mockMvc.perform(post("/inventory-app/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Botella de agua"))
                .andExpect(jsonPath("$.description").value("Botella de agua no gasificada de 500ml"))
                .andExpect(jsonPath("$.price").value(2999.99))
                .andExpect(jsonPath("$.stock").value(15))
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.supplierId").value(4))
                .andExpect(jsonPath("$.categoryName").value("Bebidas"))
                .andExpect(jsonPath("$.supplierName").value("Simeflat SA"));
    }

    @Test
    void testCreateValidationError() throws Exception {
        // Arrange
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("A"); // Too short
        requestDto.setDescription("B"); // Too short
        requestDto.setPrice(0.0); // Not positive
        requestDto.setStock(-1); // Negative
        requestDto.setCategoryId(null); // Null
        requestDto.setSupplierId(null); // Null

        // Act & Assert
        mockMvc.perform(post("/inventory-app/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.messages").exists());
    }

    @Test
    void testCreateDuplicateName() throws Exception {
        // Arrange
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("Botella de agua");
        requestDto.setDescription("Botella de agua no gasificada de 500ml");
        requestDto.setPrice(2999.99);
        requestDto.setStock(15);
        requestDto.setCategoryId(1L);
        requestDto.setSupplierId(4L);

        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder().id(4L).name("Simeflat SA").build();

        when(categoryService.getById(1L)).thenReturn(category);
        when(supplierService.getById(4L)).thenReturn(supplier);
        when(productService.create(any(Product.class)))
                .thenThrow(new DuplicateFieldException("Ya existe un producto con el nombre: Botella de agua"));

        // Act & Assert
        mockMvc.perform(post("/inventory-app/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Ya existe un producto con el nombre: Botella de agua"));
    }

    // ------------------------------------------------------
    // GET ALL
    // ------------------------------------------------------
    @Test
    void testFindAll() throws Exception {
        // Arrange
        Category cat1 = Category.builder().id(1L).name("Bebidas").build();
        Supplier sup1 = Supplier.builder().id(4L).name("Simeflat SA").build();
        Product prod1 = Product.builder()
                .id(1L)
                .name("Botella de agua")
                .price(2999.99)
                .stock(15)
                .category(cat1)
                .supplier(sup1)
                .build();

        Category cat2 = Category.builder().id(2L).name("Alimentos").build();
        Supplier sup2 = Supplier.builder().id(5L).name("Tech Solutions").build();
        Product prod2 = Product.builder()
                .id(2L)
                .name("Galletitas")
                .price(1500.0)
                .stock(50)
                .category(cat2)
                .supplier(sup2)
                .build();

        List<Product> products = Arrays.asList(prod1, prod2);

        when(productService.getAll()).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/inventory-app/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Botella de agua"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Galletitas"));
    }

    // ------------------------------------------------------
    // GET BY ID
    // ------------------------------------------------------
    @Test
    void testFindByIdSuccess() throws Exception {
        // Arrange
        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder().id(4L).name("Simeflat SA").build();
        Product product = Product.builder()
                .id(1L)
                .name("Botella de agua")
                .description("Botella de agua no gasificada de 500ml")
                .price(2999.99)
                .stock(15)
                .category(category)
                .supplier(supplier)
                .build();

        when(productService.getById(1L)).thenReturn(product);

        // Act & Assert
        mockMvc.perform(get("/inventory-app/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Botella de agua"))
                .andExpect(jsonPath("$.description").value("Botella de agua no gasificada de 500ml"))
                .andExpect(jsonPath("$.price").value(2999.99))
                .andExpect(jsonPath("$.stock").value(15))
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.supplierId").value(4))
                .andExpect(jsonPath("$.categoryName").value("Bebidas"))
                .andExpect(jsonPath("$.supplierName").value("Simeflat SA"));
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        // Arrange
        when(productService.getById(999L))
                .thenThrow(new ResourceNotFoundException("No se encontró un producto con ID: 999"));

        // Act & Assert
        mockMvc.perform(get("/inventory-app/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("No se encontró un producto con ID: 999"));
    }

    // ------------------------------------------------------
    // SEARCH BY NAME
    // ------------------------------------------------------
    @Test
    void testSearchByName() throws Exception {
        // Arrange
        Category cat1 = Category.builder().id(1L).name("Bebidas").build();
        Supplier sup1 = Supplier.builder().id(4L).name("Simeflat SA").build();
        Product prod1 = Product.builder()
                .id(1L)
                .name("Botella de agua")
                .price(2999.99)
                .stock(15)
                .category(cat1)
                .supplier(sup1)
                .build();
        Product prod2 = Product.builder()
                .id(2L)
                .name("Agua mineral")
                .price(2500.0)
                .stock(20)
                .category(cat1)
                .supplier(sup1)
                .build();
        List<Product> products = Arrays.asList(prod1, prod2);

        when(productService.searchByName("agua")).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/inventory-app/products/search")
                        .param("name", "agua"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Botella de agua"))
                .andExpect(jsonPath("$[1].name").value("Agua mineral"));
    }

    // ------------------------------------------------------
    // UPDATE
    // ------------------------------------------------------
    @Test
    void testUpdateSuccess() throws Exception {
        // Arrange
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("Botella de agua Actualizada");
        requestDto.setDescription("Nueva descripción");
        requestDto.setPrice(3500.0);
        requestDto.setStock(20);
        requestDto.setCategoryId(1L);
        requestDto.setSupplierId(4L);

        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder().id(4L).name("Simeflat SA").build();
        Product updatedProduct = Product.builder()
                .id(1L)
                .name("Botella de agua Actualizada")
                .description("Nueva descripción")
                .price(3500.0)
                .stock(15) // Stock no se modifica
                .category(category)
                .supplier(supplier)
                .build();

        when(productService.update(anyLong(), any(ProductRequestDto.class))).thenReturn(updatedProduct);

        // Act & Assert
        mockMvc.perform(put("/inventory-app/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Botella de agua Actualizada"))
                .andExpect(jsonPath("$.description").value("Nueva descripción"))
                .andExpect(jsonPath("$.price").value(3500.0))
                .andExpect(jsonPath("$.stock").value(15)) // Stock no debería cambiar
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.supplierId").value(4));
    }

    @Test
    void testUpdateValidationError() throws Exception {
        // Arrange
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName(""); // Empty
        requestDto.setDescription("Descripción válida");
        requestDto.setPrice(100.0);
        requestDto.setStock(10);
        requestDto.setCategoryId(1L);
        requestDto.setSupplierId(1L);

        // Act & Assert
        mockMvc.perform(put("/inventory-app/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.messages").exists());
    }

    @Test
    void testUpdateNotFound() throws Exception {
        // Arrange
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("Nombre");
        requestDto.setDescription("Descripción");
        requestDto.setPrice(100.0);
        requestDto.setStock(10);
        requestDto.setCategoryId(1L);
        requestDto.setSupplierId(1L);

        when(productService.update(anyLong(), any(ProductRequestDto.class)))
                .thenThrow(new ResourceNotFoundException("No se encontró un producto con ID: 999"));

        // Act & Assert
        mockMvc.perform(put("/inventory-app/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("No se encontró un producto con ID: 999"));
    }

    @Test
    void testUpdateDuplicateName() throws Exception {
        // Arrange
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("Nombre Duplicado");
        requestDto.setDescription("Descripción");
        requestDto.setPrice(100.0);
        requestDto.setStock(10);
        requestDto.setCategoryId(1L);
        requestDto.setSupplierId(1L);

        when(productService.update(anyLong(), any(ProductRequestDto.class)))
                .thenThrow(new DuplicateFieldException("Ya existe un producto con el nombre: Nombre Duplicado"));

        // Act & Assert
        mockMvc.perform(put("/inventory-app/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Ya existe un producto con el nombre: Nombre Duplicado"));
    }

    // ------------------------------------------------------
    // DELETE
    // ------------------------------------------------------
    @Test
    void testDeleteSuccess() throws Exception {
        // Arrange
        doNothing().when(productService).delete(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/inventory-app/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteNotFound() throws Exception {
        // Arrange
        doThrow(new ResourceNotFoundException("No se encontró un producto con ID: 999"))
                .when(productService).delete(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/inventory-app/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("No se encontró un producto con ID: 999"));
    }

    @Test
    void testDeleteHasMovements() throws Exception {
        // Arrange
        doThrow(new ProductHasMovementsException(
                "No se puede eliminar un producto con movimientos de inventario"))
                .when(productService).delete(anyLong());

        // Act & Assert
        // ProductHasMovementsException devuelve Map con solo clave "error" (sin timestamp)
        mockMvc.perform(delete("/inventory-app/products/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("No se puede eliminar un producto con movimientos de inventario"));
    }

}
