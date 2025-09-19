package com.brasens.layout.controller;

import com.brasens.Config;
import com.brasens.Tank;
import com.brasens.Vetor;
import com.brasens.http.Employees;
import com.brasens.http.HTTPRequests;
import com.brasens.http.HTTPResponse;
import com.brasens.http.objects.HttpStatusCode;
import com.brasens.javafx.utils.Controller;
import com.brasens.layout.ApplicationWindow;
import com.brasens.layout.controller.fxml.ActionStatusPopUp;
import com.brasens.layout.view.UserTanksView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.lang.reflect.Type;
import java.util.*;

public class UserTanksController extends Controller {

    private UserTanksView userTanksView;
    private ApplicationWindow applicationWindow;
    private Employees currentEmployee;

    public UserTanksController(ApplicationWindow applicationWindow) {
        this.applicationWindow = applicationWindow;
    }

    @Override
    public void init() {
        userTanksView = applicationWindow.getViewManager().getUserTanksView();
        currentEmployee = applicationWindow.getNetworkManager().getSelectedEmployee();

        if (currentEmployee != null) {
            userTanksView.setCurrentEmployee(currentEmployee);
            refreshTanksList();
        } else {
            Vetor.openActionPopUp("Erro", "Nenhum usuário selecionado", ActionStatusPopUp.ActionStatus.ERROR);
            onBackClick();
        }

        System.out.println("User Tanks View initialized for user: " +
                (currentEmployee != null ? currentEmployee.getLogin() : "null"));
    }

    @Override
    public void close() {
        System.out.println("User Tanks View closed");
    }

    @Override
    public void update() {
        // Atualização periódica dos níveis dos tanques se necessário
    }

    public void refreshTanksList() {
        if (currentEmployee == null) {
            Vetor.openActionPopUp("Erro", "Nenhum usuário selecionado", ActionStatusPopUp.ActionStatus.ERROR);
            return;
        }

        try {
            String token = applicationWindow.getNetworkManager().getToken().getTokenJWT();

            System.out.println("Try get tanks: "+ currentEmployee.getId());

            HTTPResponse response = HTTPRequests.GET(
                    Config.BACKEND_HOST_GET_USER_TANKS + "/" + currentEmployee.getId(),
                    ""
            );
            System.out.println(response);

            if (response.getCode() == HttpStatusCode.OK) {
                Gson gson = new GsonBuilder().create();

                // Parse da resposta
                Type responseType = new TypeToken<Map<String, Object>>(){}.getType();
                Map<String, Object> responseData = gson.fromJson(response.getContent(), responseType);

                // Extrair lista de tanques
                List<Map<String, Object>> tanksData = (List<Map<String, Object>>) responseData.get("tanks");
                List<Tank> tanks = new ArrayList<>();

                for (Map<String, Object> tankData : tanksData) {
                    Tank tank = new Tank();
                    tank.setId(UUID.fromString((String) tankData.get("id")));
                    tank.setName((String) tankData.get("name"));
                    tank.setKey((String) tankData.get("key"));

                    // Converter números de forma segura
                    Object tank1Obj = tankData.get("tank1");
                    Object tank2Obj = tankData.get("tank2");

                    if (tank1Obj instanceof Number) {
                        tank.setTank1(((Number) tank1Obj).intValue());
                    }
                    if (tank2Obj instanceof Number) {
                        tank.setTank2(((Number) tank2Obj).intValue());
                    }

                    tanks.add(tank);
                }

                Platform.runLater(() -> userTanksView.updateTanksList(tanks));
            } else if (response.getCode() == HttpStatusCode.NOT_FOUND) {
                // Usuário não tem tanques ainda
                Platform.runLater(() -> userTanksView.updateTanksList(new ArrayList<>()));
            } else if (response.getCode() == HttpStatusCode.UNAUTHORIZED) {
                Vetor.openActionPopUp("Erro", "Sessão expirada", ActionStatusPopUp.ActionStatus.ERROR);
            } else {
                Vetor.openActionPopUp("Erro", "Falha ao carregar tanques", ActionStatusPopUp.ActionStatus.ERROR);
            }
        } catch (Exception e) {
            Vetor.printNicerStackTrace(e);
            Vetor.openActionPopUp("Erro", "Erro ao buscar tanques", ActionStatusPopUp.ActionStatus.ERROR);
        }
    }

    public void onBackClick() {
        applicationWindow.changePage(applicationWindow.getViewManager().getUsersManagementView());
    }

