package wvsg.informatik.planner.views.login;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * Einfache View zur Anmeldung von Benutzern, die in der Datenbank gespeichert sind
 */
@Route("login")
@PageTitle("Login | plannly")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    /** Vaadin Komponente für ein Login-Formular/Login Eingabemasken */
    private final LoginForm login = new LoginForm();

    /**
     * Standard-Konstruktor mit Rendering der grundlegenden Elemente
     */
    public LoginView(){
        addClassName("login-view");
        setSizeFull();

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");

        Anchor signupLink = new Anchor("/signup", "Don't have an account yet? Sign up");

        add(new H1("Welcome to plannly"), login, signupLink);
    }

    /**
     * Event, welches vor einer Navigation auf diese oder eine andere Seite ausgeführt wird
     * Wenn ein Fehler bei der Anmeldung auftritt, wird diese Methode aufgerufen und eine Fehlermeldung kann angezeigt werden
     * @param beforeEnterEvent
     *            Event mit Details
     */
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // Nutzer über Fehler im Authentication-Prozess informieren
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}