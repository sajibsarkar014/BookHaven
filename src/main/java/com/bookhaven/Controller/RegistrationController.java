package com.bookhaven.Controller;

import com.bookhaven.Exceptions.RegistrationException;
import com.bookhaven.View.RegistrationView;
import com.bookhaven.Service.UserService;
import com.bookhaven.Utils.NavigationController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;


public class RegistrationController {
    private RegistrationView view;
    private UserService authservice;
    private NavigationController transition;

    public RegistrationController(RegistrationView view, UserService authservice, NavigationController transition){
        this.view = view;
        this.transition = transition;
        this.authservice = authservice;


        // *** THIS IS THE CRITICAL FIX ***
        // Attach the handleRegistration method to the button in the view.
        this.view.addRegisterListener(this::handleRegistration);
    }

    private void attachEventListeners(){

    }

    private void handleRegistration(ActionEvent event) /*throws RegistrationException*/ {

        String firstName = view.getFirstNameField();
        String lastName = view.getLastNameField();
        char[] password = view.getPassword();
        char[] confirmPass = view.getConfirmPasswordField();
        String email = view.getEmailField();


        try{
            validateFields(firstName,lastName,password,confirmPass,email);

            if(authservice.registerUser(firstName,lastName, email, password)){
                JOptionPane.showMessageDialog(view, "Registration Successful! Please log in.");

                // After successful registration, go back to the login view.
                transition.showLoginView();
                view.clearForm(); // Clear fields for next time
            } else {

                showError("Registration failed. The email might already be in use.");
            }


        } catch (RegistrationException e){
            showError(e.getMessage());
        } finally {
            clearPasswordData(password, confirmPass);
        }

    }

    private void clearPasswordData(char[] password, char[] confirmPass) {
        Arrays.fill(password,'\0');
        Arrays.fill(confirmPass,'\0');
        view.clearPasswordFields();
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(
                        view,
                        message,
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE
                )
        );
    }

    private void validateFields (String firstName, String lastName, char[] passWord, char[] confirmPass, String email)
        throws RegistrationException{


        if(firstName.isEmpty() || lastName.isEmpty()){
            throw new RegistrationException("Fill the name fields!");
        }

        if(email.isEmpty()){
            throw new RegistrationException("Fill the email!");
        }

        if(!isValidEmail(email)){
            throw new RegistrationException("Enter a valid email");
        }

        if(passWord.length == 0){
            throw new RegistrationException("Password cannot be empty");
        }
        if(passWord.length <8){
            throw  new RegistrationException("Password should have minimum 8 characters");
        }

        String passwordStr = new String(passWord);

        // Check for valid characters (no spaces, no non-printables)
        if (!passwordStr.matches("^[\\p{Print}&&[^\\s]]+$")) {
            throw new RegistrationException("Password contains invalid characters");
        }
        // Combined strength validation
        if (!(passwordStr.matches(".*[a-z].*") &&
                passwordStr.matches(".*[A-Z].*") &&
                passwordStr.matches(".*\\d.*") &&
                passwordStr.matches(".*[^a-zA-Z0-9].*"))) {

            throw new RegistrationException("Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character");
        }

        if(!Arrays.equals(passWord,confirmPass)){
            throw new RegistrationException("Passwords do not match");
        }
    }

    private boolean isValidEmail(String email) {
        // Simple regex for demonstration - use proper validation in production
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }




}
