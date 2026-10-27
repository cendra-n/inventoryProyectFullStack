package gm.inventoryproject.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import gm.inventoryproject.model.Product;
import gm.inventoryproject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Servicio para la generación de reportes en formato PDF.
 * <p>
 * Utiliza la librería iText para generar documentos PDF con listados
 * de productos, incluyendo información de categorías y proveedores.
 * </p>
 */
@Service
public class PdfReportService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Genera un PDF con la lista completa de productos.
     * <p>
     * El PDF incluye el logo de la aplicación, título y una tabla con
     * los datos de productos, categorías y proveedores.
     * </p>
     * @return InputStream con el contenido del PDF generado.
     * @throws Exception si ocurre un error durante la generación del PDF
     *         o si no se encuentra el archivo de logo.
     */
    public ByteArrayInputStream generateProductListPdf() throws Exception {

        Document document = new Document(PageSize.A4.rotate()); // A4 apaisado para más columnas
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // LOGO
        Image logo = Image.getInstance(getClass().getResource("/static/CnInventoryLogo.png"));
        logo.scaleToFit(90, 90);
        logo.setAlignment(Element.ALIGN_CENTER);
        document.add(logo);

        // TÍTULO
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
        Paragraph title = new Paragraph("Lista Completa de Productos", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(25);
        document.add(title);

        // TABLA: 6 COLUMNAS
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

        table.addCell(new PdfPCell(new Phrase("Producto", headFont)));
        table.addCell(new PdfPCell(new Phrase("Stock", headFont)));
        table.addCell(new PdfPCell(new Phrase("Categoría", headFont)));
        table.addCell(new PdfPCell(new Phrase("Proveedor", headFont)));
        table.addCell(new PdfPCell(new Phrase("Email", headFont)));
        table.addCell(new PdfPCell(new Phrase("Teléfono", headFont)));

        // FILAS
        for (Product p : productRepository.findAll()) {

            table.addCell(p.getName());
            table.addCell(String.valueOf(p.getStock()));
            table.addCell(p.getCategory().getName());

            table.addCell(p.getSupplier().getName());
            table.addCell(p.getSupplier().getEmail() != null ? p.getSupplier().getEmail() : "—");
            table.addCell(p.getSupplier().getPhone() != null ? p.getSupplier().getPhone() : "—");
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
