
package bw.ac.cse202.banking;

import bw.ac.cse202.banking.controller.AccountController;
import bw.ac.cse202.banking.view.AccountView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankingApp extends Application {

    @Override
    public void start(Stage stage) {

        AccountView accountView = new AccountView();

        // Connect the view to its controller
        new AccountController(accountView);

        Scene scene = new Scene(accountView, 1000, 700);

        stage.setTitle("Banking Management System");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setMaximized(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
