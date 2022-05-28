package ru.gb.cloudapplication;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML
    public VBox leftTable;
    @FXML
    public VBox rightTable;

    private Network network;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            network = new Network(8189);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void pushFail(ActionEvent actionEvent) {

    }

    public void downFail(ActionEvent actionEvent) {

    }
}