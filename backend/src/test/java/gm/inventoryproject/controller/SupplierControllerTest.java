package gm.inventoryproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gm.inventoryproject.dto.supplier.SupplierRequestDto;
import gm.inventoryproject.dto.supplier.SupplierResponseDto;
import gm.inventoryproject.exceptions.DuplicateFieldException;
import gm.inventoryproject.exceptions.ResourceNotFoundException;
import gm.inventoryproject.exceptions.SupplierHasProductsException;
import gm.inventoryproject.model.Supplier;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitarios para SupplierController.
 * <p>
 * Verifica los endpoints HTTP utilizando @WebMvcTest y MockMvc.
 * </p>
 */
@WebMvcTest(SupplierController.class)
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ISupplierService supplierService;

    // ------------------------------------------------------
    // CREATE
    // ------------------------------------------------------
    @Test
    void testCreateSuccess() throws Exception {
        // Arrange
        SupplierRequestDto requestDto = new SupplierRequestDto();
        requestDto.setName("Simeflat SA");
        requestDto.setEmail("contacto@simeflat.com");
        requestDto.setPhone("1144151093");

        Supplier savedSupplier = Supplier.builder()
                .id(1L)
                .name("Simeflat SA")
                .email("contacto@simeflat.com")
                .phone("1144151093")
                .build();

        when(supplierService.createFromDto(any(SupplierRequestDto.class))).thenReturn(savedSupplier);

        // Act & Assert
        mockMvc.perform(post("/inventory-app/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Simeflat SA"))
                .andExpect(jsonPath("$.email").value("contacto@simeflat.com"))
                .andExpect(jsonPath("$.phone").value("1144151093"));
    }

    @Test
    void testCreateValidationError() throws Exception {
        // Arrange
        SupplierRequestDto requestDto = new SupplierRequestDto();
        requestDto.setName("A"); // Too short
        requestDto.setEmail("invalid"); // Invalid email
        requestDto.setPhone("123"); // Too short

        // Act & Assert
        mockMvc.perform(post("/inventory-app/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.messages").exists());
    }

    @Test
    void testCreateDuplicateEmail() throws Exception {
        // Arrange
        SupplierRequestDto requestDto = new SupplierRequestDto();
        requestDto.setName("Simeflat SA");
        requestDto.setEmail("contacto@simeflat.com");
        requestDto.setPhone("1144151093");

        when(supplierService.createFromDto(any(SupplierRequestDto.class)))
                .thenThrow(new DuplicateFieldException("El email ya está registrado"));

        // Act & Assert
        mockMvc.perform(post("/inventory-app/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("El email ya está registrado"));
    }

    // ------------------------------------------------------
    // GET ALL
    // ------------------------------------------------------
    @Test
    void testFindAll() throws Exception {
        // Arrange
        Supplier sup1 = Supplier.builder().id(1L).name("Simeflat SA").build();
        Supplier sup2 = Supplier.builder().id(2L).name("Tech Solutions").build();
        List<Supplier> suppliers = Arrays.asList(sup1, sup2);

        when(supplierService.getAll()).thenReturn(suppliers);

        // Act & Assert
        mockMvc.perform(get("/inventory-app/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Simeflat SA"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Tech Solutions"));
    }

    // ------------------------------------------------------
    // GET BY ID
    // ------------------------------------------------------
    @Test
    void testFindByIdSuccess() throws Exception {
        // Arrange
        Supplier supplier = Supplier.builder()
                .id(1L)
                .name("Simeflat SA")
                .email("contacto@simeflat.com")
                .phone("1144151093")
                .build();

        when(supplierService.getById(1L)).thenReturn(supplier);

        // Act & Assert
        mockMvc.perform(get("/inventory-app/suppliers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Simeflat SA"))
                .andExpect(jsonPath("$.email").value("contacto@simeflat.com"))
                .andExpect(jsonPath("$.phone").value("1144151093"));
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        // Arrange
        when(supplierService.getById(999L))
                .thenThrow(new ResourceNotFoundException("Proveedor no encontrado con id: 999"));

        // Act & Assert
        mockMvc.perform(get("/inventory-app/suppliers/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Proveedor no encontrado con id: 999"));
    }

    // ------------------------------------------------------
    // SEARCH BY NAME
    // ------------------------------------------------------
    @Test
    void testSearchByName() throws Exception {
        // Arrange
        Supplier sup1 = Supplier.builder().id(1L).name("Simeflat SA").build();
        Supplier sup2 = Supplier.builder().id(2L).name("Sime Distribuidora").build();
        List<Supplier> suppliers = Arrays.asList(sup1, sup2);

        when(supplierService.searchByName("sime")).thenReturn(suppliers);

        // Act & Assert
        mockMvc.perform(get("/inventory-app/suppliers/search")
                        .param("name", "sime"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Simeflat SA"))
                .andExpect(jsonPath("$[1].name").value("Sime Distribuidora"));
    }

    // ------------------------------------------------------
    // UPDATE
    // ------------------------------------------------------
    @Test
    void testUpdateSuccess() throws Exception {
        // Arrange
        SupplierRequestDto requestDto = new SupplierRequestDto();
        requestDto.setName("Simeflat SA Actualizado");
        requestDto.setEmail("nuevo@simeflat.com");
        requestDto.setPhone("2222222222");

        Supplier updatedSupplier = Supplier.builder()
                .id(1L)
                .name("Simeflat SA Actualizado")
                .email("nuevo@simeflat.com")
                .phone("2222222222")
                .build();

        when(supplierService.updateFromDto(anyLong(), any(SupplierRequestDto.class))).thenReturn(updatedSupplier);

        // Act & Assert
        mockMvc.perform(put("/inventory-app/suppliers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Simeflat SA Actualizado"))
                .andExpect(jsonPath("$.email").value("nuevo@simeflat.com"))
                .andExpect(jsonPath("$.phone").value("2222222222"));
    }

    @Test
    void testUpdateValidationError() throws Exception {
        // Arrange
        SupplierRequestDto requestDto = new SupplierRequestDto();
        requestDto.setName(""); // Empty
        requestDto.setEmail("valid@email.com");
        requestDto.setPhone("1144151093");

        // Act & Assert
        mockMvc.perform(put("/inventory-app/suppliers/1")
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
        SupplierRequestDto requestDto = new SupplierRequestDto();
        requestDto.setName("Nombre");
        requestDto.setEmail("valid@email.com");
        requestDto.setPhone("1144151093");

        when(supplierService.updateFromDto(anyLong(), any(SupplierRequestDto.class)))
                .thenThrow(new ResourceNotFoundException("Proveedor no encontrado con id: 999"));

        // Act & Assert
        mockMvc.perform(put("/inventory-app/suppliers/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Proveedor no encontrado con id: 999"));
    }

    @Test
    void testUpdateDuplicateEmail() throws Exception {
        // Arrange
        SupplierRequestDto requestDto = new SupplierRequestDto();
        requestDto.setName("Nombre");
        requestDto.setEmail("duplicado@email.com");
        requestDto.setPhone("1144151093");

        when(supplierService.updateFromDto(anyLong(), any(SupplierRequestDto.class)))
                .thenThrow(new DuplicateFieldException("El email ya está registrado"));

        // Act & Assert
        mockMvc.perform(put("/inventory-app/suppliers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("El email ya está registrado"));
    }

    // ------------------------------------------------------
    // DELETE
    // ------------------------------------------------------
    @Test
    void testDeleteSuccess() throws Exception {
        // Arrange
        doNothing().when(supplierService).delete(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/inventory-app/suppliers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteNotFound() throws Exception {
        // Arrange
        doThrow(new ResourceNotFoundException("Proveedor no encontrado con id: 999"))
                .when(supplierService).delete(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/inventory-app/suppliers/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Proveedor no encontrado con id: 999"));
    }

    @Test
    void testDeleteHasProducts() throws Exception {
        // Arrange
        doThrow(new SupplierHasProductsException(
                "No se puede eliminar el proveedor porque tiene productos asociados"))
                .when(supplierService).delete(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/inventory-app/suppliers/1"))
                .andExpect(status().isConflict())
                .andExpect(content().string(
                        "No se puede eliminar el proveedor porque tiene productos asociados"));
    }

}
