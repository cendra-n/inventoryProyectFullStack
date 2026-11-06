package gm.inventoryproject.service;

import gm.inventoryproject.exceptions.CategoryHasProductsException;
import gm.inventoryproject.exceptions.DuplicateFieldException;
import gm.inventoryproject.exceptions.ResourceNotFoundException;
import gm.inventoryproject.model.Category;
import gm.inventoryproject.repository.CategoryRepository;
import gm.inventoryproject.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para CategoryService.
 * <p>
 * Verifica la lógica de negocio utilizando Mockito para los repositorios.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CategoryService categoryService;

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------
    @Test
    void testGetAll() {
        // Arrange
        Category cat1 = Category.builder().id(1L).name("Electrónica").build();
        Category cat2 = Category.builder().id(2L).name("Alimentos").build();
        List<Category> expectedList = Arrays.asList(cat1, cat2);
        when(categoryRepository.findAll()).thenReturn(expectedList);

        // Act
        List<Category> result = categoryService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Electrónica", result.get(0).getName());
        assertEquals("Alimentos", result.get(1).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------
    @Test
    void testGetByIdSuccess() {
        // Arrange
        Long id = 1L;
        Category expected = Category.builder().id(id).name("Electrónica").build();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(expected));

        // Act
        Category result = categoryService.getById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Electrónica", result.getName());
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    void testGetByIdNotFound() {
        // Arrange
        Long id = 999L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.getById(id)
        );
        assertEquals("Categoría no encontrada con id: " + id, exception.getMessage());
        verify(categoryRepository, times(1)).findById(id);
    }

    // ---------------------------------------------------------
    // SEARCH BY NAME
    // ---------------------------------------------------------
    @Test
    void testSearchByName() {
        // Arrange
        String searchTerm = "lec";
        Category cat1 = Category.builder().id(1L).name("Electrónica").build();
        Category cat2 = Category.builder().id(2L).name("Telelectrodomésticos").build();
        List<Category> expectedList = Arrays.asList(cat1, cat2);
        when(categoryRepository.findByNameContainingIgnoreCase(searchTerm)).thenReturn(expectedList);

        // Act
        List<Category> result = categoryService.searchByName(searchTerm);

        // Assert
        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findByNameContainingIgnoreCase(searchTerm);
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------
    @Test
    void testCreateSuccess() {
        // Arrange
        Category toCreate = Category.builder()
                .name("Nueva Categoría")
                .description("Descripción de prueba")
                .build();
        Category saved = Category.builder()
                .id(1L)
                .name("Nueva Categoría")
                .description("Descripción de prueba")
                .build();
        when(categoryRepository.existsByName(anyString())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        // Act
        Category result = categoryService.create(toCreate);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Nueva Categoría", result.getName());
        verify(categoryRepository, times(1)).existsByName("Nueva Categoría");
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testCreateDuplicateName() {
        // Arrange
        Category toCreate = Category.builder()
                .name("Electrónica")
                .description("Descripción")
                .build();
        when(categoryRepository.existsByName("Electrónica")).thenReturn(true);

        // Act & Assert
        DuplicateFieldException exception = assertThrows(
                DuplicateFieldException.class,
                () -> categoryService.create(toCreate)
        );
        assertEquals("Ya existe una categoría con ese nombre", exception.getMessage());
        verify(categoryRepository, times(1)).existsByName("Electrónica");
        verify(categoryRepository, never()).save(any(Category.class));
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------
    @Test
    void testUpdateSuccess() {
        // Arrange
        Long id = 1L;
        Category existing = Category.builder()
                .id(id)
                .name("Nombre Antiguo")
                .description("Descripción antigua")
                .build();
        Category updatedData = Category.builder()
                .name("Nombre Nuevo")
                .description("Descripción nueva")
                .build();
        Category saved = Category.builder()
                .id(id)
                .name("Nombre Nuevo")
                .description("Descripción nueva")
                .build();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepository.existsByName("Nombre Nuevo")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        // Act
        Category result = categoryService.update(id, updatedData);

        // Assert
        assertNotNull(result);
        assertEquals("Nombre Nuevo", result.getName());
        assertEquals("Descripción nueva", result.getDescription());
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, times(1)).existsByName("Nombre Nuevo");
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateSameName() {
        // Arrange
        Long id = 1L;
        Category existing = Category.builder()
                .id(id)
                .name("Electrónica")
                .description("Descripción antigua")
                .build();
        Category updatedData = Category.builder()
                .name("Electrónica")
                .description("Descripción nueva")
                .build();
        Category saved = Category.builder()
                .id(id)
                .name("Electrónica")
                .description("Descripción nueva")
                .build();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        // Act
        Category result = categoryService.update(id, updatedData);

        // Assert
        assertNotNull(result);
        assertEquals("Electrónica", result.getName());
        assertEquals("Descripción nueva", result.getDescription());
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, never()).existsByName(anyString());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateSameNameDifferentCase() {
        // Arrange
        Long id = 1L;
        Category existing = Category.builder()
                .id(id)
                .name("electrónica")
                .description("Descripción antigua")
                .build();
        Category updatedData = Category.builder()
                .name("ELECTRÓNICA")
                .description("Descripción nueva")
                .build();
        Category saved = Category.builder()
                .id(id)
                .name("ELECTRÓNICA")
                .description("Descripción nueva")
                .build();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        // Act
        Category result = categoryService.update(id, updatedData);

        // Assert
        assertNotNull(result);
        assertEquals("ELECTRÓNICA", result.getName());
        assertEquals("Descripción nueva", result.getDescription());
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, never()).existsByName(anyString());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateNotFound() {
        // Arrange
        Long id = 999L;
        Category updatedData = Category.builder()
                .name("Nombre Nuevo")
                .description("Descripción nueva")
                .build();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.update(id, updatedData)
        );
        assertEquals("Categoría no encontrada con id: " + id, exception.getMessage());
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, never()).existsByName(anyString());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testUpdateDuplicateName() {
        // Arrange
        Long id = 1L;
        Category existing = Category.builder()
                .id(id)
                .name("Nombre Antiguo")
                .description("Descripción antigua")
                .build();
        Category updatedData = Category.builder()
                .name("Nombre Duplicado")
                .description("Descripción nueva")
                .build();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepository.existsByName("Nombre Duplicado")).thenReturn(true);

        // Act & Assert
        DuplicateFieldException exception = assertThrows(
                DuplicateFieldException.class,
                () -> categoryService.update(id, updatedData)
        );
        assertEquals("El nombre de categoría ya existe", exception.getMessage());
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, times(1)).existsByName("Nombre Duplicado");
        verify(categoryRepository, never()).save(any(Category.class));
    }

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------
    @Test
    void testDeleteSuccess() {
        // Arrange
        Long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(true);
        when(productRepository.existsByCategoryId(id)).thenReturn(false);
        doNothing().when(categoryRepository).deleteById(id);

        // Act
        categoryService.delete(id);

        // Assert
        verify(categoryRepository, times(1)).existsById(id);
        verify(productRepository, times(1)).existsByCategoryId(id);
        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteNotFound() {
        // Arrange
        Long id = 999L;
        when(categoryRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.delete(id)
        );
        assertEquals("Categoría no encontrada con id: " + id, exception.getMessage());
        verify(categoryRepository, times(1)).existsById(id);
        verify(productRepository, never()).existsByCategoryId(anyLong());
        verify(categoryRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteHasProducts() {
        // Arrange
        Long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(true);
        when(productRepository.existsByCategoryId(id)).thenReturn(true);

        // Act & Assert
        CategoryHasProductsException exception = assertThrows(
                CategoryHasProductsException.class,
                () -> categoryService.delete(id)
        );
        assertEquals("No se puede eliminar la categoría porque tiene productos asociados", exception.getMessage());
        verify(categoryRepository, times(1)).existsById(id);
        verify(productRepository, times(1)).existsByCategoryId(id);
        verify(categoryRepository, never()).deleteById(anyLong());
    }
}
