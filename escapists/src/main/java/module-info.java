module com.escapists {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.escapists to javafx.fxml;
    exports com.escapists;
}
