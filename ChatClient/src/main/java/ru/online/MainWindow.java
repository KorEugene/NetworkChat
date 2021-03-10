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

    private static final String LOGIN_SCENE_FXML = "/LoginScene.fxml";
    private static final String LOGIN_TITLE = "Login";
    private static final String PATH_TO_LOGIN_ICON = "/img/login_icon.png";

    private static Stage window;

    private static FXMLLoader mainLoader;

    public static void displayLoginWindow(Stage primaryStage) {

        window = primaryStage;
        window.setOnCloseRequest(e -> {
            e.consume();
            MainWindow.closeProgram();
        });
        window.setTitle(LOGIN_TITLE);

        InputStream loginIconStream = MainWindow.class.getResourceAsStream(PATH_TO_LOGIN_ICON);
        Image chatIcon = new Image(loginIconStream);
        window.getIcons().add(chatIcon);

        FXMLLoader loginLoader = new FXMLLoader(MainWindow.class.getResource(LOGIN_SCENE_FXML));
        double windowWidth = ClientApp.getDefaultScreenWidth() / 6.0;
        double windowHeight = ClientApp.getDefaultScreenHeight() / 7.0;
        Scene loginScene = null;
        try {
            loginScene = new Scene(loginLoader.load(), windowWidth, windowHeight);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        Utility.centerStage(window, windowWidth, windowHeight);
        window.setResizable(false);
        window.setScene(loginScene);
        window.show();
    }

    public static void displayMainWindow(String login) {

        double windowWidth = ClientApp.getDefaultScreenWidth() / 2.0;
        double windowHeight = ClientApp.getDefaultScreenHeight() / 2.0;

//        InputStream chatIconStream = MainWindow.class.getResourceAsStream(PATH_TO_MAIN_ICON);
//        Image chatIcon = new Image(chatIconStream);
//        window.getIcons().removeAll();
//        window.getIcons().add(chatIcon);

//        Platform.runLater(() -> {
//            FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource(MAIN_SCENE_FXML));
//            Scene mainScene = null;
//            try {
//                mainScene = new Scene(loader.load(), windowWidth, windowHeight);
//            } catch (IOException exception) {
//                exception.printStackTrace();
//            }
//            Utility.centerStage(window, windowWidth, windowHeight);
//            window.setScene(mainScene);
//            window.show();
//        });
        FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource(MAIN_SCENE_FXML));
        Scene mainScene = null;
        try {
            mainScene = new Scene(loader.load(), windowWidth, windowHeight);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

//        mainStage.setOnCloseRequest(e -> {
//            e.consume();
//            MainWindow.closeProgram();
//        });

        Utility.centerStage(window, windowWidth, windowHeight);
//        window.setTitle(MAIN_TITLE + String.format(" (Logged in as: %s)", login));
        Scene finalMainScene = mainScene;
        window.setScene(finalMainScene);
        window.show();
//        window.setScene(mainScene);
//        window.show();
    }

    public static void closeProgram() {
        boolean answer = false;
        try {
            answer = ConfirmWindow.display();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        if (answer) {
            Platform.exit();
        }
    }

//    public static void closeProgram() {
//        boolean answer = false;
//        try {
//            answer = ConfirmWindow.display();
//        } catch (IOException exception) {
//            System.out.println(exception.getMessage());
//        }
//        if (answer) {
//            MainSceneController controller = mainLoader.getController();
//            controller.getMessageService().sendMessage("/exit");
//            Platform.exit();
//        }
//    }
}
