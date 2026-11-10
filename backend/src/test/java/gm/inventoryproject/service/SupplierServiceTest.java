package gm.inventoryproject.service;

import gm.inventoryproject.dto.supplier.SupplierRequestDto;
import gm.inventoryproject.exceptions.DuplicateFieldException;
import gm.inventoryproject.exceptions.ResourceNotFoundException;
import gm.inventoryproject.exceptions.SupplierHasProductsException;
import gm.inventoryproject.model.Supplier;
import gm.inventoryproject.repository.ProductRepository;
import gm.inventoryproject.repository.SupplierRepository;
import org.junit.jupiter.api.DisplayName;
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
 * Tests unitarios para SupplierService.
 * <p>
 * Verifica la lógica de negocio utilizando Mockito para los repositorios.
 * </p>
 */
@DisplayName("Tests de SupplierService con Mockito")
@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SupplierService supplierService;

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------
    @Test
    @DisplayName("Obtener todos los proveedores retorna lista completa")
    void testGetAll() {
        // Arrange
        Supplier sup1 = Supplier.builder().id(1L).name("Simeflat SA").build();
        Supplier sup2 = Supplier.builder().id(2L).name("Tech Solutions").build();
        List<Supplier> expectedList = Arrays.asList(sup1, sup2);
        when(supplierRepository.findAll()).thenReturn(expectedList);

        // Act
        List<Supplier> result = supplierService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Simeflat SA", result.get(0).getName());
        assertEquals("Tech Solutions", result.get(1).getName());
        verify(supplierRepository, times(1)).findAll();
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------
    @Test
    @DisplayName("Obtener proveedor por ID existente retorna el proveedor")
    void testGetByIdSuccess() {
        // Arrange
        Long id = 1L;
        Supplier expected = Supplier.builder()
                .id(id)
                .name("Simeflat SA")
                .email("contacto@simeflat.com")
                .phone("1144151093")
                .build();
        when(supplierRepository.findById(id)).thenReturn(Optional.of(expected));

        // Act
        Supplier result = supplierService.getById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Simeflat SA", result.getName());
        verify(supplierRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Obtener proveedor por ID inexistente lanza ResourceNotFoundException")
    void testGetByIdNotFound() {
        // Arrange
        Long id = 999L;
        when(supplierRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> supplierService.getById(id)
        );
        assertEquals("Proveedor no encontrado con id: " + id, exception.getMessage());
        verify(supplierRepository, times(1)).findById(id);
    }

    // ---------------------------------------------------------
    // SEARCH BY NAME
    // ---------------------------------------------------------
    @Test
    @DisplayName("Buscar proveedores por nombre retorna coincidencias parciales")
    void testSearchByName() {
        // Arrange
        String searchTerm = "sime";
        Supplier sup1 = Supplier.builder().id(1L).name("Simeflat SA").build();
        Supplier sup2 = Supplier.builder().id(2L).name("Sime Distribuidora").build();
        List<Supplier> expectedList = Arrays.asList(sup1, sup2);
        when(supplierRepository.findByNameContainingIgnoreCase(searchTerm)).thenReturn(expectedList);

        // Act
        List<Supplier> result = supplierService.searchByName(searchTerm);

        // Assert
        assertEquals(2, result.size());
        verify(supplierRepository, times(1)).findByNameContainingIgnoreCase(searchTerm);
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------
    @Test
    @DisplayName("Crear proveedor con email y teléfono únicos retorna proveedor guardado")
    void testCreateSuccess() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Nuevo Proveedor");
        dto.setEmail("nuevo@proveedor.com");
        dto.setPhone("1155555555");

        Supplier saved = Supplier.builder()
                .id(1L)
                .name("Nuevo Proveedor")
                .email("nuevo@proveedor.com")
                .phone("1155555555")
                .build();

        when(supplierRepository.existsByEmail("nuevo@proveedor.com")).thenReturn(false);
        when(supplierRepository.existsByPhone("1155555555")).thenReturn(false);
        when(supplierRepository.save(any(Supplier.class))).thenReturn(saved);

        // Act
        Supplier result = supplierService.createFromDto(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Nuevo Proveedor", result.getName());
        assertEquals("nuevo@proveedor.com", result.getEmail());
        assertEquals("1155555555", result.getPhone());
        verify(supplierRepository, times(1)).existsByEmail("nuevo@proveedor.com");
        verify(supplierRepository, times(1)).existsByPhone("1155555555");
        verify(supplierRepository, times(1)).save(any(Supplier.class));

        ArgumentCaptor<Supplier> captor = ArgumentCaptor.forClass(Supplier.class);
        verify(supplierRepository).save(captor.capture());
        Supplier captured = captor.getValue();
        assertEquals("Nuevo Proveedor", captured.getName());
        assertEquals("nuevo@proveedor.com", captured.getEmail());
        assertEquals("1155555555", captured.getPhone());
    }

    @Test
    @DisplayName("Crear proveedor con email duplicado lanza DuplicateFieldException")
    void testCreateDuplicateEmail() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Proveedor Test");
        dto.setEmail("existente@email.com");
        dto.setPhone("1155555555");

        when(supplierRepository.existsByEmail("existente@email.com")).thenReturn(true);

        // Act & Assert
        DuplicateFieldException exception = assertThrows(
                DuplicateFieldException.class,
                () -> supplierService.createFromDto(dto)
        );
        assertEquals("El email ya está registrado", exception.getMessage());
        verify(supplierRepository, times(1)).existsByEmail("existente@email.com");
        verify(supplierRepository, never()).existsByPhone(anyString());
        verify(supplierRepository, never()).save(any(Supplier.class));
    }

    @Test
    @DisplayName("Crear proveedor con teléfono duplicado lanza DuplicateFieldException")
    void testCreateDuplicatePhone() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Proveedor Test");
        dto.setEmail("nuevo@email.com");
        dto.setPhone("1144151093");

        when(supplierRepository.existsByEmail("nuevo@email.com")).thenReturn(false);
        when(supplierRepository.existsByPhone("1144151093")).thenReturn(true);

        // Act & Assert
        DuplicateFieldException exception = assertThrows(
                DuplicateFieldException.class,
                () -> supplierService.createFromDto(dto)
        );
        assertEquals("El teléfono ya está registrado", exception.getMessage());
        verify(supplierRepository, times(1)).existsByEmail("nuevo@email.com");
        verify(supplierRepository, times(1)).existsByPhone("1144151093");
        verify(supplierRepository, never()).save(any(Supplier.class));
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------
    @Test
    @DisplayName("Actualizar proveedor con email y teléfono diferentes y únicos retorna proveedor actualizado")
    void testUpdateSuccess() {
        // Arrange
        Long id = 1L;
        Supplier existing = Supplier.builder()
                .id(id)
                .name("Nombre Antiguo")
                .email("antiguo@email.com")
                .phone("1111111111")
                .build();

        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Nombre Nuevo");
        dto.setEmail("nuevo@email.com");
        dto.setPhone("2222222222");

        Supplier saved = Supplier.builder()
                .id(id)
                .name("Nombre Nuevo")
                .email("nuevo@email.com")
                .phone("2222222222")
                .build();

        when(supplierRepository.findById(id)).thenReturn(Optional.of(existing));
        when(supplierRepository.existsByEmail("nuevo@email.com")).thenReturn(false);
        when(supplierRepository.existsByPhone("2222222222")).thenReturn(false);
        when(supplierRepository.save(any(Supplier.class))).thenReturn(saved);

        // Act
        Supplier result = supplierService.updateFromDto(id, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Nombre Nuevo", result.getName());
        assertEquals("nuevo@email.com", result.getEmail());
        assertEquals("2222222222", result.getPhone());
        verify(supplierRepository, times(1)).findById(id);
        verify(supplierRepository, times(1)).existsByEmail("nuevo@email.com");
        verify(supplierRepository, times(1)).existsByPhone("2222222222");
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }

    @Test
    @DisplayName("Actualizar proveedor con el mismo email no verifica duplicado de email")
    void testUpdateSameEmail() {
        // Arrange
        Long id = 1L;
        Supplier existing = Supplier.builder()
                .id(id)
                .name("Simeflat SA")
                .email("contacto@simeflat.com")
                .phone("1144151093")
                .build();

        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA Actualizado");
        dto.setEmail("contacto@simeflat.com");
        dto.setPhone("2222222222");

        Supplier saved = Supplier.builder()
                .id(id)
                .name("Simeflat SA Actualizado")
                .email("contacto@simeflat.com")
                .phone("2222222222")
                .build();

        when(supplierRepository.findById(id)).thenReturn(Optional.of(existing));
        when(supplierRepository.existsByPhone("2222222222")).thenReturn(false);
        when(supplierRepository.save(any(Supplier.class))).thenReturn(saved);

        // Act
        Supplier result = supplierService.updateFromDto(id, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Simeflat SA Actualizado", result.getName());
        assertEquals("contacto@simeflat.com", result.getEmail());
        assertEquals("2222222222", result.getPhone());
        verify(supplierRepository, times(1)).findById(id);
        verify(supplierRepository, never()).existsByEmail(anyString());
        verify(supplierRepository, times(1)).existsByPhone("2222222222");
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }

    /**
     * Verifica que al actualizar un proveedor con el mismo email pero diferente case
     * (ej: "contacto@simeflat.com" -> "CONTACTO@SIMEFLAT.COM"), no se llama a existsByEmail
     * porque se considera el mismo email. Este comportamiento permite cambiar el case sin error.
     */
    @Test
    @DisplayName("Actualizar proveedor con mismo email diferente case no verifica duplicado de email")
    void testUpdateSameEmailDifferentCase() {
        // Arrange
        Long id = 1L;
        Supplier existing = Supplier.builder()
                .id(id)
                .name("Simeflat SA")
                .email("contacto@simeflat.com")
                .phone("1144151093")
                .build();

        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA Actualizado");
        dto.setEmail("CONTACTO@SIMEFLAT.COM");
        dto.setPhone("2222222222");

        Supplier saved = Supplier.builder()
                .id(id)
                .name("Simeflat SA Actualizado")
                .email("CONTACTO@SIMEFLAT.COM")
                .phone("2222222222")
                .build();

        when(supplierRepository.findById(id)).thenReturn(Optional.of(existing));
        when(supplierRepository.existsByPhone("2222222222")).thenReturn(false);
        when(supplierRepository.save(any(Supplier.class))).thenReturn(saved);

        // Act
        Supplier result = supplierService.updateFromDto(id, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Simeflat SA Actualizado", result.getName());
        assertEquals("CONTACTO@SIMEFLAT.COM", result.getEmail());
        verify(supplierRepository, times(1)).findById(id);
        verify(supplierRepository, never()).existsByEmail(anyString());
        verify(supplierRepository, times(1)).existsByPhone("2222222222");
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }

    @Test
    @DisplayName("Actualizar proveedor con el mismo teléfono no verifica duplicado de teléfono")
    void testUpdateSamePhone() {
        // Arrange
        Long id = 1L;
        Supplier existing = Supplier.builder()
                .id(id)
                .name("Simeflat SA")
                .email("contacto@simeflat.com")
                .phone("1144151093")
                .build();

        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA Actualizado");
        dto.setEmail("nuevo@email.com");
        dto.setPhone("1144151093");

        Supplier saved = Supplier.builder()
                .id(id)
                .name("Simeflat SA Actualizado")
                .email("nuevo@email.com")
                .phone("1144151093")
                .build();

        when(supplierRepository.findById(id)).thenReturn(Optional.of(existing));
        when(supplierRepository.existsByEmail("nuevo@email.com")).thenReturn(false);
        when(supplierRepository.save(any(Supplier.class))).thenReturn(saved);

        // Act
        Supplier result = supplierService.updateFromDto(id, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Simeflat SA Actualizado", result.getName());
        assertEquals("nuevo@email.com", result.getEmail());
        assertEquals("1144151093", result.getPhone());
        verify(supplierRepository, times(1)).findById(id);
        verify(supplierRepository, times(1)).existsByEmail("nuevo@email.com");
        verify(supplierRepository, never()).existsByPhone(anyString());
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }

    @Test
    @DisplayName("Actualizar proveedor inexistente lanza ResourceNotFoundException")
    void testUpdateNotFound() {
        // Arrange
        Long id = 999L;
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Nombre Nuevo");
        dto.setEmail("nuevo@email.com");
        dto.setPhone("2222222222");

        when(supplierRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> supplierService.updateFromDto(id, dto)
        );
        assertEquals("Proveedor no encontrado con id: " + id, exception.getMessage());
        verify(supplierRepository, times(1)).findById(id);
        verify(supplierRepository, never()).existsByEmail(anyString());
        verify(supplierRepository, never()).existsByPhone(anyString());
        verify(supplierRepository, never()).save(any(Supplier.class));
    }

    @Test
    @DisplayName("Actualizar proveedor con email duplicado lanza DuplicateFieldException")
    void testUpdateDuplicateEmail() {
        // Arrange
        Long id = 1L;
        Supplier existing = Supplier.builder()
                .id(id)
                .name("Simeflat SA")
                .email("antiguo@email.com")
                .phone("1144151093")
                .build();

        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA Actualizado");
        dto.setEmail("duplicado@email.com");
        dto.setPhone("2222222222");

        when(supplierRepository.findById(id)).thenReturn(Optional.of(existing));
        when(supplierRepository.existsByEmail("duplicado@email.com")).thenReturn(true);

        // Act & Assert
        DuplicateFieldException exception = assertThrows(
                DuplicateFieldException.class,
                () -> supplierService.updateFromDto(id, dto)
        );
        assertEquals("El email ya está registrado", exception.getMessage());
        verify(supplierRepository, times(1)).findById(id);
        verify(supplierRepository, times(1)).existsByEmail("duplicado@email.com");
        verify(supplierRepository, never()).existsByPhone(anyString());
        verify(supplierRepository, never()).save(any(Supplier.class));
    }

    @Test
    @DisplayName("Actualizar proveedor con teléfono duplicado lanza DuplicateFieldException")
    void testUpdateDuplicatePhone() {
        // Arrange
        Long id = 1L;
        Supplier existing = Supplier.builder()
                .id(id)
                .name("Simeflat SA")
                .email("antiguo@email.com")
                .phone("1144151093")
                .build();

        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA Actualizado");
        dto.setEmail("nuevo@email.com");
        dto.setPhone("duplicado");

        when(supplierRepository.findById(id)).thenReturn(Optional.of(existing));
        when(supplierRepository.existsByEmail("nuevo@email.com")).thenReturn(false);
        when(supplierRepository.existsByPhone("duplicado")).thenReturn(true);

        // Act & Assert
        DuplicateFieldException exception = assertThrows(
                DuplicateFieldException.class,
                () -> supplierService.updateFromDto(id, dto)
        );
        assertEquals("El teléfono ya está registrado", exception.getMessage());
        verify(supplierRepository, times(1)).findById(id);
        verify(supplierRepository, times(1)).existsByEmail("nuevo@email.com");
        verify(supplierRepository, times(1)).existsByPhone("duplicado");
        verify(supplierRepository, never()).save(any(Supplier.class));
    }

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------
    @Test
    @DisplayName("Eliminar proveedor existente sin productos elimina el proveedor")
    void testDeleteSuccess() {
        // Arrange
        Long id = 1L;
        when(supplierRepository.existsById(id)).thenReturn(true);
        when(productRepository.existsBySupplierId(id)).thenReturn(false);
        doNothing().when(supplierRepository).deleteById(id);

        // Act
        supplierService.delete(id);

        // Assert
        verify(supplierRepository, times(1)).existsById(id);
        verify(productRepository, times(1)).existsBySupplierId(id);
        verify(supplierRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Eliminar proveedor inexistente lanza ResourceNotFoundException")
    void testDeleteNotFound() {
        // Arrange
        Long id = 999L;
        when(supplierRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> supplierService.delete(id)
        );
        assertEquals("Proveedor no encontrado con id: " + id, exception.getMessage());
        verify(supplierRepository, times(1)).existsById(id);
        verify(productRepository, never()).existsBySupplierId(anyLong());
        verify(supplierRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Eliminar proveedor con productos asociados lanza SupplierHasProductsException")
    void testDeleteHasProducts() {
        // Arrange
        Long id = 1L;
        when(supplierRepository.existsById(id)).thenReturn(true);
        when(productRepository.existsBySupplierId(id)).thenReturn(true);

        // Act & Assert
        SupplierHasProductsException exception = assertThrows(
                SupplierHasProductsException.class,
                () -> supplierService.delete(id)
        );
        assertEquals("No se puede eliminar el proveedor porque tiene productos asociados", exception.getMessage());
        verify(supplierRepository, times(1)).existsById(id);
        verify(productRepository, times(1)).existsBySupplierId(id);
        verify(supplierRepository, never()).deleteById(anyLong());
    }

}
