package wvsg.informatik.planner.components.signup;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import wvsg.informatik.planner.data.entity.UserEntity;
import wvsg.informatik.planner.data.service.AuthService;

/**
 * Der RegistrationFormBinder bindet die Daten und die Validierungslogik an das RegistrationForm.
 */
public class RegistrationFormBinder {

    private RegistrationForm registrationForm;

    /**
     * Flag für die Deaktivierung der ersten Ausführung der Passwortvalidierung.
     */
    private boolean enablePasswordValidation;

    private AuthService authService;

    public RegistrationFormBinder(RegistrationForm registrationForm, AuthService authService) {
        this.registrationForm = registrationForm;
        this.authService = authService;
    }

    /**
     * Methode zum Hinzufügen der Datenbindung und Validierungslogik an das RegistrationForm.
     */
    public void addBindingAndValidation() {
        BeanValidationBinder<UserEntity> binder = new BeanValidationBinder<>(UserEntity.class);
        binder.bindInstanceFields(registrationForm);

        // Ein benutzerdefinierter Validator für die Passwortfelder
        binder.forField(registrationForm.getPasswordField())
                .withValidator(this::passwordValidator).bind("password");

        // Das zweite Passwortfeld ist nicht mit dem Binder verbunden, aber wir möchten,
        // dass der Binder den Passwort-Validator erneut überprüft, wenn sich der Wert des Feldes ändert.
        // Der einfachste Weg ist, das manuell zu tun.
        registrationForm.getPasswordConfirmField().addValueChangeListener(e -> {
            // Der Benutzer hat das zweite Feld geändert, jetzt können wir validieren und Fehler anzeigen.
            // Siehe passwordValidator() für die Verwendung dieser Flag.
            enablePasswordValidation = true;

            binder.validate();
        });

        // Das Label, in dem die bean-level Fehlermeldungen angezeigt werden
        binder.setStatusLabel(registrationForm.getErrorMessageField());

        // Und schließlich der Absende-Button
        registrationForm.getSubmitButton().addClickListener(event -> {
            try {
                // Leeres Bean erstellen, um die Details darin zu speichern
                UserEntity user = new UserEntity();

                // Validator ausführen und die Werte in das Bean schreiben
                binder.writeBean(user);

                // Erfolgsmeldung anzeigen, wenn alles gut gelaufen ist
                initialValidationSuccess(user);
            } catch (ValidationException exception) {
                // Validierungsfehler sind bereits für jedes Feld sichtbar,
                // und bean-level Fehler werden im Statuslabel angezeigt.
                // Hier könnten zusätzliche Nachrichten angezeigt, Protokolle erstellt usw. werden.
            }
        });
    }

    /**
     * Methode zum Validieren; Voraussetzunge :
     * 1) Das Passwort ist mindestens 8 Zeichen lang
     * 2) Die Werte stimmen in beiden Feldern überein
     */
    private ValidationResult passwordValidator(String pass1, ValueContext ctx) {
        /*
         * möglicher TODO: Komplexität des Passworts prüfen, nicht nur die Länge
         */

        if (pass1 == null || pass1.length() < 8) {
            return ValidationResult.error("Password has to be at least 8 characters");
        }

        if (!enablePasswordValidation) {
            // Der Benutzer hat das Feld noch nicht besucht, daher noch keine Validierung, sondern erst beim nächsten Mal.
            enablePasswordValidation = true;
            return ValidationResult.ok();
        }

        String pass2 = registrationForm.getPasswordConfirmField().getValue();

        if (pass1 != null && pass1.equals(pass2)) {
            return ValidationResult.ok();
        }

        return ValidationResult.error("Passwords do not match");
    }

    /**
     * Diese Methode wird aufgerufen, wenn die anfängliche Formularvalidierung erfolgreich war.
     */
    private void initialValidationSuccess(UserEntity userBean) {
        System.out.printf("%s %s %s POG!!!!!!!!!!! %n", userBean.getUsername(), userBean.getEmail(), userBean.getPassword());

        try {
            authService.registerUser(userBean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Notification notification =
                Notification.show("Your account has been created, welcome " + userBean.getUsername());
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        UI.getCurrent().navigate("/login");
    }

}
