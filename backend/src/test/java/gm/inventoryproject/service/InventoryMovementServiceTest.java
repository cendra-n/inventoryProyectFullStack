package gm.inventoryproject.service;

import gm.inventoryproject.dto.inventorymovement.InventoryMovementRequestDto;
import gm.inventoryproject.exceptions.ResourceNotFoundException;
import gm.inventoryproject.model.InventoryMovement;
import gm.inventoryproject.model.Product;
import gm.inventoryproject.repository.InventoryMovementRepository;
import gm.inventoryproject.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para InventoryMovementService.
 * <p>
 * Verifica la lógica de negocio utilizando Mockito para los repositorios.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class InventoryMovementServiceTest {

    @Mock
    private InventoryMovementRepository movementRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private InventoryMovementService movementService;

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------
    @Test
    void testGetAll() {
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
        List<InventoryMovement> expectedList = Arrays.asList(mov1, mov2);
        when(movementRepository.findAll()).thenReturn(expectedList);

        // Act
        List<InventoryMovement> result = movementService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(10, result.get(0).getQuantity());
        assertEquals(5, result.get(1).getQuantity());
        verify(movementRepository, times(1)).findAll();
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------
    @Test
    void testGetByIdSuccess() {
        // Arrange
        Long id = 1L;
        Product product = Product.builder().id(3L).name("Botella de agua").build();
        InventoryMovement expected = InventoryMovement.builder()
                .id(id)
                .quantity(10)
                .type(InventoryMovement.MovementType.IN)
                .date(LocalDateTime.of(2026, 11, 9, 10, 30))
                .product(product)
                .build();
        when(movementRepository.findById(id)).thenReturn(Optional.of(expected));

        // Act
        InventoryMovement result = movementService.getById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(10, result.getQuantity());
        verify(movementRepository, times(1)).findById(id);
    }

    @Test
    void testGetByIdNotFound() {
        // Arrange
        Long id = 999L;
        when(movementRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> movementService.getById(id)
        );
        assertEquals("Movement not found with id: " + id, exception.getMessage());
        verify(movementRepository, times(1)).findById(id);
    }

    // ---------------------------------------------------------
    // CREATE FROM DTO
    // ---------------------------------------------------------
    @Test
    void testCreateFromDtoSuccessIn() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(10);
        dto.setType("IN");
        dto.setProductId(3L);

        Product product = Product.builder()
                .id(3L)
                .name("Botella de agua")
                .stock(20)
                .build();

        InventoryMovement saved = InventoryMovement.builder()
                .id(1L)
                .quantity(10)
                .type(InventoryMovement.MovementType.IN)
                .date(LocalDateTime.now())
                .product(product)
                .build();

        when(productRepository.findById(3L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(movementRepository.save(any(InventoryMovement.class))).thenReturn(saved);

        // Act
        InventoryMovement result = movementService.createFromDto(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(10, result.getQuantity());
        assertEquals(InventoryMovement.MovementType.IN, result.getType());
        assertEquals(30, product.getStock(), "El stock debería haberse incrementado en 10");
        verify(productRepository, times(1)).findById(3L);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(movementRepository, times(1)).save(any(InventoryMovement.class));
    }

    @Test
    void testCreateFromDtoSuccessOut() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(5);
        dto.setType("OUT");
        dto.setProductId(3L);

        Product product = Product.builder()
                .id(3L)
                .name("Botella de agua")
                .stock(20)
                .build();

        InventoryMovement saved = InventoryMovement.builder()
                .id(1L)
                .quantity(5)
                .type(InventoryMovement.MovementType.OUT)
                .date(LocalDateTime.now())
                .product(product)
                .build();

        when(productRepository.findById(3L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(movementRepository.save(any(InventoryMovement.class))).thenReturn(saved);

        // Act
        InventoryMovement result = movementService.createFromDto(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(5, result.getQuantity());
        assertEquals(InventoryMovement.MovementType.OUT, result.getType());
        assertEquals(15, product.getStock(), "El stock debería haberse decrementado en 5");
        verify(productRepository, times(1)).findById(3L);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(movementRepository, times(1)).save(any(InventoryMovement.class));
    }

    @Test
    void testCreateFromDtoQuantityNull() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(null);
        dto.setType("IN");
        dto.setProductId(3L);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> movementService.createFromDto(dto)
        );
        assertEquals("La cantidad debe ser mayor a 0", exception.getMessage());
        verify(productRepository, never()).findById(anyLong());
        verify(productRepository, never()).save(any(Product.class));
        verify(movementRepository, never()).save(any(InventoryMovement.class));
    }

    @Test
    void testCreateFromDtoQuantityZero() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(0);
        dto.setType("IN");
        dto.setProductId(3L);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> movementService.createFromDto(dto)
        );
        assertEquals("La cantidad debe ser mayor a 0", exception.getMessage());
        verify(productRepository, never()).findById(anyLong());
        verify(productRepository, never()).save(any(Product.class));
        verify(movementRepository, never()).save(any(InventoryMovement.class));
    }

    @Test
    void testCreateFromDtoQuantityNegative() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(-5);
        dto.setType("IN");
        dto.setProductId(3L);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> movementService.createFromDto(dto)
        );
        assertEquals("La cantidad debe ser mayor a 0", exception.getMessage());
        verify(productRepository, never()).findById(anyLong());
        verify(productRepository, never()).save(any(Product.class));
        verify(movementRepository, never()).save(any(InventoryMovement.class));
    }

    @Test
    void testCreateFromDtoProductNotFound() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(10);
        dto.setType("IN");
        dto.setProductId(999L);

        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> movementService.createFromDto(dto)
        );
        assertEquals("Producto no encontrado con id: " + 999L, exception.getMessage());
        verify(productRepository, times(1)).findById(999L);
        verify(productRepository, never()).save(any(Product.class));
        verify(movementRepository, never()).save(any(InventoryMovement.class));
    }

    @Test
    void testCreateFromDtoInvalidType() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(10);
        dto.setType("INVALID");
        dto.setProductId(3L);

        Product product = Product.builder().id(3L).name("Botella de agua").stock(20).build();
        when(productRepository.findById(3L)).thenReturn(Optional.of(product));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> movementService.createFromDto(dto)
        );
        assertEquals("Tipo de movimiento inválido. Debe ser IN o OUT", exception.getMessage());
        verify(productRepository, times(1)).findById(3L);
        verify(productRepository, never()).save(any(Product.class));
        verify(movementRepository, never()).save(any(InventoryMovement.class));
    }

    @Test
    void testCreateFromDtoOutWithStockZero() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(5);
        dto.setType("OUT");
        dto.setProductId(3L);

        Product product = Product.builder()
                .id(3L)
                .name("Botella de agua")
                .stock(0)
                .build();

        when(productRepository.findById(3L)).thenReturn(Optional.of(product));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> movementService.createFromDto(dto)
        );
        assertEquals("El producto no tiene stock disponible para realizar una salida", exception.getMessage());
        verify(productRepository, times(1)).findById(3L);
        verify(productRepository, never()).save(any(Product.class));
        verify(movementRepository, never()).save(any(InventoryMovement.class));
    }

    @Test
    void testCreateFromDtoOutWithInsufficientStock() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(15);
        dto.setType("OUT");
        dto.setProductId(3L);

        Product product = Product.builder()
                .id(3L)
                .name("Botella de agua")
                .stock(10)
                .build();

        when(productRepository.findById(3L)).thenReturn(Optional.of(product));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> movementService.createFromDto(dto)
        );
        assertTrue(exception.getMessage().startsWith("Stock insuficiente. Disponible:"));
        verify(productRepository, times(1)).findById(3L);
        verify(productRepository, never()).save(any(Product.class));
        verify(movementRepository, never()).save(any(InventoryMovement.class));
    }

    @Test
    void testCreateFromDtoOutWithExactStock() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(10);
        dto.setType("OUT");
        dto.setProductId(3L);

        Product product = Product.builder()
                .id(3L)
                .name("Botella de agua")
                .stock(10)
                .build();

        InventoryMovement saved = InventoryMovement.builder()
                .id(1L)
                .quantity(10)
                .type(InventoryMovement.MovementType.OUT)
                .date(LocalDateTime.now())
                .product(product)
                .build();

        ArgumentCaptor<InventoryMovement> movementCaptor = ArgumentCaptor.forClass(InventoryMovement.class);

        when(productRepository.findById(3L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(movementRepository.save(any(InventoryMovement.class))).thenReturn(saved);

        // Act
        InventoryMovement result = movementService.createFromDto(dto);

        // Assert
        assertNotNull(result);
        assertEquals(0, product.getStock(), "El stock debería quedar en 0");
        verify(productRepository, times(1)).save(any(Product.class));
        verify(movementRepository, times(1)).save(movementCaptor.capture());

        InventoryMovement capturedMovement = movementCaptor.getValue();
        assertEquals(InventoryMovement.MovementType.OUT, capturedMovement.getType(), "El movimiento guardado debería ser tipo OUT");
        assertEquals(10, capturedMovement.getQuantity(), "El movimiento guardado debería tener cantidad 10");
        assertEquals(3L, capturedMovement.getProduct().getId(), "El movimiento guardado debería tener producto con id 3");
    }

    // ---------------------------------------------------------
    // GET BY PRODUCT
    // ---------------------------------------------------------
    @Test
    void testGetByProductSuccess() {
        // Arrange
        Long productId = 3L;
        Product product = Product.builder().id(productId).name("Botella de agua").build();
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
        List<InventoryMovement> expectedList = Arrays.asList(mov1, mov2);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(movementRepository.findByProduct(product)).thenReturn(expectedList);

        // Act
        List<InventoryMovement> result = movementService.getByProduct(productId);

        // Assert
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findById(productId);
        verify(movementRepository, times(1)).findByProduct(product);
    }

    @Test
    void testGetByProductNotFound() {
        // Arrange
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> movementService.getByProduct(productId)
        );
        assertEquals("Product not found with id: " + productId, exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
        verify(movementRepository, never()).findByProduct(any(Product.class));
    }

    // ---------------------------------------------------------
    // GET BY DATE RANGE
    // ---------------------------------------------------------
    @Test
    void testGetByDateRange() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2026, 11, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 11, 30, 23, 59);
        Product product = Product.builder().id(3L).name("Botella de agua").build();
        InventoryMovement mov1 = InventoryMovement.builder()
                .id(1L)
                .quantity(10)
                .type(InventoryMovement.MovementType.IN)
                .date(LocalDateTime.of(2026, 11, 10, 10, 0))
                .product(product)
                .build();
        InventoryMovement mov2 = InventoryMovement.builder()
                .id(2L)
                .quantity(5)
                .type(InventoryMovement.MovementType.OUT)
                .date(LocalDateTime.of(2026, 11, 15, 15, 0))
                .product(product)
                .build();
        List<InventoryMovement> expectedList = Arrays.asList(mov1, mov2);

        when(movementRepository.findByDateBetween(start, end)).thenReturn(expectedList);

        // Act
        List<InventoryMovement> result = movementService.getByDateRange(start, end);

        // Assert
        assertEquals(2, result.size());
        verify(movementRepository, times(1)).findByDateBetween(start, end);
    }

    // ---------------------------------------------------------
    // GET BY PRODUCT AND DATE
    // ---------------------------------------------------------
    @Test
    void testGetByProductAndDate() {
        // Arrange
        Long productId = 3L;
        LocalDateTime start = LocalDateTime.of(2026, 11, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 11, 30, 23, 59);
        Product product = Product.builder().id(productId).name("Botella de agua").build();
        InventoryMovement mov1 = InventoryMovement.builder()
                .id(1L)
                .quantity(10)
                .type(InventoryMovement.MovementType.IN)
                .date(LocalDateTime.of(2026, 11, 10, 10, 0))
                .product(product)
                .build();
        List<InventoryMovement> expectedList = Arrays.asList(mov1);

        when(movementRepository.findByProductIdAndDateBetween(productId, start, end)).thenReturn(expectedList);

        // Act
        List<InventoryMovement> result = movementService.getByProductAndDate(productId, start, end);

        // Assert
        assertEquals(1, result.size());
        verify(movementRepository, times(1)).findByProductIdAndDateBetween(productId, start, end);
    }

}
