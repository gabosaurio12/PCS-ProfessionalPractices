package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public class DocsUtil {
    private static final Logger logger = LogManager.getLogger(DocsUtil.class);

    public void fillDocx(String templatePath, String outputPath, Map<String, String> fillers) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(templatePath);
        XWPFDocument doc = new XWPFDocument(fileInputStream);

        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            StringBuilder fullText = new StringBuilder();
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                if (text != null) {
                    fullText.append(text);
                }
            }
            String replacedText = fullText.toString();
            for (Map.Entry<String, String> entry : fillers.entrySet()) {
                replacedText = replacedText.replace("${" + entry.getKey() + "}", entry.getValue());
            }

            if (!replacedText.contentEquals(fullText)) {
                for (int i = paragraph.getRuns().size() - 1; i >= 0; i--) {
                    paragraph.removeRun(i);
                }
                XWPFRun run = paragraph.createRun();
                run.setText(replacedText);
            }

        }
        FileOutputStream out = new FileOutputStream(outputPath);
        doc.write(out);
        out.close();
        doc.close();
    }

    public void copyDocx(String originPath, String destinationPath) throws IOException {
        File destinationFile = new File(destinationPath);
        if (!destinationFile.exists()) {
            destinationFile.getParentFile().mkdirs();
        }
        Path origin = Paths.get(originPath);
        Path destination = Paths.get(destinationPath);
        Files.copy(origin, destination, StandardCopyOption.REPLACE_EXISTING);
    }
}
