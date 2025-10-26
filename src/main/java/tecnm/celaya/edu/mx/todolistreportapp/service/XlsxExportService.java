package tecnm.celaya.edu.mx.todolistreportapp.service;

import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tecnm.celaya.edu.mx.todolistreportapp.model.Tarea;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Servicio encargado de la lógica para exportar tareas a un archivo XLSX (Excel).
 */
public class XlsxExportService {

    public void export(ObservableList<Tarea> tasks, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Tareas");

        // Crear un estilo para las celdas de la cabecera
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Crear la fila de la cabecera
        String[] headers = {"Nombre", "Descripción", "Estatus", "Categorías", "Fecha Vencimiento"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Llenar las filas con los datos de las tareas
        int rowNum = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Tarea task : tasks) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(task.getNombre());
            row.createCell(1).setCellValue(task.getDescripcion());
            row.createCell(2).setCellValue(task.getEstatus());
            row.createCell(3).setCellValue(task.getNombreCategoria());
            if (task.getFechaVencimiento() != null) {
                row.createCell(4).setCellValue(task.getFechaVencimiento().format(formatter));
            } else {
                row.createCell(4).setCellValue("");
            }
        }

        // Autoajustar el ancho de las columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Escribir el archivo de salida
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        workbook.close();
        System.out.println("Reporte en XLSX generado exitosamente en: " + filePath);
    }
}
