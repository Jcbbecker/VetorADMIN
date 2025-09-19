package com.brasens.layout;

import com.brasens.Config;
import com.brasens.Vetor;
import com.brasens.http.HTTPRequests;
import com.brasens.http.HTTPResponse;
import com.brasens.http.NetworkManager;
import com.brasens.http.Update;
import com.brasens.http.objects.HttpStatusCode;
import com.brasens.http.objects.Token;
import com.brasens.javafx.utils.NodeUtils;
import com.brasens.javafx.utils.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import lombok.Getter;
import lombok.Setter;

/**
 * This class, ApplicationWindow, represents the main plan of a graphical interface.
 * It extends JavaFX AnchorPane class and is responsible for managing the program's sub-interfaces.
 * The sub-interfaces are represented by classes that extend the Page class, which is also an extension of AnchorPane.
 * The organization of the sub-interfaces follows the MVC (Model View Controller) pattern.
 * The classes with the View function are the sub-interfaces, which extend the Page class and control the visual presentation.
 * The classes that perform the Model function extend the Controller class and manage the business logic and data.
 * The ApplicationWindow class acts as the application's main Controller.
 * -------------------------------------------------------------------------------------------------------------------------------------
 * The code includes methods for initialization, switching pages and logging in, following the MVC logic.
 * The init() method can be customized for specific application initialization.
 * The changePage() method manages the exchange between different sub-interfaces, initializing and updating their controllers.
 * The realiseLogin() method configures the interface after login, displaying the navigation bar and the top menu.
 * The ViewManager class has the function of storing the sub-interfaces that have already been initialized, as well as the main setup with navbar and topmenu.
 *-------------------------------------------------------------------------------------------------------------------------------------
 * The class uses NetworkManager and ViewManager objects to handle network operations and manage the sub-interfaces, respectively.
 * The layout structure is based on an AnchorPane containing a BorderPane, with the sub-interfaces being displayed in the center.
 * As well as the navbar on the left side of the BorderPane and the TopMenu at the top of it.
 * The interface body is identified using the BODY_ID constant.
 *-------------------------------------------------------------------------------------------------------------------------------------
 * Note: The MVC design pattern is adopted to maintain a clear separation between presentation logic, business logic and data.
 *
 * com.brasens.javafx.utils.Page
 * com.brasens.javafx.utils.Controller
 * com.brasens.layout.components.navbar.NavBar
 * com.brasens.layout.components.TopMenu
 * com.brasens.layout.ViewManager
 *
 */

@Getter
@Setter
public class ApplicationWindow extends AnchorPane {

    // Main class
    Vetor vetor;

    // Constants for interface body identification and minimum width
    public static final String BODY_ID = "body";
    public static final int MIN_WIDTH = 1280;

    // Instâncias de gerenciadores para rede e visualização
    private NetworkManager networkManager;
    private ViewManager viewManager;

    BorderPane borderPane = new BorderPane();

    public ApplicationWindow() {
        networkManager = new NetworkManager(new Token());
        viewManager = new ViewManager(this, networkManager);

        viewManager.init();
        viewManager.setup(this);

        setPrefSize(LayoutSizeManager.getResizedWidth(1280), LayoutSizeManager.getResizedHeight(800));
        getStyleClass().add("body");
        setId(BODY_ID);

        NodeUtils.setAnchors(this, Insets.EMPTY);
        AnchorPane.setBottomAnchor(borderPane, 0.0);
        AnchorPane.setLeftAnchor(borderPane, 0.0);
        AnchorPane.setRightAnchor(borderPane, 0.0);
        AnchorPane.setTopAnchor(borderPane, 0.0);

        borderPane.setMaxWidth(Double.MAX_VALUE);

        AnchorPane lFiller = new AnchorPane(); HBox.setHgrow(lFiller, Priority.ALWAYS); lFiller.setMinWidth(160.0);
        AnchorPane rFiller = new AnchorPane(); HBox.setHgrow(rFiller, Priority.ALWAYS); rFiller.setMinWidth(160.0);

        HBox topHBox = new HBox();

        double rightOffset = 160.0;

        rightOffset = LayoutSizeManager.getPageSideOffset();// + getViewManager().getNavBar().getNavBarWidth();

        rFiller.setMinWidth(rightOffset);

        HBox leftHBox = new HBox();

        borderPane.getStyleClass().add("body");

        changePage(viewManager.getDashboardView());
        BorderPane.setAlignment(viewManager.getDashboardView(), Pos.CENTER);

        Update appUpdate = new Update();
        try {
            System.out.println("try update");
            HTTPResponse statusResponse = HTTPRequests.GET(
                    Config.BACKEND_HOST_UPDATE_ENDPOINT,
                    ""
            );

            if (statusResponse.getCode() == HttpStatusCode.OK) {
                String responseData = statusResponse.getContent();
                if (responseData != null) {
                    final ObjectMapper objectMapperData = new ObjectMapper();
                    objectMapperData.registerModule(new JavaTimeModule());
                    appUpdate = objectMapperData.readValue(responseData, new TypeReference<Update>() {
                    });
                    if(!appUpdate.getVersion().equals(Config.APP_VERSION))
                        Vetor.openUpdatePopUp(appUpdate);
                }
                System.out.println(appUpdate.toString());
            }
        }catch (Exception e){
            Vetor.printNicerStackTrace(e);
        }

        getChildren().addAll(borderPane);
    }

    private Page currentPageLoaded;

    public void changePage(Page page){
        borderPane.setCenter(page); // Centraliza a página no BorderPane
        BorderPane.setAlignment(page, Pos.CENTER);

        page.getController().init();

        if(currentPageLoaded != null) {
            currentPageLoaded.getController().close();
            currentPageLoaded.getController().setUpdate(false);
        }

        page.getController().setUpdate(true);

        currentPageLoaded = page;
    }
}
