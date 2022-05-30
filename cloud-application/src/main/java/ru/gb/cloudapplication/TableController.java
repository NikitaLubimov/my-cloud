package ru.gb.cloudapplication;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TableController implements Initializable {

    @FXML
    public TableView<FileInfo> tableViewClient;
    @FXML
    public VBox cloudViewClient;
    @FXML
    public ComboBox<String> diskBox;
    @FXML
    public TextField pathField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>();
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileType().getName()));
        fileTypeColumn.setPrefWidth(30);

        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("Имя");
        fileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFilename()));
        fileNameColumn.setPrefWidth(180);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Размер");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getFilesize()));
        fileSizeColumn.setPrefWidth(100);

        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if (item == -1l){
                            text = "";
                        }
                        setText(text);
                    }
                }
            };
        });

        tableViewClient.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn);
        tableViewClient.getSortOrder().add(fileTypeColumn);

        diskBox.getItems().clear();
        for (Path p: FileSystems.getDefault().getRootDirectories()) {
            diskBox.getItems().add(p.toString());
        }
        diskBox.getSelectionModel().select(0);

        updateList(Paths.get("."));
    }

    public void updateList (Path path) {
        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());
            tableViewClient.getItems().clear();
            tableViewClient.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            tableViewClient.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось обновить список файлов", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void upPath(ActionEvent actionEvent) {
        Path upPath = Paths.get(pathField.getText()).getParent();
        if (upPath != null) {
            updateList(upPath);
        }
    }
}
