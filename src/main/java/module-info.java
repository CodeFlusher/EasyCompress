module me.codeflusher.easy_compress {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires ffmpeg;

    requires com.google.gson;
    requires java.desktop;
    requires java.sql;

    opens me.codeflusher.easy_compress.data_save to com.google.gson;
    opens me.codeflusher.easy_compress.data to com.google.gson;

    opens me.codeflusher.easy_compress.view to javafx.fxml;
    opens me.codeflusher.easy_compress to javafx.fxml;
    exports me.codeflusher.easy_compress;
}