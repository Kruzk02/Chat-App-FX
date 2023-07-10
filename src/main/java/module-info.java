module com.chatapp {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;

    exports com.chatapp.Client;
    opens com.chatapp.Client to javafx.fxml;
    exports com.chatapp.Server;
    opens com.chatapp.Server to javafx.fxml;
}