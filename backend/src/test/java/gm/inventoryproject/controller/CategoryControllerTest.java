package gm.inventoryproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gm.inventoryproject.dto.category.CategoryRequestDto;
import gm.inventoryproject.dto.category.CategoryResponseDto;
import gm.inventoryproject.exceptions.CategoryHasProductsException;
import gm.inventoryproject.exceptions.DuplicateFieldException;
import gm.inventoryproject.exceptions.ResourceNotFoundException;
import gm.inventoryproject.model.Category;
import gm.inventoryproject.service.ICategoryService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitarios para CategoryController.
 * <p>
 * Verifica los endpoints HTTP utilizando @WebMvcTest y MockMvc.
 * </p>
 */
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ICategoryService categoryService;

    // ------------------------------------------------------
    // CREATE
    // ------------------------------------------------------
    @Test
    void testCreateSuccess() throws Exception {
        // Arrange
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Electrónica");
        requestDto.setDescription("Dispositivos electrónicos");

        Category savedCategory = Category.builder()
                .id(1L)
                .name("Electrónica")
                .description("Dispositivos electrónicos")
                .build();

        when(categoryService.create(any(Category.class))).thenReturn(savedCategory);

        // Act & Assert
        mockMvc.perform(post("/inventory-app/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Electrónica"))
                .andExpect(jsonPath("$.description").value("Dispositivos electrónicos"));
    }

    @Test
    void testCreateValidationError() throws Exception {
        // Arrange
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("A"); // Too short
        requestDto.setDescription("B"); // Too short

        // Act & Assert
        mockMvc.perform(post("/inventory-app/categories")
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
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Electrónica");
        requestDto.setDescription("Dispositivos electrónicos");

        when(categoryService.create(any(Category.class)))
                .thenThrow(new DuplicateFieldException("Ya existe una categoría con ese nombre"));

        // Act & Assert
        mockMvc.perform(post("/inventory-app/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Ya existe una categoría con ese nombre"));
    }

    // ------------------------------------------------------
    // GET ALL
    // ------------------------------------------------------
    @Test
    void testFindAll() throws Exception {
        // Arrange
        Category cat1 = Category.builder().id(1L).name("Electrónica").build();
        Category cat2 = Category.builder().id(2L).name("Alimentos").build();
        List<Category> categories = Arrays.asList(cat1, cat2);

        when(categoryService.getAll()).thenReturn(categories);

        // Act & Assert
        mockMvc.perform(get("/inventory-app/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Electrónica"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Alimentos"));
    }

    // ------------------------------------------------------
    // GET BY ID
    // ------------------------------------------------------
    @Test
    void testFindByIdSuccess() throws Exception {
        // Arrange
        Category category = Category.builder()
                .id(1L)
                .name("Electrónica")
                .description("Dispositivos")
                .build();

        when(categoryService.getById(1L)).thenReturn(category);

        // Act & Assert
        mockMvc.perform(get("/inventory-app/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Electrónica"))
                .andExpect(jsonPath("$.description").value("Dispositivos"));
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        // Arrange
        when(categoryService.getById(999L))
                .thenThrow(new ResourceNotFoundException("Categoría no encontrada con id: 999"));

        // Act & Assert
        mockMvc.perform(get("/inventory-app/categories/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Categoría no encontrada con id: 999"));
    }

    // ------------------------------------------------------
    // SEARCH BY NAME
    // ------------------------------------------------------
    @Test
    void testSearchByName() throws Exception {
        // Arrange
        Category cat1 = Category.builder().id(1L).name("Electrónica").build();
        Category cat2 = Category.builder().id(2L).name("Telelectrodomésticos").build();
        List<Category> categories = Arrays.asList(cat1, cat2);

        when(categoryService.searchByName("lec")).thenReturn(categories);

        // Act & Assert
        mockMvc.perform(get("/inventory-app/categories/search")
                        .param("name", "lec"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electrónica"))
                .andExpect(jsonPath("$[1].name").value("Telelectrodomésticos"));
    }

    // ------------------------------------------------------
    // UPDATE
    // ------------------------------------------------------
    @Test
    void testUpdateSuccess() throws Exception {
        // Arrange
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Nombre Actualizado");
        requestDto.setDescription("Descripción actualizada");

        Category updatedCategory = Category.builder()
                .id(1L)
                .name("Nombre Actualizado")
                .description("Descripción actualizada")
                .build();

        when(categoryService.update(anyLong(), any(Category.class))).thenReturn(updatedCategory);

        // Act & Assert
        mockMvc.perform(put("/inventory-app/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nombre Actualizado"))
                .andExpect(jsonPath("$.description").value("Descripción actualizada"));
    }

    @Test
    void testUpdateValidationError() throws Exception {
        // Arrange
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName(""); // Empty
        requestDto.setDescription("Descripción válida");

        // Act & Assert
        mockMvc.perform(put("/inventory-app/categories/1")
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
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Nombre");
        requestDto.setDescription("Descripción");

        when(categoryService.update(anyLong(), any(Category.class)))
                .thenThrow(new ResourceNotFoundException("Categoría no encontrada con id: 999"));

        // Act & Assert
        mockMvc.perform(put("/inventory-app/categories/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Categoría no encontrada con id: 999"));
    }

    @Test
    void testUpdateDuplicateName() throws Exception {
        // Arrange
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Nombre Duplicado");
        requestDto.setDescription("Descripción");

        when(categoryService.update(anyLong(), any(Category.class)))
                .thenThrow(new DuplicateFieldException("El nombre de categoría ya existe"));

        // Act & Assert
        mockMvc.perform(put("/inventory-app/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("El nombre de categoría ya existe"));
    }

    // ------------------------------------------------------
    // DELETE
    // ------------------------------------------------------
    @Test
    void testDeleteSuccess() throws Exception {
        // Arrange
        doNothing().when(categoryService).delete(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/inventory-app/categories/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteNotFound() throws Exception {
        // Arrange
        doThrow(new ResourceNotFoundException("Categoría no encontrada con id: 999"))
                .when(categoryService).delete(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/inventory-app/categories/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Categoría no encontrada con id: 999"));
    }

    @Test
    void testDeleteHasProducts() throws Exception {
        // Arrange
        doThrow(new CategoryHasProductsException(
                "No se puede eliminar la categoría porque tiene productos asociados"))
                .when(categoryService).delete(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/inventory-app/categories/1"))
                .andExpect(status().isConflict())
                .andExpect(content().string(
                        "No se puede eliminar la categoría porque tiene productos asociados"));
    }
}
