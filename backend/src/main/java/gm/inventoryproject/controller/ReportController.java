package gm.inventoryproject.controller;

import gm.inventoryproject.service.PdfReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Descarga de reportes en PDF")
public class ReportController {

    private final PdfReportService pdfReportService;

    public ReportController(PdfReportService pdfReportService) {
        this.pdfReportService = pdfReportService;
    }

    @GetMapping("/products/pdf")
    @Operation(
            summary = "Descargar lista de productos",
            description = "Genera y descarga un archivo PDF con la lista completa de productos, incluyendo stock, categoría y proveedor.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "PDF generado correctamente",
                            content = @Content(mediaType = "application/pdf")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error al generar el PDF",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<byte[]> downloadProductListPdf() {
        try {
            ByteArrayInputStream pdf = pdfReportService.generateProductListPdf();
            byte[] bytes = pdf.readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=ListaDeProductos.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(bytes);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}

