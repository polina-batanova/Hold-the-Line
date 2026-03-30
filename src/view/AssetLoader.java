package view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AssetLoader {
    // Map for storing loaded images
    private Map<String, BufferedImage> sprites = new HashMap<>();

    public AssetLoader() {
        loadSprites();
    }

    // Load images into the map
    private void loadSprites() {
        sprites.put("grass", loadImage("assets/tiles/FieldsTile_01.png"));
        sprites.put("road", loadImage("assets/tiles/FieldsTile_05.png"));
        sprites.put("tower_spot", loadImage("assets/placeholders/PlaceForTower1.png"));
    }

    // Load single image
    private BufferedImage loadImage(String path) {
        // Get the file as an input stream
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Resource not found at: " + path);
                return null;
            }
            // Read and return image
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    // Get loaded image by key
    public BufferedImage getSprite(String key) {
        return sprites.get(key);
    }

}
