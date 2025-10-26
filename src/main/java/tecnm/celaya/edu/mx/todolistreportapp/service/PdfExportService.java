package tecnm.celaya.edu.mx.todolistreportapp.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.collections.ObservableList;
import tecnm.celaya.edu.mx.todolistreportapp.model.Tarea;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

/**
 * Servicio encargado de la lógica para exportar tareas a un archivo PDF.
 */
public class PdfExportService {

    /**
     * Exporta una lista de tareas a un archivo PDF.
     *
     * @param tasks La lista de tareas a exportar.
     * @param filePath La ruta del archivo donde se guardará el PDF.
     * @throws FileNotFoundException Si la ruta del archivo no es válida.
     */
    public void export(ObservableList<Tarea> tasks, String filePath) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Título del documento
        document.add(new Paragraph("Reporte de Tareas")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(20)
                .setMarginBottom(20));

        // Crear la tabla
        Table table = new Table(UnitValue.createPercentArray(new float[]{3, 4, 2, 2, 2}));
        table.setWidth(UnitValue.createPercentValue(100));

        // Añadir las cabeceras de la tabla
        addTableHeader(table);

        // Añadir las filas con los datos de las tareas
        for (Tarea task : tasks) {
            addTableRow(table, task);
        }

        document.add(table);
        document.close();

        System.out.println("Reporte en PDF generado exitosamente en: " + filePath);
    }

    private void addTableHeader(Table table) {
        table.addHeaderCell(new Cell().add(new Paragraph("Nombre").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Descripción").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Estatus").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Categorías").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Fecha Vencimiento").setBold()));
    }

    private void addTableRow(Table table, Tarea task) {
        table.addCell(new Cell().add(new Paragraph(task.getNombre() != null ? task.getNombre() : "")));
        table.addCell(new Cell().add(new Paragraph(task.getDescripcion() != null ? task.getDescripcion() : "")));
        table.addCell(new Cell().add(new Paragraph(task.getEstatus() != null ? task.getEstatus() : "")));
        table.addCell(new Cell().add(new Paragraph(task.getNombreCategoria() != null ? task.getNombreCategoria() : "")));
        
        String fechaVencimiento = "";
        if (task.getFechaVencimiento() != null) {
            fechaVencimiento = task.getFechaVencimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        table.addCell(new Cell().add(new Paragraph(fechaVencimiento)));
    }
}
