module com.escapists {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires freetts;
    requires junit;
    // requires com.escapists;

    opens com.escapists to javafx.fxml;
    opens com.escapists.Controllers to javafx.fxml;

    exports com.escapists;
}
