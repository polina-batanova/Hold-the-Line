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
        sprites.put("grass", loadImage("/assets/tiles/FieldTile_01.png"));
        sprites.put("road", loadImage("/assets/tiles/FieldTile_05.png"));
        sprites.put("tower_spot", loadImage("/assets/placeholders/PlaceForTower1.png"));
    }

    public BufferedImage getSprite(String key) {
        return null;
    }

    private BufferedImage loadImage(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) throw new IOException("Resource not found: " + path);
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
