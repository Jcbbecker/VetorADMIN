package com.brasens.javafx.utils;

import com.brasens.http.NetworkManager;
import com.brasens.layout.ApplicationWindow;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public abstract class Page extends AnchorPane {

    private ApplicationWindow applicationWindow;
    private NetworkManager networkManager;

    protected Controller controller;
    protected String CSS;

    public Page(ApplicationWindow applicationWindow, NetworkManager networkManager, String CSS){
        this.applicationWindow = applicationWindow;
        this.networkManager = networkManager;
        this.CSS = getClass().getResource("/mspm/pages/DashboardCSS.css").toString();

        setPrefSize(1080, 725);
        getStyleClass().add("body");
        getStylesheets().add(this.CSS);
    }
}
