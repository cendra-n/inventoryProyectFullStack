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

@Service
public class PdfReportService {

    @Autowired
    private ProductRepository productRepository;

    public ByteArrayInputStream generateProductListPdf() throws Exception {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // LOGO
        Image logo = Image.getInstance(getClass().getResource("/static/CnInventorylogo.png"));
        logo.scaleToFit(100, 100);
        logo.setAlignment(Element.ALIGN_CENTER);
        document.add(logo);

        // TÍTULO
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
        Paragraph title = new Paragraph("Lista de Productos", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // TABLA
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        PdfPCell h1 = new PdfPCell(new Phrase("Producto", headFont));
        PdfPCell h2 = new PdfPCell(new Phrase("Stock", headFont));
        PdfPCell h3 = new PdfPCell(new Phrase("Categoría", headFont));
        PdfPCell h4 = new PdfPCell(new Phrase("Proveedor", headFont));

        table.addCell(h1);
        table.addCell(h2);
        table.addCell(h3);
        table.addCell(h4);

        for (Product p : productRepository.findAll()) {
            table.addCell(p.getName());
            table.addCell(String.valueOf(p.getStock()));
            table.addCell(p.getCategory().getName());
            table.addCell(p.getSupplier().getName());
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}