    public void onAddTank() {
        if (currentEmployee == null) {
            Vetor.openActionPopUp("Erro", "Nenhum usuário selecionado", ActionStatusPopUp.ActionStatus.ERROR);
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Adicionar Tanque");
        dialog.setHeaderText("Adicionar novo tanque para " + currentEmployee.getLogin());
        dialog.setContentText("Código do tanque:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(tankKey -> {
            try {
                Map<String, Object> tankData = new HashMap<>();
                tankData.put("key", tankKey);
                tankData.put("name", "Tanque " + tankKey);
                tankData.put("tank1", 0);
                tankData.put("tank2", 0);
                tankData.put("employeeId", currentEmployee.getId().toString());

                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(tankData);

                String token = applicationWindow.getNetworkManager().getToken().getTokenJWT();
                HTTPResponse response = HTTPRequests.POST(
                        Config.BACKEND_HOST_ADD_TANK,
                        "",
                        json
                );

                if (response.getCode() == HttpStatusCode.OK) {
                    refreshTanksList();
                    Vetor.openActionPopUp("Sucesso", "Tanque adicionado!", ActionStatusPopUp.ActionStatus.OK);
                } else if (response.getCode() == HttpStatusCode.BAD_REQUEST) {
                    // Parse error message
                    try {
                        String errorMsg = response.getContent();
                        if (errorMsg != null && errorMsg.contains("já está associado")) {
                            Vetor.openActionPopUp("Aviso", errorMsg, ActionStatusPopUp.ActionStatus.ERROR);
                        } else {
                            Vetor.openActionPopUp("Erro", "Código de tanque inválido ou já em uso", ActionStatusPopUp.ActionStatus.ERROR);
                        }
                    } catch (Exception ex) {
                        Vetor.openActionPopUp("Erro", "Falha ao adicionar tanque", ActionStatusPopUp.ActionStatus.ERROR);
                    }
                } else {
                    Vetor.openActionPopUp("Erro", "Falha ao adicionar tanque", ActionStatusPopUp.ActionStatus.ERROR);
                }
            } catch (Exception e) {
                Vetor.printNicerStackTrace(e);
                Vetor.openActionPopUp("Erro", "Erro ao adicionar tanque", ActionStatusPopUp.ActionStatus.ERROR);
            }
        });
    }

    public void onEditTankName(Tank tank) {
        TextInputDialog dialog = new TextInputDialog(tank.getName());
        dialog.setTitle("Editar Nome");
        dialog.setHeaderText("Editar nome do tanque");
        dialog.setContentText("Novo nome:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            try {
                Map<String, String> updateData = new HashMap<>();
                updateData.put("name", newName);

                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(updateData);

                String token = applicationWindow.getNetworkManager().getToken().getTokenJWT();
                HTTPResponse response = HTTPRequests.PATCH(
                        Config.BACKEND_HOST_RENAME_TANK +"/users/"+ currentEmployee.getId()+ "/tanks/" + tank.getId() + "/rename",
                        "",
                        json
                );

                if (response.getCode() == HttpStatusCode.OK) {
                    refreshTanksList();
                    Vetor.openActionPopUp("Sucesso", "Nome atualizado!", ActionStatusPopUp.ActionStatus.OK);
                } else if (response.getCode() == HttpStatusCode.NOT_FOUND) {
                    Vetor.openActionPopUp("Erro", "Tanque não encontrado", ActionStatusPopUp.ActionStatus.ERROR);
                } else {
                    Vetor.openActionPopUp("Erro", "Falha ao atualizar nome", ActionStatusPopUp.ActionStatus.ERROR);
                }
            } catch (Exception e) {
                Vetor.printNicerStackTrace(e);
                Vetor.openActionPopUp("Erro", "Erro ao atualizar nome", ActionStatusPopUp.ActionStatus.ERROR);
            }
        });
    }

    public void onEditTankKey(Tank tank) {
        TextInputDialog dialog = new TextInputDialog(tank.getKey());
        dialog.setTitle("Editar Código");
        dialog.setHeaderText("Editar código do tanque");
        dialog.setContentText("Novo código:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newKey -> {
            try {
                Map<String, String> updateData = new HashMap<>();
                updateData.put("key", newKey);

                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(updateData);

                String token = applicationWindow.getNetworkManager().getToken().getTokenJWT();

                Map<String, Object> tankUpdate = new HashMap<>();
                tankUpdate.put("key", newKey);
                tankUpdate.put("tank1", tank.getTank1());
                tankUpdate.put("tank2", tank.getTank2());

                String updateJson = gson.toJson(tankUpdate);

                HTTPResponse response = HTTPRequests.POST(
                        Config.BACKEND_HOST_CREATE_TANK, // /reservatorios/update
                        "",
                        updateJson
                );

                if (response.getCode() == HttpStatusCode.OK) {
                    refreshTanksList();
                    Vetor.openActionPopUp("Sucesso", "Código atualizado!", ActionStatusPopUp.ActionStatus.OK);
                } else {
                    Vetor.openActionPopUp("Erro", "Falha ao atualizar código", ActionStatusPopUp.ActionStatus.ERROR);
                }
            } catch (Exception e) {
                Vetor.printNicerStackTrace(e);
                Vetor.openActionPopUp("Erro", "Erro ao atualizar código", ActionStatusPopUp.ActionStatus.ERROR);
            }
        });
    }

    public void onDeleteTank(Tank tank) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir tanque");
        alert.setContentText("Tem certeza que deseja excluir o tanque " + tank.getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String token = applicationWindow.getNetworkManager().getToken().getTokenJWT();
                HTTPResponse response = HTTPRequests.DELETE(
                        Config.BACKEND_HOST_DELETE_TANK +"/users/"+ currentEmployee.getId()+  "/tanks/" + tank.getId(),
                        ""
                );

                if (response.getCode() == HttpStatusCode.OK || response.getCode() == HttpStatusCode.NO_CONTENT) {
                    refreshTanksList();
                    Vetor.openActionPopUp("Sucesso", "Tanque excluído!", ActionStatusPopUp.ActionStatus.OK);
                } else if (response.getCode() == HttpStatusCode.NOT_FOUND) {
                    Vetor.openActionPopUp("Erro", "Tanque não encontrado", ActionStatusPopUp.ActionStatus.ERROR);
                } else {
                    Vetor.openActionPopUp("Erro", "Falha ao excluir tanque", ActionStatusPopUp.ActionStatus.ERROR);
                }
            } catch (Exception e) {
                Vetor.printNicerStackTrace(e);
                Vetor.openActionPopUp("Erro", "Erro ao excluir tanque", ActionStatusPopUp.ActionStatus.ERROR);
            }
        }
    }
}