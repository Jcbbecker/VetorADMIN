package com.brasens.layout.view;

import com.brasens.http.NetworkManager;
import com.brasens.javafx.utils.Page;
import com.brasens.layout.ApplicationWindow;
import com.brasens.layout.LayoutSizeManager;
import com.brasens.layout.controller.ClientRegisterController;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientRegisterView extends Page {

    private TextField nameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button registerButton;
    private TextField sensorCode;

    public ClientRegisterView(ApplicationWindow applicationWindow, NetworkManager networkManager) {
        super(applicationWindow, networkManager, "/mspm/pages/RegisterCSS.css");
        this.controller = new ClientRegisterController(applicationWindow);
        createView();
    }

    public void createView() {
        double cardPrefHeight = LayoutSizeManager.getResizedHeight(380);
        double cardPrefWidth = LayoutSizeManager.getResizedWidth(460);

        double topOffset = LayoutSizeManager.getResizedHeight(25.0);
        double leftOffset = LayoutSizeManager.getResizedWidth(40.0);
        double rightOffset = LayoutSizeManager.getResizedWidth(40.0);

        Font buttonFont = new Font("Arial", (int)(26 * LayoutSizeManager.getInverseScreenAreaRatio()));

        VBox root = new VBox();
        setPrefHeight(LayoutSizeManager.getResizedHeight(400.0));
        setPrefWidth(LayoutSizeManager.getResizedWidth(360.0));

        setMaxHeight(LayoutSizeManager.getResizedHeight(400.0));
        setMaxWidth(LayoutSizeManager.getResizedWidth(360.0));

        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        AnchorPane.setTopAnchor(root, 0.0);

        root.setPrefSize(LayoutSizeManager.getResizedWidth(1280), LayoutSizeManager.getResizedHeight(800));

        VBox formBox = new VBox();
        formBox.setSpacing(15);
        formBox.setAlignment(Pos.CENTER);

        Label title = new Label("Registrar Novo Usuário");
        title.setFont(new Font("Arial", 22));

        nameField = new TextField();
        nameField.setPromptText("Nome");
        nameField.setPrefWidth(300);

        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setPrefWidth(300);

        passwordField = new PasswordField();
        passwordField.setPromptText("Senha");
        passwordField.setPrefWidth(300);


        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirmar Senha");
        confirmPasswordField.setPrefWidth(300);

        sensorCode = new TextField();
        sensorCode.setPromptText("Codigo do Dispositivo");
        sensorCode.setPrefWidth(300);

        registerButton = new Button("Registrar");
        registerButton.setPrefWidth(300);
        registerButton.setStyle("-fx-background-color: #0b1727; -fx-text-fill: white; -fx-background-radius: 6px;");
        registerButton.setCursor(Cursor.HAND);

        registerButton.setOnAction(e -> {
            ((ClientRegisterController) controller).onClickInRegisterButton(
                    nameField.getText(),
                    emailField.getText(),
                    passwordField.getText(),
                    confirmPasswordField.getText(),
                    sensorCode.getText()
            );
        });

        formBox.getChildren().addAll(title, nameField, emailField, passwordField, confirmPasswordField, sensorCode, registerButton);

        root.getChildren().add(formBox);
        root.setAlignment(Pos.CENTER);

        getChildren().add(root);
    }
}
