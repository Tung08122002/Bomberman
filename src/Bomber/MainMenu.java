package Bomber;

import Bomber.GameFunction.Map;
import Bomber.GameFunction.Sound;
import Bomber.GameFunction.Texture;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainMenu extends Application {
    public static final double fontSize = 36;
    private int chosenLevel;
    private final int numberOfLevel = 5;
    private Game game;
    private Group group;
    private double x, y;

    public void setChosenLevel(int chosenLevel) {
        this.chosenLevel = chosenLevel;
    }

    @Override
    public void start(Stage stage) {
        game = new Game(Texture.IMAGE_SIZE * Map.MAP_WIDTH, Texture.IMAGE_SIZE * (Map.MAP_HEIGHT + 2));

        chosenLevel = 1;
        group = new Group();
        x = game.getWidth();
        y = game.getHeight();
        setBackGround();
        game.setMainMenu(this);

        Text playText = new Text();
        playText.setText("Play");
        playText.setFont(Font.font("Arial", fontSize));
        playText.setX((x - fontSize * 3.2) / 2);
        playText.setY(y / 3);
        playText.setFill(Color.GREEN);

        group.getChildren().add(playText);

        Text levelText = new Text("Level");
        levelText.setFont(Font.font("Arial", fontSize));
        levelText.setX((x - fontSize * 3.2) / 2);
        levelText.setY(y / 3 + 40);
        levelText.setFill(Color.BLUE);

        group.getChildren().add(levelText);

        Text tutorialText = new Text("Tutorial");
        tutorialText.setFont(Font.font("Arial", fontSize));
        tutorialText.setX((x - fontSize * 3.2) / 2);
        tutorialText.setY(y / 3 + 80);
        tutorialText.setFill(Color.YELLOW);

        group.getChildren().add(tutorialText);

        stage.setTitle("Bomberman");
        stage.setResizable(false);
        Scene scene = new Scene(group);
        scene.setFill(Color.LIGHTGRAY);

        stage.setScene(scene);
        game.setScene(scene);
        stage.show();

        playText.setOnMouseClicked(mouseEvent -> {
            playText.setFill(Color.WHITESMOKE);
            group.getChildren().clear();
            group.getChildren().add(game);
            game.start(chosenLevel);
        });

        levelText.setOnMouseClicked(mouseEvent -> {
            levelText.setFill(Color.WHITESMOKE);
            group.getChildren().clear();
            setBackGround();
            List<Text> texts = new ArrayList<>(numberOfLevel);
            for (int i = 0; i < numberOfLevel; i++) {
                Text text = new Text("Level " + (i + 1));
                texts.add(text);
                text.setFont(Font.font("Arial", 20));
                text.setX(20 + 80 * i);
                text.setY(30);
                int finalI = i;
                text.setOnMouseClicked(mouseEvent1 -> {
                    setChosenLevel(finalI + 1);
                    for (Text c : texts) {
                        c.setFill(Color.WHITE);
                    }
                    text.setFill(Color.RED);
                });
            }

            group.getChildren().addAll(texts);
            Text backText = new Text("Back");
            backText.setFont(Font.font("Arial", 20));
            backText.setFill(Color.RED);
            backText.setX(20);
            backText.setY(game.getHeight() - 30);
            group.getChildren().add(backText);
            backText.setOnMouseClicked(mouseEvent1 -> {
                backText.setFill(Color.WHITESMOKE);
                group.getChildren().clear();
                setBackGround();
                group.getChildren().add(levelText);
                levelText.setFill(Color.RED);
                group.getChildren().add(playText);
                group.getChildren().add(tutorialText);
            });
        });

        tutorialText.setOnMouseClicked(mouseEvent -> {
            tutorialText.setFill(Color.WHITESMOKE);
            group.getChildren().clear();

            Text text = new Text("Author: Nguyễn Thanh Tùng - 20020270.\n" +
                    "Dùng phím W A S D hoặc → ← ↑ ↓ để di chuyển." +
                    "\nPhím SPACE để đặt bom.\nPhím V để kiểm tra thông tin người chơi.\n" +
                    "F11 và F12 để tắt bật hack.\nTrong quá trình hack dùng chuột và các số từ 1 đến 9 để sửa map.\n" +
                    "Khi hack ấn Z để đặt bom lớn.");
            text.setFont(Font.font("Arial", 20));
            text.setFill(Color.RED);
            text.setX(20);
            text.setY(game.getHeight() - 510);
            group.getChildren().add(text);
            Text back = new Text("Back");
            back.setFont(Font.font("Arial", 20));
            back.setFill(Color.RED);
            back.setX(20);
            back.setY(game.getHeight() - 30);
            group.getChildren().add(back);
            back.setOnMouseClicked(mouseEvent1 -> {
                back.setFill(Color.WHITESMOKE);
                group.getChildren().clear();
                setBackGround();
                group.getChildren().add(levelText);
                group.getChildren().add(playText);
                group.getChildren().add(tutorialText);
                tutorialText.setFill(Color.RED);

            });
        });

        if (game.isPlayAgain()) {
            drawPlayAgain();
        }
    }

    public void setBackGround() {
        try {
            Image image = new Image(new FileInputStream(Game.textureFolderPath + "mainMenu.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(x);
            imageView.setFitHeight(y);
            group.getChildren().addAll(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVictoryBackGround() {
        try {
            Image image = new Image(new FileInputStream(Game.textureFolderPath + "Victory.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(x);
            imageView.setFitHeight(y);
            group.getChildren().addAll(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nextLevel() {
        if (chosenLevel < numberOfLevel) {
            chosenLevel++;
            System.out.println("Next level called");
            group.getChildren().clear();
            group.getChildren().add(game);
            game.newLevel(chosenLevel);
        }
        else {
            group.getChildren().clear();
            setVictoryBackGround();
        }
    }

    public void drawPlayAgain() {
        int x1, y1;
        x1 = (Game.WIDTH - 2) / 2;
        y1 = (Game.HEIGHT) / 2;
        Rectangle rectangle = new Rectangle(x1 * Texture.IMAGE_SIZE, y1 * Texture.IMAGE_SIZE,
                6 * Texture.IMAGE_SIZE, 3 * Texture.IMAGE_SIZE);
        rectangle.setFill(Color.LIGHTGRAY);
        group.getChildren().add(rectangle);
        Text playAgain = new Text("Play again");
        playAgain.setX((x1) * Texture.IMAGE_SIZE + 50);
        playAgain.setY((y1) * Texture.IMAGE_SIZE + 50);
        playAgain.setFont(new Font("Arial", 20));
        group.getChildren().add(playAgain);

        playAgain.setOnMouseClicked(mouseEvent -> {
            playAgain.setFill(Color.WHITESMOKE);
            group.getChildren().clear();
            group.getChildren().add(game);
            game.newGame(true);
            game.newLevel(chosenLevel);
        });
    }

    public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        Sound.ThemeSound();
        javafx.application.Application.launch(MainMenu.class);
    }
}

