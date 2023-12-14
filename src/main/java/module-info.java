module com.example.durak_game {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.example.durak_game to javafx.fxml;
    exports com.example.durak_game;
}