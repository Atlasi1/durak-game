module com.example.durak {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.durak to javafx.fxml;
    exports com.example.durak;
}