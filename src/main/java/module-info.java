module com.vkrui.vkr_ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires choco.solver;
    requires jdk.jdi;


    opens com.vkrui.vkr_ui to javafx.fxml;
    exports com.vkrui.vkr_ui;
}