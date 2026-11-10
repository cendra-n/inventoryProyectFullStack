package gm.inventoryproject.controller;

import gm.inventoryproject.service.PdfReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitarios para ReportController.
 * <p>
 * Verifica los endpoints HTTP utilizando @WebMvcTest y MockMvc.
 * </p>
 */
@DisplayName("Tests de ReportController con @WebMvcTest")
@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PdfReportService pdfReportService;

    // ------------------------------------------------------
    // GET /api/reports/products/pdf
    // ------------------------------------------------------
    @Test
    @DisplayName("Descargar PDF de productos retorna 200 OK con Content-Type application/pdf")
    void testDownloadProductListPdfSuccess() throws Exception {
        // Arrange
        byte[] pdfBytes = "%PDF-1.4...".getBytes();
        ByteArrayInputStream pdfStream = new ByteArrayInputStream(pdfBytes);
        when(pdfReportService.generateProductListPdf()).thenReturn(pdfStream);

        // Act & Assert
        mockMvc.perform(get("/api/reports/products/pdf"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/pdf"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=ListaDeProductos.pdf"))
                .andExpect(content().bytes(pdfBytes));
    }

    // ------------------------------------------------------
    // ERROR EN GENERACIÓN DE PDF
    // ------------------------------------------------------
    /**
     * Verifica que cuando el servicio lanza una excepción, el controller
     * captura Exception y devuelve 500 Internal Server Error sin cuerpo.
     */
    @Test
    @DisplayName("Cuando el service lanza excepción retorna 500 Internal Server Error sin cuerpo")
    void testDownloadProductListPdfServiceException() throws Exception {
        // Arrange
        when(pdfReportService.generateProductListPdf())
                .thenThrow(new RuntimeException("Error al generar PDF"));

        // Act & Assert
        mockMvc.perform(get("/api/reports/products/pdf"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().bytes(new byte[0]));
    }
}
