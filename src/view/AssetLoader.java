package view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AssetLoader {
    // Key = image, Value = BufferedImage
    private final Map<String, BufferedImage> spriteCache = new HashMap<>();

    public AssetLoader() {
    }

    public BufferedImage getSprite(String key) {
        if (spriteCache.containsKey(key)) {
            return spriteCache.get(key);
        }

        // Full path
        String fullPath = "assets/" + key + ".png";
        BufferedImage image = loadImage(fullPath);

        if (image != null) {
            spriteCache.put(key, image);
        }
        return image;
    }

    private BufferedImage loadImage(String path) {
        // Try-with-resources which closes InputStream
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Resource not found at path: src/" + path);
                return null;
            }
            // Read image from stream and return it
            return ImageIO.read(is);
        } catch (IOException e) {
            System.err.println("Could not read image at: " + path);
            e.printStackTrace();
            return null;
        }
    }
}