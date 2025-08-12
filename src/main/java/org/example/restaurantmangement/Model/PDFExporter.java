package org.example.restaurantmangement.Model;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PDFExporter {

    private static final String userHome = System.getProperty("user.home");
    private static final String downloadsPath = Paths.get(userHome, "Downloads").toString();

    public static void exportPaneToPDF(Pane pane) {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String filePath = "revenue_report_" + now.format(formatter) + ".png";
            filePath = Paths.get(downloadsPath, filePath).toString();
            WritableImage snapshot = pane.snapshot(new SnapshotParameters(), null);
            File file = new File(filePath);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);
            ImageIO.write(bufferedImage, "png", file);
            System.out.println("Ảnh snapshot đã lưu: " + file.getAbsolutePath());

//            PrinterJob job = PrinterJob.createPrinterJob();
//            if (job != null && job.showPrintDialog(pane.getScene().getWindow())) {
//
//
//                PageLayout pageLayout = job.getJobSettings().getPageLayout();
//                double scaleX = pageLayout.getPrintableWidth() / snapshot.getWidth();
//                double scaleY = pageLayout.getPrintableHeight() / snapshot.getHeight();
//                double scale = Math.min(scaleX, scaleY);
//
//
//                Scale newScale = new Scale(scale, scale);
//                pane.getTransforms().add(newScale);
//
//                boolean success = job.printPage(pane);
//                if (success) {
//                    job.endJob();
//                    System.out.println("In thành công");
//                }
//
//                pane.getTransforms().remove(newScale); // Xóa scale sau khi in
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
