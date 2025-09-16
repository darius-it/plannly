package wvsg.informatik.planner.components.signup;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import java.util.stream.Stream;

/**
 * Das RegistrationForm ist ein benutzerdefiniertes FormLayout zur Registrierung von neuen Benutzern.
 */
public class RegistrationForm extends FormLayout {

    private PasswordField password;
    private PasswordField passwordConfirm;

    private Span errorMessageField;

    private Button submitButton;

    /**
     * Konstruktor für das RegistrationForm.
     * Initialisiert das Formular für die Registrierung.
     */
    public RegistrationForm() {
        H1 title = new H1("Create an account");
        TextField username = new TextField("Username");
        EmailField email = new EmailField("Email");

        password = new PasswordField("Password");
        passwordConfirm = new PasswordField("Confirm password");

        setRequiredIndicatorVisible(username, email, password,
                passwordConfirm);

        errorMessageField = new Span();

        submitButton = new Button("Create account");
        submitButton.getElement().getStyle().set("margin-top", "12px");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.addClickShortcut(Key.ENTER);

        add(title, username, email, password,
                passwordConfirm, errorMessageField,
                submitButton);

        // Maximalbreite des Formulars
        setMaxWidth("500px");

        // Ermöglicht ein responsives Formular-Layout.
        // Für Gerätbreiten von 0-490px haben wir eine Spalte.
        // Ansonsten haben wir zwei Spalten.
        setResponsiveSteps(
                new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP),
                new ResponsiveStep("490px", 2, ResponsiveStep.LabelsPosition.TOP));

        // Diese Komponenten nehmen immer die volle Breite ein
        setColspan(title, 2);
        setColspan(username, 2);
        setColspan(email, 2);
        setColspan(errorMessageField, 2);
        setColspan(submitButton, 2);
    }

    /**
     * Gibt das Passwortfeld zurück.
     *
     * @return Das Passwortfeld
     */
    public PasswordField getPasswordField() { return password; }

    /**
     * Gibt das Passwortbestätigungsfeld zurück.
     *
     * @return Das Passwortbestätigungsfeld
     */
    public PasswordField getPasswordConfirmField() { return passwordConfirm; }

    /**
     * Gibt das Fehlermeldungsfeld zurück.
     *
     * @return Das Fehlermeldungsfeld
     */
    public Span getErrorMessageField() { return errorMessageField; }

    /**
     * Gibt den Button zum Absenden des Formulars zurück.
     *
     * @return Der Absende-Button
     */
    public Button getSubmitButton() { return submitButton; }

    /**
     * Setzt das Pflichtindikator-Sichtbarkeit für die angegebenen Komponenten.
     *
     * @param components Die Komponenten, für die der Pflichtindikator sichtbar gemacht werden soll
     */
    private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
        Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
    }
}
