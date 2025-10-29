module com.escapists {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires freetts;
    requires junit;

    opens com.escapists to javafx.fxml;
    exports com.escapists;
}
