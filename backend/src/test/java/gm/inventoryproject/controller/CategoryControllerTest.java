package gm.inventoryproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gm.inventoryproject.dto.category.CategoryRequestDto;
import gm.inventoryproject.dto.category.CategoryResponseDto;
import gm.inventoryproject.exceptions.CategoryHasProductsException;
import gm.inventoryproject.exceptions.DuplicateFieldException;
import gm.inventoryproject.exceptions.ResourceNotFoundException;
import gm.inventoryproject.model.Category;
import gm.inventoryproject.service.ICategoryService;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("Tests de CategoryController con @WebMvcTest")
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
    @DisplayName("Crear categoría válida retorna 201 Created con categoría creada")
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
    @DisplayName("Crear categoría con datos inválidos retorna 400 Bad Request con errores")
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
    @DisplayName("Crear categoría con nombre duplicado retorna 400 Bad Request con error de duplicado")
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
    @DisplayName("Obtener todas las categorías retorna 200 OK con lista de categorías")
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
    @DisplayName("Obtener categoría por ID existente retorna 200 OK con categoría")
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
    @DisplayName("Obtener categoría por ID inexistente retorna 404 Not Found")
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
    @DisplayName("Buscar categorías por nombre retorna 200 OK con coincidencias")
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
    @DisplayName("Actualizar categoría válida retorna 200 OK con categoría actualizada")
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
    @DisplayName("Actualizar categoría con datos inválidos retorna 400 Bad Request con errores")
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
    @DisplayName("Actualizar categoría inexistente retorna 404 Not Found")
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
    @DisplayName("Actualizar categoría con nombre duplicado retorna 400 Bad Request con error de duplicado")
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
    @DisplayName("Eliminar categoría existente retorna 204 No Content")
    void testDeleteSuccess() throws Exception {
        // Arrange
        doNothing().when(categoryService).delete(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/inventory-app/categories/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Eliminar categoría inexistente retorna 404 Not Found")
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

    /**
     * Verifica que al intentar eliminar una categoría con productos asociados,
     * el controller retorna 409 Conflict con el mensaje de error plano (sin JSON),
     * siguiendo el comportamiento específico del GlobalExceptionHandler para
     * CategoryHasProductsException.
     */
    @Test
    @DisplayName("Eliminar categoría con productos retorna 409 Conflict con mensaje plano")
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
