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
import com.brasens.layout.view.UsersManagementView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.lang.reflect.Type;
import java.util.*;

public class UsersManagementController extends Controller {

    private UsersManagementView usersManagementView;
    private ApplicationWindow applicationWindow;

    public UsersManagementController(ApplicationWindow applicationWindow) {
        this.applicationWindow = applicationWindow;
    }

    @Override
    public void init() {
        usersManagementView = applicationWindow.getViewManager().getUsersManagementView();
        System.out.println("Users Management View initialized");
        refreshUsersList();
    }

    @Override
    public void close() {
        System.out.println("Users Management View closed");
    }

    @Override
    public void update() {
        // Atualização periódica se necessário
    }

    public void refreshUsersList() {
        try {
            String token = applicationWindow.getNetworkManager().getToken().getTokenJWT();

//            if (token == null || token.isEmpty()) {
//                Vetor.openActionPopUp("Erro", "Token de autenticação não encontrado", ActionStatusPopUp.ActionStatus.ERROR);
//                return;
//            }

            HTTPResponse response = HTTPRequests.GET(
                    Config.BACKEND_HOST_GET_ALL_USERS, ""
            );

            if (response.getCode() == HttpStatusCode.OK) {
                Gson gson = new GsonBuilder().create();

                // Parse da resposta para lista de Maps
                Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
                List<Map<String, Object>> usersData = gson.fromJson(response.getContent(), listType);

                // Converter Maps para objetos Employees
                List<Employees> users = new ArrayList<>();
                for (Map<String, Object> userData : usersData) {
                    Employees emp = new Employees();
                    emp.setId(UUID.fromString((String) userData.get("id")));
                    emp.setLogin((String) userData.get("login"));
                    emp.setEmail((String) userData.get("email"));
                    emp.setKey((String) userData.get("key"));
                    emp.setPassword((String) userData.get("password"));
                    users.add(emp);
                }

                Platform.runLater(() -> usersManagementView.updateUsersList(users));
            } else if (response.getCode() == HttpStatusCode.UNAUTHORIZED) {
                Vetor.openActionPopUp("Erro", "Sessão expirada. Faça login novamente", ActionStatusPopUp.ActionStatus.ERROR);
            } else {
                Vetor.openActionPopUp("Erro", "Falha ao carregar usuários", ActionStatusPopUp.ActionStatus.ERROR);
            }
        } catch (Exception e) {
            Vetor.printNicerStackTrace(e);
            Vetor.openActionPopUp("Erro", "Erro ao buscar usuários: " + e.getMessage(), ActionStatusPopUp.ActionStatus.ERROR);
        }
    }

    public void onBackClick() {
        applicationWindow.changePage(applicationWindow.getViewManager().getDashboardView());
    }

    public void onEditUser(Employees employee) {
        TextInputDialog dialog = new TextInputDialog(employee.getLogin());
        dialog.setTitle("Editar Usuário");
        dialog.setHeaderText("Editar nome do usuário");
        dialog.setContentText("Novo nome:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            try {
                Map<String, String> updateData = new HashMap<>();
                updateData.put("id", employee.getId().toString());
                updateData.put("name", newName);

                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(updateData);

                String token = applicationWindow.getNetworkManager().getToken().getTokenJWT();
                HTTPResponse response = HTTPRequests.PUT(
                        Config.BACKEND_HOST_UPDATE_USER,
                        "",
                        json
                );

                if (response.getCode() == HttpStatusCode.OK) {
                    refreshUsersList();
                    Vetor.openActionPopUp("Sucesso", "Usuário atualizado!", ActionStatusPopUp.ActionStatus.OK);
                } else if (response.getCode() == HttpStatusCode.UNAUTHORIZED) {
                    Vetor.openActionPopUp("Erro", "Sessão expirada", ActionStatusPopUp.ActionStatus.ERROR);
                } else {
                    Vetor.openActionPopUp("Erro", "Falha ao atualizar usuário", ActionStatusPopUp.ActionStatus.ERROR);
                }
            } catch (Exception e) {
                Vetor.printNicerStackTrace(e);
                Vetor.openActionPopUp("Erro", "Erro ao atualizar usuário", ActionStatusPopUp.ActionStatus.ERROR);
            }
        });
    }

    public void onDeleteUser(Employees employee) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir usuário");
        alert.setContentText("Tem certeza que deseja excluir o usuário " + employee.getLogin() + "?\n" +
                "Todos os tanques associados também serão removidos!");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String token = applicationWindow.getNetworkManager().getToken().getTokenJWT();
                HTTPResponse response = HTTPRequests.DELETE(
                        Config.BACKEND_HOST_DELETE_USER + "/" + employee.getId(),
                        ""
                );

                if (response.getCode() == HttpStatusCode.OK || response.getCode() == HttpStatusCode.NO_CONTENT) {
                    refreshUsersList();
                    Vetor.openActionPopUp("Sucesso", "Usuário excluído!", ActionStatusPopUp.ActionStatus.OK);
                } else if (response.getCode() == HttpStatusCode.UNAUTHORIZED) {
                    Vetor.openActionPopUp("Erro", "Sessão expirada", ActionStatusPopUp.ActionStatus.ERROR);
                } else {
                    Vetor.openActionPopUp("Erro", "Falha ao excluir usuário", ActionStatusPopUp.ActionStatus.ERROR);
                }
            } catch (Exception e) {
                Vetor.printNicerStackTrace(e);
                Vetor.openActionPopUp("Erro", "Erro ao excluir usuário", ActionStatusPopUp.ActionStatus.ERROR);
            }
        }
    }

    public void onViewTanks(Employees employee) {
        // Salvar o usuário selecionado no NetworkManager
        applicationWindow.getNetworkManager().setSelectedEmployee(employee);
        // Navegar para a view de tanques
        applicationWindow.changePage(applicationWindow.getViewManager().getUserTanksView());
    }

    public void onAddTank(Employees employee) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Adicionar Tanque");
        dialog.setHeaderText("Adicionar novo tanque para " + employee.getLogin());
        dialog.setContentText("Código do tanque:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(tankKey -> {
            try {
                Map<String, Object> tankData = new HashMap<>();
                tankData.put("key", tankKey);
                tankData.put("employeeId", employee.getId().toString());
                tankData.put("name", "Tanque " + tankKey);
                tankData.put("tank1", 0);
                tankData.put("tank2", 0);

                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(tankData);

                String token = applicationWindow.getNetworkManager().getToken().getTokenJWT();
                HTTPResponse response = HTTPRequests.POST(
                        Config.BACKEND_HOST_ADD_TANK,
                        "",
                        json
                );

                if (response.getCode() == HttpStatusCode.OK) {
                    Vetor.openActionPopUp("Sucesso", "Tanque adicionado!", ActionStatusPopUp.ActionStatus.OK);
                } else if (response.getCode() == HttpStatusCode.BAD_REQUEST) {
                    // Parse error message
                    Map<String, String> errorResponse = gson.fromJson(response.getContent(), Map.class);
                    String errorMsg = errorResponse.getOrDefault("message", "Falha ao adicionar tanque");
                    Vetor.openActionPopUp("Erro", errorMsg, ActionStatusPopUp.ActionStatus.ERROR);
                } else {
                    Vetor.openActionPopUp("Erro", "Falha ao adicionar tanque", ActionStatusPopUp.ActionStatus.ERROR);
                }
            } catch (Exception e) {
                Vetor.printNicerStackTrace(e);
                Vetor.openActionPopUp("Erro", "Erro ao adicionar tanque", ActionStatusPopUp.ActionStatus.ERROR);
            }
        });
    }
}