package com.brasens.layout;

import com.brasens.http.NetworkManager;
import com.brasens.layout.view.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewManager {

    private ClientRegisterView clientRegisterView;

    private DashboardView dashboardView;
    private UsersManagementView usersManagementView;
    private UserTanksView userTanksView;

    private ApplicationWindow applicationWindow;
    private NetworkManager networkManager;

    public ViewManager(ApplicationWindow applicationWindow, NetworkManager networkManager) {
        this.applicationWindow = applicationWindow;
        this.networkManager = networkManager;
    }

    public void init() {
        // Inicializar todas as views
        clientRegisterView = new ClientRegisterView(applicationWindow, networkManager);
        dashboardView = new DashboardView(applicationWindow, networkManager);
        usersManagementView = new UsersManagementView(applicationWindow, networkManager);
        userTanksView = new UserTanksView(applicationWindow, networkManager);
    }

    public void setup(ApplicationWindow applicationWindow) {
        // Configuração adicional se necessária
        // Por exemplo, configurar navbar e topmenu se existirem
        // navBar = new NavBar(applicationWindow);
        // topMenu = new TopMenu(applicationWindow);
    }
}