import org.apache.pdfbox.pdmodel.PDDocument;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;

public class PDFUnlockerApp {

    public static void main(String[] args) {
        JFrame frame = new JFrame("PDF Unlocker App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JTextArea messageArea = new JTextArea();
        messageArea.setText(
                "Desarrollado por Andres Hoyos Garcia - Junio 2023\n" +
                "andyholesdev@gmail.com | github.com/andyholes\n\n"+
                "Arrastra y suelta un archivo PDF este recuadro para desbloquearlo");
        messageArea.setEditable(false);
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setFocusable(false);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(messageArea, BorderLayout.NORTH);

        DropTarget dropTarget = new DropTarget(contentPanel, new DropTargetAdapter() {
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
        contentPanel.setDropTarget(dropTarget);

        frame.add(contentPanel, BorderLayout.CENTER);
        frame.setVisible(true);
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
