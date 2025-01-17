package ru.troyanov.transcribtionservice.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PDFileService implements FileService {

    @Value("#{systemProperties['user.dir'] + '${path.to.dir}'}")
    private Path pathToDir;

    @Override
    public File generateFile(String content) {

        File file = pathToDir.resolve(UUID.randomUUID() + ".pdf").toFile();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float margin = 50; // Отступ от края страницы
                float width = page.getMediaBox().getWidth() - 2 * margin; // Ширина доступной области для текста
                float startY = 750; // Начальная позиция по оси Y
                float fontSize = 12; // Размер шрифта
                float leading = 1.5f * fontSize; // Межстрочный интервал
                PDType0Font font = PDType0Font.load(document, new File("src/main/resources/static/font/times-new-roman.ttf"));
                
                List<String> lines = new ArrayList<>();
                int lastSpace = -1;
                while (content.length() > 0) {
                    int spaceIndex = content.indexOf(' ', lastSpace + 1);
                    if (spaceIndex == -1) spaceIndex = content.length();
                    String subString = content.substring(0, spaceIndex);
                    float textWidth = font.getStringWidth(subString) / 1000 * fontSize;
                    if (textWidth > width) {
                        if (lastSpace == -1) lastSpace = spaceIndex;
                        subString = content.substring(0, lastSpace);
                        lines.add(subString);
                        content = content.substring(lastSpace).trim();
                        lastSpace = -1;
                    } else if (spaceIndex == content.length()) {
                        lines.add(content);
                        content = "";
                    } else {
                        lastSpace = spaceIndex;
                    }
                }

                contentStream.beginText();
                contentStream.setFont(PDType0Font.load(document, new File("src/main/resources/static/font/times-new-roman.ttf")), fontSize);
                contentStream.newLineAtOffset(margin, startY);

                for (String line : lines) {
                    contentStream.showText(line);
                    contentStream.newLineAtOffset(0, -leading);
                }

                contentStream.endText();
            }

            document.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
