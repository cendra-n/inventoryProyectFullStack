package gm.inventoryproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gm.inventoryproject.dto.inventorymovement.InventoryMovementRequestDto;
import gm.inventoryproject.dto.inventorymovement.InventoryMovementResponseDto;
import gm.inventoryproject.exceptions.ResourceNotFoundException;
import gm.inventoryproject.model.InventoryMovement;
import gm.inventoryproject.model.Product;
import gm.inventoryproject.service.IInventoryMovementService;
import gm.inventoryproject.service.IProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitarios para InventoryMovementController.
 * <p>
 * Verifica los endpoints HTTP utilizando @WebMvcTest y MockMvc.
 * </p>
 */
@WebMvcTest(InventoryMovementController.class)
class InventoryMovementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IInventoryMovementService movementService;

    @MockBean
    private IProductService productService;

    // ------------------------------------------------------
    // GET ALL
    // ------------------------------------------------------
    @Test
    void testGetAll() throws Exception {
        // Arrange
        Product product = Product.builder().id(3L).name("Botella de agua").build();
        InventoryMovement mov1 = InventoryMovement.builder()
                .id(1L)
                .quantity(10)
                .type(InventoryMovement.MovementType.IN)
                .date(LocalDateTime.of(2026, 11, 9, 10, 30))
                .product(product)
                .build();
        InventoryMovement mov2 = InventoryMovement.builder()
                .id(2L)
                .quantity(5)
                .type(InventoryMovement.MovementType.OUT)
                .date(LocalDateTime.of(2026, 11, 10, 15, 45))
                .product(product)
                .build();
        List<InventoryMovement> movements = Arrays.asList(mov1, mov2);

        when(movementService.getAll()).thenReturn(movements);

        // Act & Assert
        mockMvc.perform(get("/inventory-app/movements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].quantity").value(10))
                .andExpect(jsonPath("$[0].type").value("IN"))
                .andExpect(jsonPath("$[0].productId").value(3))
                .andExpect(jsonPath("$[0].productName").value("Botella de agua"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].quantity").value(5))
                .andExpect(jsonPath("$[1].type").value("OUT"));
    }

    // ------------------------------------------------------
    // GET BY ID
    // ------------------------------------------------------
    @Test
    void testGetByIdSuccess() throws Exception {
        // Arrange
        Product product = Product.builder().id(3L).name("Botella de agua").build();
        InventoryMovement movement = InventoryMovement.builder()
                .id(1L)
                .quantity(10)
                .type(InventoryMovement.MovementType.IN)
                .date(LocalDateTime.of(2026, 11, 9, 10, 30))
                .product(product)
                .build();

        when(movementService.getById(1L)).thenReturn(movement);

        // Act & Assert
        mockMvc.perform(get("/inventory-app/movements/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.type").value("IN"))
                .andExpect(jsonPath("$.productId").value(3))
                .andExpect(jsonPath("$.productName").value("Botella de agua"));
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        // Arrange
        when(movementService.getById(999L))
                .thenThrow(new ResourceNotFoundException("Movement not found with id: 999"));

        // Act & Assert
        mockMvc.perform(get("/inventory-app/movements/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Movement not found with id: 999"));
    }

    // ------------------------------------------------------
    // CREATE
    // ------------------------------------------------------
    @Test
    void testCreateSuccess() throws Exception {
        // Arrange
        InventoryMovementRequestDto requestDto = new InventoryMovementRequestDto();
        requestDto.setQuantity(10);
        requestDto.setType("IN");
        requestDto.setProductId(3L);

        Product product = Product.builder()
                .id(3L)
                .name("Botella de agua")
                .stock(30)
                .build();

        InventoryMovement savedMovement = InventoryMovement.builder()
                .id(1L)
                .quantity(10)
                .type(InventoryMovement.MovementType.IN)
                .date(LocalDateTime.now())
                .product(product)
                .build();

        when(movementService.createFromDto(any(InventoryMovementRequestDto.class))).thenReturn(savedMovement);

        // Act & Assert
        mockMvc.perform(post("/inventory-app/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.type").value("IN"))
                .andExpect(jsonPath("$.productId").value(3))
                .andExpect(jsonPath("$.productName").value("Botella de agua"));
    }

    @Test
    void testCreateValidationError() throws Exception {
        // Arrange
        InventoryMovementRequestDto requestDto = new InventoryMovementRequestDto();
        requestDto.setQuantity(0); // Invalid
        requestDto.setType("INVALID"); // Invalid
        requestDto.setProductId(null); // Invalid

        // Act & Assert
        mockMvc.perform(post("/inventory-app/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.messages").exists());
    }

    @Test
    void testCreateProductNotFound() throws Exception {
        // Arrange
        InventoryMovementRequestDto requestDto = new InventoryMovementRequestDto();
        requestDto.setQuantity(10);
        requestDto.setType("IN");
        requestDto.setProductId(999L);

        when(movementService.createFromDto(any(InventoryMovementRequestDto.class)))
                .thenThrow(new ResourceNotFoundException("Producto no encontrado con id: 999"));

        // Act & Assert
        mockMvc.perform(post("/inventory-app/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Producto no encontrado con id: 999"));
    }

    @Test
    void testCreateInsufficientStock() throws Exception {
        // Arrange
        InventoryMovementRequestDto requestDto = new InventoryMovementRequestDto();
        requestDto.setQuantity(15);
        requestDto.setType("OUT");
        requestDto.setProductId(3L);

        when(movementService.createFromDto(any(InventoryMovementRequestDto.class)))
                .thenThrow(new IllegalArgumentException("Stock insuficiente. Disponible: 10"));

        // Act & Assert
        mockMvc.perform(post("/inventory-app/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Stock insuficiente. Disponible: 10"));
    }

    // ------------------------------------------------------
    // GET BY PRODUCT
    // ------------------------------------------------------
    @Test
    void testGetByProductSuccess() throws Exception {
        // Arrange
        Product product = Product.builder().id(3L).name("Botella de agua").build();
        InventoryMovement mov1 = InventoryMovement.builder()
                .id(1L)
                .quantity(10)
                .type(InventoryMovement.MovementType.IN)
                .product(product)
                .build();
        InventoryMovement mov2 = InventoryMovement.builder()
                .id(2L)
                .quantity(5)
                .type(InventoryMovement.MovementType.OUT)
                .product(product)
                .build();
        List<InventoryMovement> movements = Arrays.asList(mov1, mov2);

        when(movementService.getByProduct(3L)).thenReturn(movements);

        // Act & Assert
        mockMvc.perform(get("/inventory-app/movements/product/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(3))
                .andExpect(jsonPath("$[0].quantity").value(10))
                .andExpect(jsonPath("$[1].productId").value(3))
                .andExpect(jsonPath("$[1].quantity").value(5));
    }

    @Test
    void testGetByProductNotFound() throws Exception {
        // Arrange
        when(movementService.getByProduct(999L))
                .thenThrow(new ResourceNotFoundException("Product not found with id: 999"));

        // Act & Assert
        mockMvc.perform(get("/inventory-app/movements/product/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Product not found with id: 999"));
    }

    // ------------------------------------------------------
    // GET BY DATE RANGE
    // ------------------------------------------------------
    @Test
    void testGetByDateRange() throws Exception {
        // Arrange
        Product product = Product.builder().id(3L).name("Botella de agua").build();
        InventoryMovement mov1 = InventoryMovement.builder()
                .id(1L)
                .quantity(10)
                .type(InventoryMovement.MovementType.IN)
                .date(LocalDateTime.of(2026, 11, 10, 10, 0))
                .product(product)
                .build();
        List<InventoryMovement> movements = Arrays.asList(mov1);

        when(movementService.getByDateRange(any(), any())).thenReturn(movements);

        // Act & Assert
        mockMvc.perform(get("/inventory-app/movements/dates")
                        .param("start", "2026-11-01T00:00:00")
                        .param("end", "2026-11-30T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity").value(10));
    }

    // ------------------------------------------------------
    // GET BY PRODUCT AND DATE
    // ------------------------------------------------------
    @Test
    void testGetByProductAndDate() throws Exception {
        // Arrange
        Product product = Product.builder().id(3L).name("Botella de agua").build();
        InventoryMovement mov1 = InventoryMovement.builder()
                .id(1L)
                .quantity(10)
                .type(InventoryMovement.MovementType.IN)
                .date(LocalDateTime.of(2026, 11, 10, 10, 0))
                .product(product)
                .build();
        List<InventoryMovement> movements = Arrays.asList(mov1);

        when(movementService.getByProductAndDate(anyLong(), any(), any())).thenReturn(movements);

        // Act & Assert
        mockMvc.perform(get("/inventory-app/movements/product/3/dates")
                        .param("start", "2026-11-01T00:00:00")
                        .param("end", "2026-11-30T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(3))
                .andExpect(jsonPath("$[0].quantity").value(10));
    }

}
