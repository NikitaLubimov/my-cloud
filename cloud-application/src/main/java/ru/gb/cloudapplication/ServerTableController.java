package ru.gb.cloudapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerTableController implements Initializable {


    @FXML
    public ComboBox diskBox;
    @FXML
    public TextField pathField;
    @FXML
    public TableView<FileInfo> tableViewServer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>();
        fileTypeColumn.setPrefWidth(30);

        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("Имя");
        fileNameColumn.setPrefWidth(180);


        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Размер");
        fileSizeColumn.setPrefWidth(100);





        tableViewServer.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn);
        tableViewServer.getSortOrder().add(fileTypeColumn);

    }

    public void upPath(ActionEvent actionEvent) {


    }
}
