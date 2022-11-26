module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jconsole;
    requires lombok;
    requires java.rmi;
    requires servlet.api;
    requires sip.api;
    requires sip.core;
    requires javafx.media;


    opens com.example.clientGUI to javafx.fxml;
    exports com.example.clientGUI;
    exports com.example.Server;
    exports com.example.clientGUI.video;
    opens com.example.clientGUI.video to javafx.fxml;
}