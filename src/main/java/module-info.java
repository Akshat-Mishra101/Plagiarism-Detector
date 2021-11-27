module no.fxplagiarismchecker {
    requires javafx.controls;
    requires javafx.fxml;

    opens no.fxplagiarismchecker to javafx.fxml;
    exports no.fxplagiarismchecker;
}
