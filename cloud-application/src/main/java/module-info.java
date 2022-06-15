module ru.gb.cloudapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires netty.all.jdk11;
    requires ru.gb.model;

    opens ru.gb.cloudapplication to javafx.fxml;
    exports ru.gb.cloudapplication;
}