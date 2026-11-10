package gm.inventoryproject.service;

import gm.inventoryproject.dto.product.ProductRequestDto;
import gm.inventoryproject.exceptions.DuplicateFieldException;
import gm.inventoryproject.exceptions.ProductHasMovementsException;
import gm.inventoryproject.exceptions.ResourceNotFoundException;
import gm.inventoryproject.model.Category;
import gm.inventoryproject.model.Product;
import gm.inventoryproject.model.Supplier;
import gm.inventoryproject.repository.InventoryMovementRepository;
import gm.inventoryproject.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ProductService.
 * <p>
 * Verifica la lógica de negocio utilizando Mockito para los repositorios y servicios.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ICategoryService categoryService;

    @Mock
    private ISupplierService supplierService;

    @Mock
    private InventoryMovementRepository inventoryMovementRepository;

    @InjectMocks
    private ProductService productService;

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------
    @Test
    void testGetAll() {
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

        List<Product> expectedList = Arrays.asList(prod1, prod2);
        when(productRepository.findAll()).thenReturn(expectedList);

        // Act
        List<Product> result = productService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Botella de agua", result.get(0).getName());
        assertEquals("Galletitas", result.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------
    @Test
    void testGetByIdSuccess() {
        // Arrange
        Long id = 1L;
        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder().id(4L).name("Simeflat SA").build();
        Product expected = Product.builder()
                .id(id)
                .name("Botella de agua")
                .price(2999.99)
                .stock(15)
                .category(category)
                .supplier(supplier)
                .build();
        when(productRepository.findById(id)).thenReturn(Optional.of(expected));

        // Act
        Product result = productService.getById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Botella de agua", result.getName());
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void testGetByIdNotFound() {
        // Arrange
        Long id = 999L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.getById(id)
        );
        assertEquals("No se encontró un producto con ID: " + id, exception.getMessage());
        verify(productRepository, times(1)).findById(id);
    }

    // ---------------------------------------------------------
    // SEARCH BY NAME
    // ---------------------------------------------------------
    @Test
    void testSearchByName() {
        // Arrange
        String searchTerm = "agua";
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
        List<Product> expectedList = Arrays.asList(prod1, prod2);
        when(productRepository.findByNameContainingIgnoreCase(searchTerm)).thenReturn(expectedList);

        // Act
        List<Product> result = productService.searchByName(searchTerm);

        // Assert
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase(searchTerm);
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------
    @Test
    void testCreateSuccess() {
        // Arrange
        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder().id(4L).name("Simeflat SA").build();
        Product toCreate = Product.builder()
                .name("Nuevo Producto")
                .description("Descripción de prueba")
                .price(100.0)
                .stock(10)
                .category(category)
                .supplier(supplier)
                .build();

        Product saved = Product.builder()
                .id(1L)
                .name("Nuevo Producto")
                .description("Descripción de prueba")
                .price(100.0)
                .stock(10)
                .category(category)
                .supplier(supplier)
                .build();

        when(productRepository.existsByName("Nuevo Producto")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        // Act
        Product result = productService.create(toCreate);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Nuevo Producto", result.getName());
        verify(productRepository, times(1)).existsByName("Nuevo Producto");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testCreateDuplicateName() {
        // Arrange
        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder().id(4L).name("Simeflat SA").build();
        Product toCreate = Product.builder()
                .name("Botella de agua")
                .description("Descripción")
                .price(100.0)
                .stock(10)
                .category(category)
                .supplier(supplier)
                .build();

        when(productRepository.existsByName("Botella de agua")).thenReturn(true);

        // Act & Assert
        DuplicateFieldException exception = assertThrows(
                DuplicateFieldException.class,
                () -> productService.create(toCreate)
        );
        assertEquals("Ya existe un producto con el nombre: Botella de agua", exception.getMessage());
        verify(productRepository, times(1)).existsByName("Botella de agua");
        verify(productRepository, never()).save(any(Product.class));
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------
    @Test
    void testUpdateSuccessWithoutMovements() {
        // Arrange
        Long id = 1L;
        Category oldCategory = Category.builder().id(1L).name("Bebidas").build();
        Supplier oldSupplier = Supplier.builder().id(4L).name("Simeflat SA").build();
        Product existing = Product.builder()
                .id(id)
                .name("Nombre Antiguo")
                .description("Descripción antigua")
                .price(100.0)
                .stock(10)
                .category(oldCategory)
                .supplier(oldSupplier)
                .build();

        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre Nuevo");
        dto.setDescription("Descripción nueva");
        dto.setPrice(200.0);
        dto.setStock(20);
        dto.setCategoryId(2L);
        dto.setSupplierId(5L);

        Category newCategory = Category.builder().id(2L).name("Alimentos").build();
        Supplier newSupplier = Supplier.builder().id(5L).name("Tech Solutions").build();

        Product saved = Product.builder()
                .id(id)
                .name("Nombre Nuevo")
                .description("Descripción nueva")
                .price(200.0)
                .stock(10) // Stock no se modifica
                .category(newCategory)
                .supplier(newSupplier)
                .build();

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(productRepository.existsByName("Nombre Nuevo")).thenReturn(false);
        when(inventoryMovementRepository.existsByProductId(id)).thenReturn(false);
        when(categoryService.getById(2L)).thenReturn(newCategory);
        when(supplierService.getById(5L)).thenReturn(newSupplier);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        // Act
        Product result = productService.update(id, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Nombre Nuevo", result.getName());
        assertEquals("Descripción nueva", result.getDescription());
        assertEquals(200.0, result.getPrice());
        assertEquals(10, result.getStock()); // Stock no debería cambiar
        assertEquals(newCategory, result.getCategory());
        assertEquals(newSupplier, result.getSupplier());
        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).existsByName("Nombre Nuevo");
        verify(inventoryMovementRepository, times(1)).existsByProductId(id);
        verify(categoryService, times(1)).getById(2L);
        verify(supplierService, times(1)).getById(5L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateSuccessWithMovements() {
        // Arrange
        Long id = 1L;
        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder().id(4L).name("Simeflat SA").build();
        Product existing = Product.builder()
                .id(id)
                .name("Nombre Antiguo")
                .description("Descripción antigua")
                .price(100.0)
                .stock(10)
                .category(category)
                .supplier(supplier)
                .build();

        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre Nuevo");
        dto.setDescription("Descripción nueva");
        dto.setPrice(200.0);
        dto.setStock(20);
        dto.setCategoryId(2L);
        dto.setSupplierId(5L);

        Product saved = Product.builder()
                .id(id)
                .name("Nombre Nuevo")
                .description("Descripción nueva")
                .price(200.0)
                .stock(10) // Stock no se modifica
                .category(category) // Categoría no cambia (tiene movimientos)
                .supplier(supplier) // Proveedor no cambia (tiene movimientos)
                .build();

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(productRepository.existsByName("Nombre Nuevo")).thenReturn(false);
        when(inventoryMovementRepository.existsByProductId(id)).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        // Act
        Product result = productService.update(id, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Nombre Nuevo", result.getName());
        assertEquals("Descripción nueva", result.getDescription());
        assertEquals(200.0, result.getPrice());
        assertEquals(10, result.getStock()); // Stock no debería cambiar
        assertEquals(category, result.getCategory()); // Categoría no debería cambiar
        assertEquals(supplier, result.getSupplier()); // Proveedor no debería cambiar
        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).existsByName("Nombre Nuevo");
        verify(inventoryMovementRepository, times(1)).existsByProductId(id);
        verify(categoryService, never()).getById(anyLong());
        verify(supplierService, never()).getById(anyLong());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateSameName() {
        // Arrange
        Long id = 1L;
        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder().id(4L).name("Simeflat SA").build();
        Product existing = Product.builder()
                .id(id)
                .name("Botella de agua")
                .description("Descripción antigua")
                .price(100.0)
                .stock(10)
                .category(category)
                .supplier(supplier)
                .build();

        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Botella de agua");
        dto.setDescription("Descripción nueva");
        dto.setPrice(200.0);
        dto.setStock(20);
        dto.setCategoryId(1L);
        dto.setSupplierId(4L);

        Product saved = Product.builder()
                .id(id)
                .name("Botella de agua")
                .description("Descripción nueva")
                .price(200.0)
                .stock(10)
                .category(category)
                .supplier(supplier)
                .build();

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(inventoryMovementRepository.existsByProductId(id)).thenReturn(false);
        when(categoryService.getById(1L)).thenReturn(category);
        when(supplierService.getById(4L)).thenReturn(supplier);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        // Act
        Product result = productService.update(id, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Botella de agua", result.getName());
        assertEquals("Descripción nueva", result.getDescription());
        verify(productRepository, times(1)).findById(id);
        verify(productRepository, never()).existsByName(anyString());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateSameNameDifferentCase() {
        // Arrange
        Long id = 1L;
        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder().id(4L).name("Simeflat SA").build();
        Product existing = Product.builder()
                .id(id)
                .name("botella de agua")
                .description("Descripción antigua")
                .price(100.0)
                .stock(10)
                .category(category)
                .supplier(supplier)
                .build();

        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("BOTELLA DE AGUA");
        dto.setDescription("Descripción nueva");
        dto.setPrice(200.0);
        dto.setStock(20);
        dto.setCategoryId(1L);
        dto.setSupplierId(4L);

        Product saved = Product.builder()
                .id(id)
                .name("BOTELLA DE AGUA")
                .description("Descripción nueva")
                .price(200.0)
                .stock(10)
                .category(category)
                .supplier(supplier)
                .build();

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(inventoryMovementRepository.existsByProductId(id)).thenReturn(false);
        when(categoryService.getById(1L)).thenReturn(category);
        when(supplierService.getById(4L)).thenReturn(supplier);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        // Act
        Product result = productService.update(id, dto);

        // Assert
        assertNotNull(result);
        assertEquals("BOTELLA DE AGUA", result.getName());
        verify(productRepository, times(1)).findById(id);
        verify(productRepository, never()).existsByName(anyString());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateNotFound() {
        // Arrange
        Long id = 999L;
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre Nuevo");
        dto.setDescription("Descripción nueva");
        dto.setPrice(200.0);
        dto.setStock(20);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.update(id, dto)
        );
        assertEquals("No se encontró un producto con ID: " + id, exception.getMessage());
        verify(productRepository, times(1)).findById(id);
        verify(productRepository, never()).existsByName(anyString());
        verify(inventoryMovementRepository, never()).existsByProductId(anyLong());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateDuplicateName() {
        // Arrange
        Long id = 1L;
        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder().id(4L).name("Simeflat SA").build();
        Product existing = Product.builder()
                .id(id)
                .name("Nombre Antiguo")
                .description("Descripción antigua")
                .price(100.0)
                .stock(10)
                .category(category)
                .supplier(supplier)
                .build();

        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre Duplicado");
        dto.setDescription("Descripción nueva");
        dto.setPrice(200.0);
        dto.setStock(20);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(productRepository.existsByName("Nombre Duplicado")).thenReturn(true);

        // Act & Assert
        DuplicateFieldException exception = assertThrows(
                DuplicateFieldException.class,
                () -> productService.update(id, dto)
        );
        assertEquals("Ya existe un producto con el nombre: Nombre Duplicado", exception.getMessage());
        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).existsByName("Nombre Duplicado");
        verify(inventoryMovementRepository, never()).existsByProductId(anyLong());
        verify(productRepository, never()).save(any(Product.class));
    }

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------
    @Test
    void testDeleteSuccess() {
        // Arrange
        Long id = 1L;
        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder().id(4L).name("Simeflat SA").build();
        Product product = Product.builder()
                .id(id)
                .name("Botella de agua")
                .price(2999.99)
                .stock(15)
                .category(category)
                .supplier(supplier)
                .build();

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(inventoryMovementRepository.existsByProductId(id)).thenReturn(false);
        doNothing().when(productRepository).delete(any(Product.class));

        // Act
        productService.delete(id);

        // Assert
        verify(productRepository, times(1)).findById(id);
        verify(inventoryMovementRepository, times(1)).existsByProductId(id);
        verify(productRepository, times(1)).delete(any(Product.class));
    }

    @Test
    void testDeleteNotFound() {
        // Arrange
        Long id = 999L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.delete(id)
        );
        assertEquals("No se encontró un producto con ID: " + id, exception.getMessage());
        verify(productRepository, times(1)).findById(id);
        verify(inventoryMovementRepository, never()).existsByProductId(anyLong());
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void testDeleteHasMovements() {
        // Arrange
        Long id = 1L;
        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder().id(4L).name("Simeflat SA").build();
        Product product = Product.builder()
                .id(id)
                .name("Botella de agua")
                .price(2999.99)
                .stock(15)
                .category(category)
                .supplier(supplier)
                .build();

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(inventoryMovementRepository.existsByProductId(id)).thenReturn(true);

        // Act & Assert
        ProductHasMovementsException exception = assertThrows(
                ProductHasMovementsException.class,
                () -> productService.delete(id)
        );
        assertEquals("No se puede eliminar un producto con movimientos de inventario", exception.getMessage());
        verify(productRepository, times(1)).findById(id);
        verify(inventoryMovementRepository, times(1)).existsByProductId(id);
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void testDeleteWithStockButNoMovements() {
        // Arrange
        Long id = 1L;
        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder().id(4L).name("Simeflat SA").build();
        Product product = Product.builder()
                .id(id)
                .name("Botella de agua")
                .price(2999.99)
                .stock(100) // Tiene stock inicial pero sin movimientos
                .category(category)
                .supplier(supplier)
                .build();

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(inventoryMovementRepository.existsByProductId(id)).thenReturn(false);
        doNothing().when(productRepository).delete(any(Product.class));

        // Act
        productService.delete(id);

        // Assert
        verify(productRepository, times(1)).findById(id);
        verify(inventoryMovementRepository, times(1)).existsByProductId(id);
        verify(productRepository, times(1)).delete(any(Product.class));
    }

}
