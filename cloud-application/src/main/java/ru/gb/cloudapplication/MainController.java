package ru.gb.cloudapplication;


import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML
    public VBox leftTable;


    private Network network;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            network = new Network(8189);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void readloop () {
        try {
            while (true) {
                String msg = network.readMSG();
               // Platform.runLater(() ->  Что ту тут надо);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pushFail(ActionEvent actionEvent) {

    }

    public void downFail(ActionEvent actionEvent) {

    }
}