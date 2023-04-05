package Bomber.GameFunction;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Texture {
    public static final int IMAGE_SIZE = 32;
    private Image image;

    public void render(GraphicsContext gc, int x, int y) {
        gc.drawImage(image, x, y);
    }

    public Image getImage() {
        return image;
    }

    public Texture(String path) {
        try {
            image = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
