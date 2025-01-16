package ru.troyanov.transcribtionservice.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
public class DOCXFileService implements FileService {

    @Value("#{systemProperties['user.dir'] + '${path.to.dir}'}")
    private Path pathToDir;

    @Override
    public File generateFile(String content) {
        LocalDateTime now = LocalDateTime.now();
        File file = pathToDir.resolve(now + ".docx").toFile();

        try (XWPFDocument doc = new XWPFDocument();
             FileOutputStream fos = new FileOutputStream(file);) {
            XWPFParagraph paragraph = doc.createParagraph();
            XWPFRun run = paragraph.createRun();

            run.setText(content);
            run.setBold(true);
            run.setFontFamily("Times New Roman");

            doc.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
