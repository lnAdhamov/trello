package uz.pdp.trello.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class FileUtil {

    public static byte[] readImageAsByteArray(String imagePath) throws IOException {
        Path path = Paths.get(imagePath);
        return Files.readAllBytes(path);
    }

    public static String encodeImageToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }

}
