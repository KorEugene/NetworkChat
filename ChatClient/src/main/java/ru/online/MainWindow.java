package ru.online;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.online.controllers.MainSceneController;
import ru.online.utility.Utility;

import java.io.IOException;
import java.io.InputStream;

public class MainWindow {

    private static final String MAIN_SCENE_FXML = "/MainScene.fxml";
    private static final String MAIN_TITLE = "Just Java Chat";
    private static final String PATH_TO_MAIN_ICON = "/img/chat_icon.png";

    private static Stage mainWindow;
    private static FXMLLoader mainLoader;

    public static void displayMainWindow(String login) throws IOException {

        int defScreenWidth = ClientApp.getDefaultScreenWidth();
        int defScreenHeight = ClientApp.getDefaultScreenHeight();

        mainWindow = new Stage();
        double windowWidth = defScreenWidth / 2.0;
        double windowHeight = defScreenHeight / 2.0;

        InputStream chatIconStream = MainWindow.class.getResourceAsStream(PATH_TO_MAIN_ICON);
        Image chatIcon = new Image(chatIconStream);
        mainWindow.getIcons().add(chatIcon);

        mainLoader = new FXMLLoader(MainWindow.class.getResource(MAIN_SCENE_FXML));
        Scene mainScene = new Scene(mainLoader.load(), windowWidth, windowHeight);

        mainWindow.setOnCloseRequest(e -> {
            e.consume();
            MainWindow.closeProgram();
        });

        Utility.centerStage(mainWindow, windowWidth, windowHeight);
        mainWindow.setTitle(MAIN_TITLE + String.format(" (Logged in as: %s)", login));
        mainWindow.setScene(mainScene);
        mainWindow.show();
    }

    public static void closeProgram() {
        boolean answer = false;
        try {
            answer = ConfirmWindow.display();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        if (answer) {
            MainSceneController controller = mainLoader.getController();
            controller.getMessageService().sendMessage("/exit");
            Platform.exit();
        }
    }

    public static Stage getMainWindow() {
        return mainWindow;
    }
}
