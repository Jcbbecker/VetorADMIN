package com.brasens.layout;

import com.brasens.http.NetworkManager;
import com.brasens.layout.view.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewManager {

    private ClientRegisterView clientRegisterView;

    public ViewManager(ApplicationWindow applicationWindow, NetworkManager networkManager) {
        clientRegisterView = new ClientRegisterView(applicationWindow, networkManager);
    }

    public void setup(ApplicationWindow applicationWindow){

    }
}
