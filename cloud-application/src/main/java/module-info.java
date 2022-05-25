module ru.gb.cloudapplication {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.gb.cloudapplication to javafx.fxml;
    exports ru.gb.cloudapplication;
}