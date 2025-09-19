package com.brasens.layout.view;

import com.brasens.Config;
import com.brasens.Vetor;
import com.brasens.http.Employees;
import com.brasens.http.HTTPRequests;
import com.brasens.http.HTTPResponse;
import com.brasens.http.NetworkManager;
import com.brasens.http.objects.HttpStatusCode;
import com.brasens.javafx.utils.Page;
import com.brasens.layout.ApplicationWindow;
import com.brasens.layout.LayoutSizeManager;
import com.brasens.layout.controller.UsersManagementController;
import com.brasens.layout.controller.fxml.ActionStatusPopUp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class UsersManagementView extends Page {

    private TableView<Employees> usersTable;
    private ObservableList<Employees> usersList;
    private Button backButton;
    private Button refreshButton;
    private VBox mainContainer;

    public UsersManagementView(ApplicationWindow applicationWindow, NetworkManager networkManager) {
        super(applicationWindow, networkManager, "/mspm/pages/DashboardCSS.css");
        this.controller = new UsersManagementController(applicationWindow);
        createView();
    }

    public void createView() {
        // Configuração principal da página
        setMaxHeight(Double.MAX_VALUE);
        setMaxWidth(Double.MAX_VALUE);
        setPrefHeight(LayoutSizeManager.getResizedHeight(800));
        setPrefWidth(LayoutSizeManager.getResizedWidth(1280));
        getStyleClass().add("body");

        // Container principal
        mainContainer = new VBox();
        mainContainer.setSpacing(20);
        mainContainer.setPadding(new Insets(30));

        AnchorPane.setTopAnchor(mainContainer, 0.0);
        AnchorPane.setRightAnchor(mainContainer, 0.0);
        AnchorPane.setBottomAnchor(mainContainer, 0.0);
        AnchorPane.setLeftAnchor(mainContainer, 0.0);

        // Header com título e botões
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setSpacing(20);

        Label titleLabel = new Label("Gerenciar Usuários");
        titleLabel.setFont(new Font("Arial", (int)(28 * LayoutSizeManager.getInverseScreenAreaRatio())));
        titleLabel.setStyle("-fx-text-fill: #333333;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Botão Voltar
        backButton = new Button("Voltar");
        backButton.setPrefWidth(100);
        backButton.setStyle(
                "-fx-background-color: #757575; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 6px;"
        );
        backButton.setCursor(Cursor.HAND);
        backButton.setOnAction(e -> {
            ((UsersManagementController) controller).onBackClick();
        });

        // Botão Atualizar
        refreshButton = new Button("Atualizar");
        refreshButton.setPrefWidth(100);
        refreshButton.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 6px;"
        );
        refreshButton.setCursor(Cursor.HAND);
        refreshButton.setOnAction(e -> {
            ((UsersManagementController) controller).refreshUsersList();
        });

        headerBox.getChildren().addAll(titleLabel, spacer, refreshButton, backButton);

        // Inicializar lista de usuários
        usersList = FXCollections.observableArrayList();

        // Criar tabela de usuários
        usersTable = new TableView<>();
        usersTable.setItems(usersList);
        usersTable.setPrefHeight(LayoutSizeManager.getResizedHeight(500));
        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Coluna ID
        TableColumn<Employees, UUID> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(200);
        idColumn.setStyle("-fx-alignment: CENTER;");

        // Coluna Nome
        TableColumn<Employees, String> nameColumn = new TableColumn<>("Nome");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        nameColumn.setPrefWidth(150);

        // Coluna Email
        TableColumn<Employees, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setPrefWidth(200);

        // Coluna de Ações
        TableColumn<Employees, Void> actionsColumn = new TableColumn<>("Ações");
        actionsColumn.setPrefWidth(300);

        actionsColumn.setCellFactory(param -> new TableCell<Employees, Void>() {
            private final HBox actionBox = new HBox(10);
            private final Button editButton = new Button("Editar");
            private final Button deleteButton = new Button("Excluir");
            private final Button tanksButton = new Button("Tanques");
            private final Button addTankButton = new Button("+ Tanque");

            {
                // Estilizar botão Editar
                editButton.setStyle(
                        "-fx-background-color: #2196F3; " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 4px; " +
                                "-fx-padding: 5 10;"
                );
                editButton.setCursor(Cursor.HAND);

                // Estilizar botão Excluir
                deleteButton.setStyle(
                        "-fx-background-color: #f44336; " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 4px; " +
                                "-fx-padding: 5 10;"
                );
                deleteButton.setCursor(Cursor.HAND);

                // Estilizar botão Tanques
                tanksButton.setStyle(
                        "-fx-background-color: #FF9800; " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 4px; " +
                                "-fx-padding: 5 10;"
                );
                tanksButton.setCursor(Cursor.HAND);

                // Estilizar botão Adicionar Tanque
                addTankButton.setStyle(
                        "-fx-background-color: #4CAF50; " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 4px; " +
                                "-fx-padding: 5 10;"
                );
                addTankButton.setCursor(Cursor.HAND);

                actionBox.setAlignment(Pos.CENTER);
                actionBox.getChildren().addAll(editButton, tanksButton, addTankButton, deleteButton);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Employees employee = getTableView().getItems().get(getIndex());

                    System.out.println(employee.getEmail() +" "+ employee.getPassword());

                    //String token = getToken(employee.getEmail(), employee.getPassword());
                    //getNetworkManager().setTokenJWT(token);

                    editButton.setOnAction(e -> {
                        ((UsersManagementController) controller).onEditUser(employee);
                    });

                    deleteButton.setOnAction(e -> {
                        ((UsersManagementController) controller).onDeleteUser(employee);
                    });

                    tanksButton.setOnAction(e -> {
                        ((UsersManagementController) controller).onViewTanks(employee);
                    });

                    addTankButton.setOnAction(e -> {
                        ((UsersManagementController) controller).onAddTank(employee);
                    });

                    setGraphic(actionBox);
                }
            }
        });

        // Adicionar colunas à tabela
        usersTable.getColumns().addAll(idColumn, nameColumn, emailColumn, actionsColumn);

        // Configurar clique na linha da tabela
        usersTable.setRowFactory(tv -> {
            TableRow<Employees> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    Employees employee = row.getItem();
                    ((UsersManagementController) controller).onViewTanks(employee);
                }
            });
            return row;
        });

        // Adicionar elementos ao container principal
        mainContainer.getChildren().addAll(headerBox, usersTable);

        // Adicionar container à página
        getChildren().add(mainContainer);
    }

    public void updateUsersList(List<Employees> users) {
        usersList.clear();
        usersList.addAll(users);
    }

    public String getToken(String email, String password) {
        Map<String, String> myMap = new HashMap<>();
        myMap.put("email", email);
        myMap.put("password", password);

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(myMap);
        System.out.println("Entrando com usuario...");
        try {
            HTTPResponse response = HTTPRequests.POST(Config.BACKEND_HOST_CLIENT_LOGIN, json);
            if(response.getCode() == HttpStatusCode.OK)
                return response.getContent();
        } catch (Exception e) {
            Vetor.printNicerStackTrace(e);
            Vetor.openActionPopUp("Erro", "Falha ao conectar ao servidor", ActionStatusPopUp.ActionStatus.ERROR);
        }
        return "";
    }

}