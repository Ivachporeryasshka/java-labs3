package storage;

import exceptions.InvalidFileFormatException;
import geometry2d.Figure;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BinaryFigureStorage implements FigureStorage {

    @Override
    @SuppressWarnings("unchecked")
    public List<Figure> read(Path path) throws IOException, ClassNotFoundException, InvalidFileFormatException {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            Object obj = ois.readObject();
            if (!(obj instanceof List<?> list)) {
                throw new InvalidFileFormatException("Ожидался список (List), получено: " + obj.getClass().getName(), null);
            }

            List<Figure> figures = new ArrayList<>();
            for (Object item : list) {
                if (!(item instanceof Figure figure)) {
                    throw new InvalidFileFormatException("Элемент списка не реализует интерфейс Figure: " + item.getClass().getName(), null);
                }
                figures.add(figure);
            }
            return figures;
        }
    }

    @Override
    public void write(Path path, List<Figure> figures) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(figures);
        }
    }
}