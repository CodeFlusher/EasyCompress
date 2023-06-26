module me.urbanfaust.easy_compress {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires ffmpeg;

    requires com.google.gson;
    requires java.desktop;

    opens me.urbanfaust.easy_compress.data_save to com.google.gson;
    opens me.urbanfaust.easy_compress.data to com.google.gson;

    opens me.urbanfaust.easy_compress.view to javafx.fxml;
    opens me.urbanfaust.easy_compress to javafx.fxml;
    exports me.urbanfaust.easy_compress;
}