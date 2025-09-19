package com.brasens.layout.controller;

import com.brasens.javafx.utils.Controller;
import com.brasens.layout.ApplicationWindow;
import com.brasens.layout.view.DashboardView;

public class DashboardController extends Controller {

    private DashboardView dashboardView;
    private ApplicationWindow applicationWindow;

    public DashboardController(ApplicationWindow applicationWindow) {
        this.applicationWindow = applicationWindow;
    }

    @Override
    public void init() {
        dashboardView = applicationWindow.getViewManager().getDashboardView();
        System.out.println("Dashboard initialized");
    }

    @Override
    public void close() {
        System.out.println("Dashboard closed");
    }

    @Override
    public void update() {
        // Não precisa de atualização periódica para o dashboard
    }

    public void onAddUserClick() {
        System.out.println("Navigating to Client Register View");
        applicationWindow.changePage(applicationWindow.getViewManager().getClientRegisterView());
    }

    public void onManageUsersClick() {
        System.out.println("Navigating to Users Management View");
        applicationWindow.changePage(applicationWindow.getViewManager().getUsersManagementView());
    }
}