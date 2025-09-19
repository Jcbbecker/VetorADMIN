package com.brasens.layout.view;

import com.brasens.http.NetworkManager;
import com.brasens.javafx.utils.Page;
import com.brasens.layout.ApplicationWindow;
import com.brasens.layout.LayoutSizeManager;
import com.brasens.layout.controller.DashboardController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardView extends Page {

    private Button addUserButton;
    private Button manageUsersButton;
    private VBox mainContainer;

    public DashboardView(ApplicationWindow applicationWindow, NetworkManager networkManager) {
        super(applicationWindow, networkManager, "/mspm/pages/DashboardCSS.css");
        this.controller = new DashboardController(applicationWindow);
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
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setSpacing(30);
        mainContainer.setPadding(new Insets(50));

        AnchorPane.setTopAnchor(mainContainer, 0.0);
        AnchorPane.setRightAnchor(mainContainer, 0.0);
        AnchorPane.setBottomAnchor(mainContainer, 0.0);
        AnchorPane.setLeftAnchor(mainContainer, 0.0);

        // Título
        Label titleLabel = new Label("Sistema de Gerenciamento");
        titleLabel.setFont(new Font("Arial", (int)(36 * LayoutSizeManager.getInverseScreenAreaRatio())));
        titleLabel.setStyle("-fx-text-fill: #333333;");

        // Container para os botões
        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setSpacing(40);

        // Botão Adicionar Usuário
        VBox addUserBox = new VBox();
        addUserBox.setAlignment(Pos.CENTER);
        addUserBox.setSpacing(10);

        addUserButton = new Button("Adicionar Usuário");
        addUserButton.setPrefWidth(LayoutSizeManager.getResizedWidth(250));
        addUserButton.setPrefHeight(LayoutSizeManager.getResizedHeight(60));
        addUserButton.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 18px; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-font-weight: bold;"
        );
        addUserButton.setCursor(Cursor.HAND);

        addUserButton.setOnMouseEntered(e ->
                addUserButton.setStyle(
                        "-fx-background-color: #45a049; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 18px; " +
                                "-fx-background-radius: 8px; " +
                                "-fx-font-weight: bold;"
                )
        );

        addUserButton.setOnMouseExited(e ->
                addUserButton.setStyle(
                        "-fx-background-color: #4CAF50; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 18px; " +
                                "-fx-background-radius: 8px; " +
                                "-fx-font-weight: bold;"
                )
        );

        Label addUserDesc = new Label("Registrar novo usuário no sistema");
        addUserDesc.setStyle("-fx-text-fill: #666666;");

        addUserBox.getChildren().addAll(addUserButton, addUserDesc);

        // Botão Gerenciar Usuários
        VBox manageUsersBox = new VBox();
        manageUsersBox.setAlignment(Pos.CENTER);
        manageUsersBox.setSpacing(10);

        manageUsersButton = new Button("Gerenciar Usuários");
        manageUsersButton.setPrefWidth(LayoutSizeManager.getResizedWidth(250));
        manageUsersButton.setPrefHeight(LayoutSizeManager.getResizedHeight(60));
        manageUsersButton.setStyle(
                "-fx-background-color: #2196F3; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 18px; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-font-weight: bold;"
        );
        manageUsersButton.setCursor(Cursor.HAND);

        manageUsersButton.setOnMouseEntered(e ->
                manageUsersButton.setStyle(
                        "-fx-background-color: #0b7dda; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 18px; " +
                                "-fx-background-radius: 8px; " +
                                "-fx-font-weight: bold;"
                )
        );

        manageUsersButton.setOnMouseExited(e ->
                manageUsersButton.setStyle(
                        "-fx-background-color: #2196F3; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 18px; " +
                                "-fx-background-radius: 8px; " +
                                "-fx-font-weight: bold;"
                )
        );

        Label manageUsersDesc = new Label("Editar e gerenciar usuários existentes");
        manageUsersDesc.setStyle("-fx-text-fill: #666666;");

        manageUsersBox.getChildren().addAll(manageUsersButton, manageUsersDesc);

        // Adicionar boxes ao container de botões
        buttonContainer.getChildren().addAll(addUserBox, manageUsersBox);

        // Adicionar todos os elementos ao container principal
        mainContainer.getChildren().addAll(titleLabel, buttonContainer);

        // Adicionar container principal à página
        getChildren().add(mainContainer);

        // Configurar ações dos botões
        addUserButton.setOnAction(e -> {
            ((DashboardController) controller).onAddUserClick();
        });

        manageUsersButton.setOnAction(e -> {
            ((DashboardController) controller).onManageUsersClick();
        });
    }
}