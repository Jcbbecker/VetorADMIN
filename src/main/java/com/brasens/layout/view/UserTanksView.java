package com.brasens.layout.view;

import com.brasens.Tank;
import com.brasens.http.Employees;
import com.brasens.http.NetworkManager;
import com.brasens.javafx.utils.Page;
import com.brasens.layout.ApplicationWindow;
import com.brasens.layout.LayoutSizeManager;
import com.brasens.layout.controller.UserTanksController;
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

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserTanksView extends Page {

    private TableView<Tank> tanksTable;
    private ObservableList<Tank> tanksList;
    private Button backButton;
    private Button refreshButton;
    private Button addTankButton;
    private Label userLabel;
    private VBox mainContainer;
    private Employees currentEmployee;

    public UserTanksView(ApplicationWindow applicationWindow, NetworkManager networkManager) {
        super(applicationWindow, networkManager, "/mspm/pages/DashboardCSS.css");
        this.controller = new UserTanksController(applicationWindow);
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

        Label titleLabel = new Label("Tanques do Usuário");
        titleLabel.setFont(new Font("Arial", (int)(28 * LayoutSizeManager.getInverseScreenAreaRatio())));
        titleLabel.setStyle("-fx-text-fill: #333333;");

        userLabel = new Label("");
        userLabel.setFont(new Font("Arial", (int)(20 * LayoutSizeManager.getInverseScreenAreaRatio())));
        userLabel.setStyle("-fx-text-fill: #666666;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Botão Adicionar Tanque
        addTankButton = new Button("+ Novo Tanque");
        addTankButton.setPrefWidth(130);
        addTankButton.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 6px;"
        );
        addTankButton.setCursor(Cursor.HAND);
        addTankButton.setOnAction(e -> {
            ((UserTanksController) controller).onAddTank();
        });

        // Botão Atualizar
        refreshButton = new Button("Atualizar");
        refreshButton.setPrefWidth(100);
        refreshButton.setStyle(
                "-fx-background-color: #2196F3; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 6px;"
        );
        refreshButton.setCursor(Cursor.HAND);
        refreshButton.setOnAction(e -> {
            ((UserTanksController) controller).refreshTanksList();
        });

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
            ((UserTanksController) controller).onBackClick();
        });

        headerBox.getChildren().addAll(titleLabel, userLabel, spacer, addTankButton, refreshButton, backButton);

        // Inicializar lista de tanques
        tanksList = FXCollections.observableArrayList();

        // Criar tabela de tanques
        tanksTable = new TableView<>();
        tanksTable.setItems(tanksList);
        tanksTable.setPrefHeight(LayoutSizeManager.getResizedHeight(500));
        tanksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Coluna ID
        TableColumn<Tank, UUID> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(180);
        idColumn.setStyle("-fx-alignment: CENTER;");

        // Coluna Nome
        TableColumn<Tank, String> nameColumn = new TableColumn<>("Nome");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(150);

        // Coluna Key
        TableColumn<Tank, String> keyColumn = new TableColumn<>("Código");
        keyColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        keyColumn.setPrefWidth(120);

        // Coluna Tank1
        TableColumn<Tank, Integer> tank1Column = new TableColumn<>("Tanque 1");
        tank1Column.setCellValueFactory(new PropertyValueFactory<>("tank1"));
        tank1Column.setPrefWidth(100);
        tank1Column.setStyle("-fx-alignment: CENTER;");

        // Coluna Tank2
        TableColumn<Tank, Integer> tank2Column = new TableColumn<>("Tanque 2");
        tank2Column.setCellValueFactory(new PropertyValueFactory<>("tank2"));
        tank2Column.setPrefWidth(100);
        tank2Column.setStyle("-fx-alignment: CENTER;");

        // Coluna de Ações
        TableColumn<Tank, Void> actionsColumn = new TableColumn<>("Ações");
        actionsColumn.setPrefWidth(250);

        actionsColumn.setCellFactory(param -> new TableCell<Tank, Void>() {
            private final HBox actionBox = new HBox(10);
            private final Button editNameButton = new Button("Nome");
            private final Button editKeyButton = new Button("Código");
            private final Button deleteButton = new Button("Excluir");

            {
                // Estilizar botão Editar Nome
                editNameButton.setStyle(
                        "-fx-background-color: #2196F3; " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 4px; " +
                                "-fx-padding: 5 10; " +
                                "-fx-font-size: 12px;"
                );
                editNameButton.setCursor(Cursor.HAND);

                // Estilizar botão Editar Key
                editKeyButton.setStyle(
                        "-fx-background-color: #FF9800; " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 4px; " +
                                "-fx-padding: 5 10; " +
                                "-fx-font-size: 12px;"
                );
                editKeyButton.setCursor(Cursor.HAND);

                // Estilizar botão Excluir
                deleteButton.setStyle(
                        "-fx-background-color: #f44336; " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 4px; " +
                                "-fx-padding: 5 10; " +
                                "-fx-font-size: 12px;"
                );
                deleteButton.setCursor(Cursor.HAND);

                actionBox.setAlignment(Pos.CENTER);
                actionBox.getChildren().addAll(editNameButton, editKeyButton, deleteButton);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Tank tank = getTableView().getItems().get(getIndex());

                    editNameButton.setOnAction(e -> {
                        ((UserTanksController) controller).onEditTankName(tank);
                    });

                    editKeyButton.setOnAction(e -> {
                        ((UserTanksController) controller).onEditTankKey(tank);
                    });

                    deleteButton.setOnAction(e -> {
                        ((UserTanksController) controller).onDeleteTank(tank);
                    });

                    setGraphic(actionBox);
                }
            }
        });

        // Adicionar colunas à tabela
        tanksTable.getColumns().addAll(idColumn, nameColumn, keyColumn, tank1Column, tank2Column, actionsColumn);

        // Container para informações adicionais
        VBox infoBox = new VBox(10);
        infoBox.setPadding(new Insets(20));
        infoBox.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 8px;");

        Label infoLabel = new Label("Informações do Tanque");
        infoLabel.setFont(new Font("Arial Bold", (int)(16 * LayoutSizeManager.getInverseScreenAreaRatio())));

        Label instructionLabel = new Label("• Clique duas vezes em um tanque para ver detalhes\n" +
                "• Use os botões de ação para editar ou excluir\n" +
                "• Os valores dos tanques são atualizados automaticamente pelo sensor");
        instructionLabel.setStyle("-fx-text-fill: #666666;");

        infoBox.getChildren().addAll(infoLabel, instructionLabel);

        // Adicionar elementos ao container principal
        mainContainer.getChildren().addAll(headerBox, tanksTable, infoBox);

        // Adicionar container à página
        getChildren().add(mainContainer);
    }

    public void setCurrentEmployee(Employees employee) {
        this.currentEmployee = employee;
        if (employee != null) {
            userLabel.setText("- " + employee.getLogin());
        }
    }

    public void updateTanksList(List<Tank> tanks) {
        tanksList.clear();
        tanksList.addAll(tanks);
    }
}