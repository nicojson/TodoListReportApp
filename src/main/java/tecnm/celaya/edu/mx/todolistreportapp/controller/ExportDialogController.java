package tecnm.celaya.edu.mx.todolistreportapp.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import tecnm.celaya.edu.mx.todolistreportapp.model.Tarea;
import tecnm.celaya.edu.mx.todolistreportapp.service.PdfExportService;
import tecnm.celaya.edu.mx.todolistreportapp.service.XlsxExportService;

import java.io.File;
import java.io.IOException;

public class ExportDialogController {

    private JFXDialog dialog;
    private ObservableList<Tarea> tasksToExport;
    private StackPane rootPane;
    private PdfExportService pdfExportService;
    private XlsxExportService xlsxExportService;

    public void initData(JFXDialog dialog, ObservableList<Tarea> tasks, StackPane rootPane) {
        this.dialog = dialog;
        this.tasksToExport = tasks;
        this.rootPane = rootPane;
        this.pdfExportService = new PdfExportService();
        this.xlsxExportService = new XlsxExportService();
    }

    @FXML
    private void exportToPdf() {
        dialog.close();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte en PDF");
        fileChooser.setInitialFileName("Reporte_Tareas.pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

        File file = fileChooser.showSaveDialog(rootPane.getScene().getWindow());
        if (file != null) {
            try {
                pdfExportService.export(tasksToExport, file.getAbsolutePath());
                showConfirmationDialog("Exportación Exitosa", "El reporte en PDF se ha guardado correctamente.");
            } catch (IOException e) {
                showErrorDialog("Error de Archivo", "No se pudo guardar el archivo.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void exportToXlsx() {
        dialog.close();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte en XLSX");
        fileChooser.setInitialFileName("Reporte_Tareas.xlsx");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Excel", "*.xlsx"));

        File file = fileChooser.showSaveDialog(rootPane.getScene().getWindow());
        if (file != null) {
            try {
                xlsxExportService.export(tasksToExport, file.getAbsolutePath());
                showConfirmationDialog("Exportación Exitosa", "El reporte en XLSX se ha guardado correctamente.");
            } catch (IOException e) {
                showErrorDialog("Error de Archivo", "No se pudo guardar el archivo.");
                e.printStackTrace();
            }
        }
    }

    private void showConfirmationDialog(String heading, String body) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(heading));
        content.setBody(new Text(body));
        JFXDialog confirmationDialog = new JFXDialog(rootPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("Aceptar");
        button.setOnAction(event -> confirmationDialog.close());
        content.setActions(button);
        confirmationDialog.show();
    }

    private void showErrorDialog(String heading, String body) {
        showConfirmationDialog(heading, body);
    }
}
