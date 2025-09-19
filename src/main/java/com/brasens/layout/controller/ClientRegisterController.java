package com.brasens.layout.controller;

import com.brasens.Config;
import com.brasens.Vetor;
import com.brasens.http.Employees;
import com.brasens.http.HTTPRequests;
import com.brasens.http.HTTPResponse;
import com.brasens.http.objects.HttpStatusCode;
import com.brasens.javafx.utils.Controller;
import com.brasens.layout.ApplicationWindow;
import com.brasens.layout.controller.fxml.ActionStatusPopUp;
import com.brasens.layout.view.ClientRegisterView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class ClientRegisterController extends Controller {

    ClientRegisterView clientRegisterView;
    ApplicationWindow applicationWindow;

    public ClientRegisterController(ApplicationWindow applicationWindow) {
        this.applicationWindow = applicationWindow;
    }

    @Override
    public void init() {
        clientRegisterView = applicationWindow.getViewManager().getClientRegisterView();
        System.out.println("Starting Register Bridge");
    }

    @Override
    public void close() {}

    @Override
    public void update() {}

    public void onClickInRegisterButton(String name, String email, String password, String confirmPassword, String sensorCode) {
        if (name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()) {
            Vetor.openActionPopUp("Erro", "Preencha todos os campos", ActionStatusPopUp.ActionStatus.ERROR);
            return;
        }

        if (!password.equals(confirmPassword)) {
            Vetor.openActionPopUp("Erro", "As senhas não coincidem", ActionStatusPopUp.ActionStatus.ERROR);
            return;
        }
        String employeeID = sendClientRegisterRequest(name, email, password);
        if(employeeID == null)
            Vetor.openActionPopUp("Erro", "Falha ao registrar usuario", ActionStatusPopUp.ActionStatus.ERROR);
        else {

            clientRegisterView.getNameField().clear();
            clientRegisterView.getEmailField().clear();
            clientRegisterView.getPasswordField().clear();
            clientRegisterView.getConfirmPasswordField().clear();
            clientRegisterView.getSensorCode().clear();

            onBackClick();
            Vetor.openActionPopUp("Concluido", "Usuario registrado!", ActionStatusPopUp.ActionStatus.OK);
            /*
            String token = getToken(email, password);
            System.out.println(token);
            if (!sendSensorCodeRequest(sensorCode, token, employeeID))
                Vetor.openActionPopUp("Erro", "Falha ao registrar sensor", ActionStatusPopUp.ActionStatus.ERROR);
            else {
                Vetor.openActionPopUp("Concluido", "Usuario registrado!", ActionStatusPopUp.ActionStatus.OK);
                clientRegisterView.getNameField().clear();
                clientRegisterView.getEmailField().clear();
                clientRegisterView.getPasswordField().clear();
                clientRegisterView.getConfirmPasswordField().clear();
                clientRegisterView.getSensorCode().clear();
            }*/
        }
    }

    public String sendClientRegisterRequest(String name, String email, String password) {
        Map<String, String> myMap = new HashMap<>();
        myMap.put("name", name);
        myMap.put("email", email);
        myMap.put("password", password);

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(myMap);
        System.out.println("Registrando usuario:");
        try {
            HTTPResponse response = HTTPRequests.POST(Config.BACKEND_HOST_CLIENT_REGISTER, json);
            return response.getContent();
        } catch (Exception e) {
            Vetor.printNicerStackTrace(e);
            Vetor.openActionPopUp("Erro", "Falha ao conectar ao servidor", ActionStatusPopUp.ActionStatus.ERROR);
        }
        return null;
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

    public boolean sendSensorCodeRequest(String sensorCode, String token, String employeeID) {
        try {
        Map<String, Object> tankData = new HashMap<>();
        tankData.put("key", sensorCode);
        tankData.put("name", "Tanque " + sensorCode);
        tankData.put("tank1", 0);
        tankData.put("tank2", 0);
        tankData.put("employeeId", employeeID);

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(tankData);

        HTTPResponse response = HTTPRequests.POST(
                Config.BACKEND_HOST_ADD_TANK,
                "",
                json
        );

        System.out.println("Registrando sensor...");

            return response.getCode() == HttpStatusCode.OK;
        } catch (Exception e) {
            Vetor.printNicerStackTrace(e);
            Vetor.openActionPopUp("Erro", "Falha ao conectar ao servidor", ActionStatusPopUp.ActionStatus.ERROR);
        }
        return false;
    }

    public void onBackClick() {
        applicationWindow.changePage(applicationWindow.getViewManager().getDashboardView());
    }

}
