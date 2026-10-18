package app;

import exceptions.InvalidFileFormatException;
import geometry2d.Figure;
import geometry3d.Cylinder;
import storage.BinaryFigureStorage;
import storage.FigureStorage;
import storage.TextFigureStorage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. Получение путей из аргументов или использование значений по умолчанию
        Path inputPath = args.length > 0 ? Path.of(args[0]) : Path.of("data/input.txt");
        Path outputPath = args.length > 1 ? Path.of(args[1]) : Path.of("data/output.bin");

        System.out.println("Входной файл: " + inputPath.toAbsolutePath());
        System.out.println("Выходной файл: " + outputPath.toAbsolutePath());
        System.out.println("--------------------------------------------------");

        // 2. Чтение списка фигур через текстовую реализацию
        FigureStorage textStorage = new TextFigureStorage();
        List<Figure> figures;
        try {
            figures = textStorage.read(inputPath);
        } catch (IOException | InvalidFileFormatException | ClassNotFoundException e) {
            System.err.println("Критическая ошибка чтения файла: " + e.getMessage());
            return;
        }

        // 3. Вывод информации о каждой фигуре через интерфейс Figure
        System.out.println("Прочитанные фигуры:");
        if (figures.isEmpty()) {
            System.out.println("  (список пуст)");
        }
        for (Figure f : figures) {
            System.out.printf("  %s | Площадь: %.4f | Периметр: %.4f%n", f.toString(), f.area(), f.perimeter());
        }
        System.out.println("--------------------------------------------------");

        // 4. Создание случайного цилиндра
        if (!figures.isEmpty()) {
            Figure randomBase = figures.get(new Random().nextInt(figures.size()));

            // Запрос высоты у пользователя или значение по умолчанию
            double height = 10.0;
            System.out.print("Введите высоту цилиндра (или нажмите Enter для значения по умолчанию 10.0): ");
            try (Scanner scanner = new Scanner(System.in)) {
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) {
                    height = Double.parseDouble(input);
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод, используется высота по умолчанию: 10.0");
            }

            Cylinder cylinder = new Cylinder(randomBase, height);
            System.out.println("Случайный цилиндр создан:");
            System.out.printf("  %s%n", cylinder.toString());
            System.out.printf("  Объём: %.4f%n", cylinder.volume());
        } else {
            System.out.println("Список фигур пуст, создание цилиндра пропущено.");
        }
        System.out.println("--------------------------------------------------");

        // 5. Сохранение списка в бинарный файл через сериализацию
        FigureStorage binaryStorage = new BinaryFigureStorage();
        try {
            binaryStorage.write(outputPath, figures);
            System.out.println("УСПЕХ: Список фигур успешно сохранён в бинарный файл после закрытия ресурса.");
        } catch (IOException e) {
            System.err.println("Ошибка записи бинарного файла: " + e.getMessage());
        }
    }
}