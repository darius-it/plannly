package wvsg.informatik.planner.views.signup;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import wvsg.informatik.planner.components.signup.RegistrationForm;
import wvsg.informatik.planner.components.signup.RegistrationFormBinder;
import wvsg.informatik.planner.data.service.AuthService;

/**
 * Seite zum Erstellen eines Benutzerkontos
 */
@Route("signup")
@PageTitle("Sign up | plannly")
@AnonymousAllowed
public class SignupView extends VerticalLayout {

    /**
     * Standard-Konstruktor mit Rendering der grundlegenden Elemente
     * @param authService
     *      Durch Dependency Injection eingefügter Service zur Verwaltung von Nuterdaten
     */
    public SignupView(AuthService authService) {
        // Hauptkomponente erstellen und zentrieren
        RegistrationForm registrationForm = new RegistrationForm();
        setHorizontalComponentAlignment(Alignment.CENTER, registrationForm);

        // Link zur Login-Seite
        Anchor loginLink = new Anchor("/login", "Already have an account? Log in");
        setHorizontalComponentAlignment(Alignment.CENTER, loginLink);

        add(registrationForm, loginLink);

        // Zur Benutzeroberfläche des Formulars zur Erstellung eines Benutzers Logik anfügen ("bind")
        RegistrationFormBinder registrationFormBinder = new RegistrationFormBinder(registrationForm, authService);
        registrationFormBinder.addBindingAndValidation();
    }
}