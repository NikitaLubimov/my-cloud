package ru.gb.cloudapplication;


import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class MainController implements Initializable {

    // поля клиента таблицы
    @FXML
    public TextField pathFieldClient;
    @FXML
    public TableView<FileInfo> tableViewClient;
    @FXML
    public ComboBox<String> diskBoxClient;

    // поля сервера таблицы
    @FXML
    public TableView<FileInfo> tableViewServer;
    @FXML
    public TextField pathFieldServer;
    @FXML
    public ComboBox diskBoxServer;

    private Network network;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            network = new Network(8189);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        readyClientTable();
        readyServerTable();

    }

    // Заполнение клиент таблицы
    private void readyClientTable() {
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
                        if (item == -1l) {
                            text = "";
                        }
                        setText(text);
                    }
                }
            };
        });

        tableViewClient.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn);
        tableViewClient.getSortOrder().add(fileTypeColumn);

        diskBoxClient.getItems().clear();
        for (Path p : FileSystems.getDefault().getRootDirectories()) {
            diskBoxClient.getItems().add(p.toString());
        }
        diskBoxClient.getSelectionModel().select(0);

        tableViewClient.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    Path path = Paths.get(pathFieldClient.getText()).resolve(tableViewClient.getSelectionModel().getSelectedItem().getFilename());
                    if (Files.isDirectory(path)) {
                        updateList(path);
                    }
                }
            }
        });

        updateList(Paths.get("."));
    }


    // Заполнение сервера таблицы
    private void readyServerTable() {
        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>();
        fileTypeColumn.setPrefWidth(30);

        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("Имя");
        fileNameColumn.setPrefWidth(180);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Размер");
        fileSizeColumn.setPrefWidth(100);

        tableViewServer.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn);
        tableViewServer.getSortOrder().add(fileTypeColumn);

    }

    public void updateList(Path path) {
        try {
            pathFieldClient.setText(path.normalize().toAbsolutePath().toString());
            tableViewClient.getItems().clear();
            tableViewClient.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            tableViewClient.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось обновить список файлов", ButtonType.OK);
            alert.showAndWait();
        }
    }

    // кнопка вверх клиента
    public void upPathClient(ActionEvent actionEvent) {
        Path upPath = Paths.get(pathFieldClient.getText()).getParent();
        if (upPath != null) {
            updateList(upPath);
        }
    }

    // кнопка вверх сервера
    public void upPathServer(ActionEvent actionEvent) {

    }

    // выбор диска клиента
    public void selectDiskActionClient(ActionEvent actionEvent) {
        ComboBox<String> element = (ComboBox<String>) actionEvent.getSource();
        updateList(Paths.get(element.getSelectionModel().getSelectedItem()));
    }

    // выбор диска сервера
    public void selectDiskActionServer(ActionEvent actionEvent) {
    }

    //    private void readloop() {
//        try {
//            while (true) {
//                String msg = network.readMSG();
//                // Platform.runLater(() ->  Что ту тут надо);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//

    // отправка файла
    public void pushFail(ActionEvent actionEvent) {

    }

    // скачивание файла
    public void downFail(ActionEvent actionEvent) {

    }
}