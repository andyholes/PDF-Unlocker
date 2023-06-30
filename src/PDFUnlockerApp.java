import org.apache.pdfbox.pdmodel.PDDocument;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;

public class PDFUnlockerApp {
    public static void main(String[] args) {
        if (args.length == 0) {
            String message = """
                    PDF Unlocker App
                    Desarrollado por Andyholes, 2023

                    Arrastra y suelta un archivo PDF en esta ventana de diálogo o en el ícono de la aplicación para desbloquearlo.""";
            JOptionPane.showMessageDialog(null, message, "PDF Unlocker App", JOptionPane.INFORMATION_MESSAGE);

            // Enable drag n drop on the dialog box
            new DropTarget(JOptionPane.getRootFrame(), new DropTargetAdapter() {
                @Override
                public void drop(DropTargetDropEvent dtde) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    try {
                        Transferable transferable = dtde.getTransferable();
                        if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                            java.util.List<File> files = (java.util.List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                            for (File file : files) {
                                if (file.getName().endsWith(".pdf")) {
                                    processPDF(file);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            // Process the dragged files
            for (String filePath : args) {
                File file = new File(filePath);
                if (file.getName().endsWith(".pdf")) {
                    processPDF(file);
                }
            }
        }
    }

    private static void processPDF(File file) {
        try {
            PDDocument document = PDDocument.load(file);

            // Unlocking the PDF document
            document.setAllSecurityToBeRemoved(true);

            // Get the app location
            String appPath = PDFUnlockerApp.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            File appDirectory = new File(appPath).getParentFile();

            // Save the processed pdf in the same directory as the app
            String outputPath = appDirectory.getAbsolutePath() + File.separator + "unlocked_" + file.getName();
            document.save(outputPath);

            document.close();

            System.out.println("PDF unlocked and saved: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}