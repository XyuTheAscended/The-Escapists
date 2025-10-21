module com.escapists {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires freetts;

    opens com.escapists to javafx.fxml;
    exports com.escapists;
}
