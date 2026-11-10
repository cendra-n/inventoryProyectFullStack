package gm.inventoryproject.service;

import gm.inventoryproject.model.Category;
import gm.inventoryproject.model.Product;
import gm.inventoryproject.model.Supplier;
import gm.inventoryproject.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para PdfReportService.
 * <p>
 * Verifica la generación de reportes PDF utilizando Mockito para el repositorio.
 * </p>
 */
@DisplayName("Tests de PdfReportService con Mockito")
@ExtendWith(MockitoExtension.class)
class PdfReportServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private PdfReportService pdfReportService;

    // ---------------------------------------------------------
    // GENERAR PDF CON PRODUCTOS
    // ---------------------------------------------------------
    @Test
    @DisplayName("Generar PDF con productos retorna bytes que comienzan con %PDF")
    void testGeneratePdfWithProducts() throws Exception {
        // Arrange
        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder()
                .id(1L)
                .name("Proveedor Test")
                .email("proveedor@test.com")
                .phone("123456789")
                .build();
        Product product = Product.builder()
                .id(1L)
                .name("Botella de agua")
                .stock(15)
                .category(category)
                .supplier(supplier)
                .build();

        List<Product> products = Arrays.asList(product);
        when(productRepository.findAll()).thenReturn(products);

        // Act
        ByteArrayInputStream result = pdfReportService.generateProductListPdf();
        byte[] bytes = result.readAllBytes();

        // Assert
        assertNotNull(result);
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
        assertTrue(new String(bytes, 0, 4).startsWith("%PDF"), "El PDF debe comenzar con %PDF");
        verify(productRepository, times(1)).findAll();
    }

    // ---------------------------------------------------------
    // GENERAR PDF CON LISTA VACÍA
    // ---------------------------------------------------------
    @Test
    @DisplayName("Generar PDF con lista vacía genera PDF válido")
    void testGeneratePdfWithEmptyList() throws Exception {
        // Arrange
        List<Product> emptyList = Collections.emptyList();
        when(productRepository.findAll()).thenReturn(emptyList);

        // Act
        ByteArrayInputStream result = pdfReportService.generateProductListPdf();
        byte[] bytes = result.readAllBytes();

        // Assert
        assertNotNull(result);
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
        assertTrue(new String(bytes, 0, 4).startsWith("%PDF"), "El PDF debe comenzar con %PDF");
        verify(productRepository, times(1)).findAll();
    }

    // ---------------------------------------------------------
    // GENERAR PDF CON PROVEEDOR SIN EMAIL NI TELÉFONO
    // ---------------------------------------------------------
    /**
     * Verifica que el PDF se genera correctamente cuando el proveedor tiene
     * email y teléfono null. El código muestra "—" en esas celdas.
     */
    @Test
    @DisplayName("Generar PDF con proveedor sin email ni teléfono genera PDF sin error")
    void testGeneratePdfWithSupplierWithoutContactInfo() throws Exception {
        // Arrange
        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder()
                .id(1L)
                .name("Proveedor Sin Contacto")
                .email(null)
                .phone(null)
                .build();
        Product product = Product.builder()
                .id(1L)
                .name("Botella de agua")
                .stock(15)
                .category(category)
                .supplier(supplier)
                .build();

        List<Product> products = Arrays.asList(product);
        when(productRepository.findAll()).thenReturn(products);

        // Act
        ByteArrayInputStream result = pdfReportService.generateProductListPdf();
        byte[] bytes = result.readAllBytes();

        // Assert
        assertNotNull(result);
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
        assertTrue(new String(bytes, 0, 4).startsWith("%PDF"), "El PDF debe comenzar con %PDF");
        verify(productRepository, times(1)).findAll();
    }

    // ---------------------------------------------------------
    // VERIFICAR LLAMADA A REPOSITORY
    // ---------------------------------------------------------
    @Test
    @DisplayName("Generar PDF consulta productRepository.findAll exactamente una vez")
    void testGeneratePdfCallsFindAllOnce() throws Exception {
        // Arrange
        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder()
                .id(1L)
                .name("Proveedor Test")
                .email("proveedor@test.com")
                .phone("123456789")
                .build();
        Product product = Product.builder()
                .id(1L)
                .name("Botella de agua")
                .stock(15)
                .category(category)
                .supplier(supplier)
                .build();

        List<Product> products = Arrays.asList(product);
        when(productRepository.findAll()).thenReturn(products);

        // Act
        pdfReportService.generateProductListPdf();

        // Assert
        verify(productRepository, times(1)).findAll();
    }
}
