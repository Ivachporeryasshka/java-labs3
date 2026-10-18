package storage;

import exceptions.InvalidFileFormatException;
import geometry2d.Circle;
import geometry2d.Figure;
import geometry2d.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TextFigureStorage implements FigureStorage {

    @Override
    public List<Figure> read(Path path) throws IOException, InvalidFileFormatException {
        List<Figure> figures = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    lineNumber++;
                    continue;
                }
                try {
                    figures.add(parseLine(line));
                } catch (InvalidFileFormatException e) {
                    // Поведение при ошибке: пропускаем строку с сообщением, уже прочитанные фигуры сохраняются в списке
                    System.err.println("Предупреждение: строка " + lineNumber + " пропущена. " + e.getMessage());
                    if (e.getCause() != null) {
                        System.err.println("  Причина: " + e.getCause().getMessage());
                    }
                }
                lineNumber++;
            }
        }
        return figures;
    }

    private Figure parseLine(String line) throws InvalidFileFormatException {
        String[] parts = line.split("\\s+");
        try {
            if (parts[0].equalsIgnoreCase("CIRCLE") && parts.length == 2) {
                return new Circle(Double.parseDouble(parts[1]));
            } else if (parts[0].equalsIgnoreCase("RECTANGLE") && parts.length == 3) {
                return new Rectangle(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
            } else {
                throw new IllegalArgumentException("Неизвестный тип фигуры или неверное количество параметров");
            }
        } catch (IllegalArgumentException e) {
            throw new InvalidFileFormatException("Некорректный формат данных", e);
        }
    }

    @Override
    public void write(Path path, List<Figure> figures) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (Figure figure : figures) {
                if (figure instanceof Circle c) {
                    writer.write(String.format("CIRCLE %.6f", c.getRadius()));
                } else if (figure instanceof Rectangle r) {
                    writer.write(String.format("RECTANGLE %.6f %.6f", r.getWidth(), r.getHeight()));
                }
                writer.newLine();
            }
        }
    }
}