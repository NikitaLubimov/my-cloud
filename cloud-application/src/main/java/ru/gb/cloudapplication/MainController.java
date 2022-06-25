package ru.gb.cloudapplication;


import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
    public TableView<FileInfoServer> tableViewServer;
    @FXML
    public TextField pathFieldServer;
    @FXML
    public ComboBox diskBoxServer;
    private Network network;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            network = new Network(8189, this);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        readyClientTable();
        readyServerTable();

        Thread msgListener = new Thread(() -> {
            try {
                network.msgReader();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        msgListener.setDaemon(true);
        msgListener.start();

        try {
            network.write(new FileRequest());
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        TableColumn<FileInfoServer, String> fileTypeColumnServer = new TableColumn<>();
        fileTypeColumnServer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().isType()));

        TableColumn<FileInfoServer, String> fileNameColumnServer = new TableColumn<>("Имя");
        fileNameColumnServer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileName()));
        fileNameColumnServer.setPrefWidth(180);

        TableColumn<FileInfoServer, Long> fileSizeColumnServer = new TableColumn<>("Размер");
        fileSizeColumnServer.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumnServer.setPrefWidth(100);

        fileSizeColumnServer.setCellFactory(column -> {
            return new TableCell<FileInfoServer, Long>() {
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

        tableViewServer.getColumns().addAll(fileTypeColumnServer, fileNameColumnServer, fileSizeColumnServer);
        tableViewServer.getSortOrder().add(fileTypeColumnServer);

        tableViewServer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    if (tableViewServer.getSelectionModel().getSelectedItem() != null) {
                        FileInfoServer fis = tableViewServer.getSelectionModel().getSelectedItem();
                        if (fis.isDir()) {
                            try {
                                network.pathChangeRequest(fis);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    public void updateList(Path path) {
        try {
            pathFieldClient.setText(path.normalize().toAbsolutePath().toString());
            tableViewClient.getItems().clear();
            tableViewClient.getItems().addAll(Files.list(path).map(FileInfo::new).toList());
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
    public void upPathServer(ActionEvent actionEvent) throws IOException {
        network.write(new ServerPathUpRequest());
    }

    // выбор диска клиента
    public void selectDiskActionClient(ActionEvent actionEvent) {
        ComboBox<String> element = (ComboBox<String>) actionEvent.getSource();
        updateList(Paths.get(element.getSelectionModel().getSelectedItem()));
    }


    // скачивание файла с сервера
    public void downFail(ActionEvent actionEvent) {
        FileInfoServer file = tableViewServer.getSelectionModel().getSelectedItem();
        if (file.isDir()) {
            return;
        } else {
            try {
                network.getFileFromServer(file.getFileName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // отправка файла на сервер
    public void sendFailToServer(ActionEvent actionEvent) throws IOException {
        if (tableViewClient.getSelectionModel().getSelectedItem() != null) {
            String file = tableViewClient.getSelectionModel().getSelectedItem().getFilename();
            Path filePath = Paths.get(pathFieldClient.getText()).resolve(file);
            if (Files.isDirectory(filePath)) {
                return;
            } else {
                network.sendFileToServer(filePath);
                network.requestFilesToServer();
            }
        }
    }

    // обновление таблицы файлов на клиенте
    public void updateServerList(List<FileInfoServer> files) {
        tableViewServer.getItems().clear();
        tableViewServer.getItems().addAll(files);
        tableViewServer.sort();
    }

    // возвращает текущею деррикторию клиента
    public String getCurrentPath() {
        return pathFieldClient.getText();
    }

    // кнопка закрытия приложения
    public void close(ActionEvent actionEvent) {
        Platform.exit();
    }

    // кнопка удаления файла
    public void deleteFile(ActionEvent actionEvent) {

    }
}