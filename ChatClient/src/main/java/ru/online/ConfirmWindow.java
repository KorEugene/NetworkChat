package ru.online;

import ru.online.utility.Utility;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class ConfirmWindow {

    private static final String FXML = "/ConfirmScene.fxml";
    private static final String CONFIRM_TITLE = "Confirmation";
    private static final String PATH_TO_CONFIRM_ICON = "/img/confirm_icon.png";
    private static final int WINDOW_WIDTH = 200;
    private static final int WINDOW_HEIGHT = 80;

    private static boolean answer;

    public static boolean display() throws IOException {

        Stage confirmWindow = new Stage();
        confirmWindow.initModality(Modality.APPLICATION_MODAL);
        confirmWindow.setTitle(CONFIRM_TITLE);

        InputStream chatIconStream = ClientApp.class.getResourceAsStream(PATH_TO_CONFIRM_ICON);
        Image chatIcon = new Image(chatIconStream);
        confirmWindow.getIcons().add(chatIcon);

        FXMLLoader loader = new FXMLLoader(ConfirmWindow.class.getResource(FXML));
        Scene confirmScene = new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);

        Utility.centerStage(confirmWindow, WINDOW_WIDTH, WINDOW_HEIGHT);
        confirmWindow.setScene(confirmScene);
        confirmWindow.setResizable(false);
        confirmWindow.showAndWait();

        return answer;
    }

    public static void setAnswer(boolean answer) {
        ConfirmWindow.answer = answer;
    }
}
