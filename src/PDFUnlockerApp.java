import org.apache.pdfbox.pdmodel.PDDocument;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;

public class PDFUnlockerApp {
    public static void main(String[] args) {
        if (args.length == 0) {
            String message = "PDF Unlocker App\n" +
                    "Desarrollado por Andyholes, 2023\n" +
                    "Arrastra y suelta un archivo PDF en esta ventana de diálogo o en el ícono de la aplicación para desbloquearlo.";
            JOptionPane.showMessageDialog(null, message, "PDF Unlocker App", JOptionPane.INFORMATION_MESSAGE);

            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setVisible(true);

            DropTarget dropTarget = new DropTarget(frame, new DropTargetAdapter() {
                @Override
                public void drop(DropTargetDropEvent event) {
                    event.acceptDrop(DnDConstants.ACTION_COPY);
                    try {
                        Transferable transferable = event.getTransferable();
                        if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                            List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                            processFiles(files);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            frame.setDropTarget(dropTarget);
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

    private static void processFiles(List<File> files) {
        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".pdf")) {
                processPDF(file);
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

            // Save the processed PDF in the same directory as the app
            String outputPath = appDirectory.getAbsolutePath() + File.separator + "unlocked_" + file.getName();
            document.save(outputPath);

            document.close();

            System.out.println("PDF unlocked and saved: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}