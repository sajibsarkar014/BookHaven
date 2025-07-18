package com.bookhaven;

import com.bookhaven.Controller.LoginController;
import com.bookhaven.Controller.RegistrationController;
import com.bookhaven.Service.UserService;
import com.bookhaven.Utils.NavigationController;
import com.bookhaven.View.LoginView;
import com.bookhaven.View.RegistrationView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main {
    public static void main(String[] args) {
        // It's best practice to run Swing applications on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {

            // 1. Create the top-level frame and the main panel with CardLayout
            JFrame mainFrame = new JFrame("BookHaven");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel mainPanel = new JPanel(new CardLayout());

            // 2. Create ONE instance of each View
            LoginView loginView = new LoginView();
            RegistrationView registrationView = new RegistrationView();

            // 3. Add the views to the main panel with UNIQUE String names
            mainPanel.add(loginView, "Login");
            mainPanel.add(registrationView, "Registration");

            // 4. Create ONE instance of the services and controller
            UserService userService = new UserService();
            NavigationController navigationController = new NavigationController(mainPanel);

            // Create the logic controllers, injecting the dependencies
            new LoginController(loginView, userService, navigationController);
            new RegistrationController(registrationView, userService, navigationController);

            // This listener handles the "Register" link click on the Login screen
            loginView.addRegisterLinkListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    navigationController.showRegistrationView();
                }
            });

            // 6. Finalize and show the frame
            mainFrame.getContentPane().add(mainPanel);
            mainFrame.pack();
            mainFrame.setSize(500, 600);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setVisible(true);
        });
    }
}