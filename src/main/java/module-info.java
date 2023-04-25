module com.ignotus {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ignotus.GUI to javafx.fxml;
    exports com.ignotus.GUI;
}