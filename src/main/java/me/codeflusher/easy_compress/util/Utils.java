package me.codeflusher.easy_compress.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import me.codeflusher.easy_compress.MainApp;

import java.io.File;
import java.nio.file.Path;

import static javafx.scene.control.Alert.AlertType;

/**
 * @author Egor (CodeFlusher)
 * @since 5/24/2024 at 12:33 PM</p>
 * For EasyCompress project.
 */
public class Utils {

    public static boolean askYesNoDialog(String title, String question) {
        var alert = new Alert(AlertType.CONFIRMATION, question);
        alert.setTitle(title);
        var result = alert.showAndWait().orElse(ButtonType.CANCEL);
        Logger.log("Utils", "Permission ask result", result);
        return result.equals(ButtonType.OK) || result.equals(ButtonType.YES) || result.equals(ButtonType.APPLY);
    }

    public static ButtonType askChooseDialog(String title, String question, ButtonType... types) {
        var alert = new Alert(AlertType.CONFIRMATION, question, types);
        alert.setTitle(title);
        var result = alert.showAndWait().orElse(ButtonType.CANCEL);
        Logger.log("Utils", "Permission ask result", result);
        return result;
    }

    public static void showUserDialog(String message, AlertType type) {
        new Alert(type, message).show();
    }

    public static File openDialogForDirectory(Window window) {
        Logger.debugLog("FXML Action", "Choosing output folder");
        var fileChooser = new DirectoryChooser();
        return fileChooser.showDialog(window);
    }

    public static File openDialogForFile(Window window) {
        Logger.debugLog("FXML Action", "Choosing output folder");
        var fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(window);
    }

    public static String createPath(String... objects) {
        StringBuilder builder = new StringBuilder();
        for (String object : objects) {
            builder.append(File.separator).append(object);
        }
        return builder.toString();
    }

    public static Path getLocalFile(String name) {
        return Path.of(MainApp.Companion.getCurrentFolder(), name);
    }

}
