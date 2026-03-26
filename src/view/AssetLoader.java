package view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AssetLoader {
    private Map<String, BufferedImage> sprites = new HashMap<>();

    public AssetLoader() {
        loadSprites();
    }
    private void loadSprites() {
        sprites.put("grass", loadImage("assets/tiles/FieldsTile_01.png"));
        sprites.put("road", loadImage("assets/tiles/FieldsTile_05.png"));
        sprites.put("tower_spot", loadImage("assets/placeholders/PlaceForTower1.png"));
    }

    private BufferedImage loadImage(String path) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Resource not found at: " + path);
                return null;
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    public BufferedImage getSprite(String key) {
        return sprites.get(key);
    }

}
