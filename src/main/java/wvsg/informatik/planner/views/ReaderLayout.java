package wvsg.informatik.planner.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * Layout für die Reader-Ansicht, die eine vereinfachte Version des Hauptlayouts ist.
 * Dieses Layout wird nur dann angezeigt, wenn ein Benutzer nicht angemeldet ist und er eine öffentliche Liste aufrufen möchte.
 */
public class ReaderLayout extends AppLayout {

    /**
     * Standard-Konstruktor mit Rendering der grundlegenden Elemente
     */
    public ReaderLayout() {
        addToNavbar(getNavbarLayout());
    }

    /**
     * Erstellt das Layout für die Kombination von Logo als Bild und Text
     * @return logoLayout
     *      finales Layout
     */
    HorizontalLayout getLogoLayout() {
        // Initialisierung des Layouts mit Styling
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.getStyle().set("gap", "0.25rem");

        // Titel/App Name mit Styling
        H1 title = new H1("plannly");
        title.setClassName(LumoUtility.Margin.NONE);
        title.setClassName(LumoUtility.FontSize.XLARGE);

        // App logo mit styling
        Image logoIcon = new Image("images/plannly_logo.png", "plannly logo");
        logoIcon.getStyle().set("width", "48px");
        logoIcon.getStyle().set("height", "48px");

        logoLayout.add(logoIcon);
        logoLayout.add(title);
        return logoLayout;
    }

    /**
     * Erstellt das Layout für die obere Navigationsleiste, die für einige Elemente der Navigation verwendet wird
     * @return navbarLayout
     *      Das finale Layout der oberen Navigationsleiste (Navbar), welches in das finale Layout eingefügt werden soll
     */
    HorizontalLayout getNavbarLayout() {
        HorizontalLayout logoLayout = getLogoLayout(); // Logo mit Text erstellen

        // Knöpfe zur Navigation mit Click-Listener
        HorizontalLayout buttonsLayout = new HorizontalLayout(
                new Button("Log in", e -> UI.getCurrent().navigate("/login")),
                new Button("Sign up", e -> UI.getCurrent().navigate("/signup"))
        );

        // Erstellen des finalen Layouts und Anordnung durch Styling (CSS)
        HorizontalLayout navbarLayout = new HorizontalLayout(logoLayout, buttonsLayout);
        navbarLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        navbarLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        navbarLayout.getStyle().set("width", "100%");
        navbarLayout.getStyle().set("margin", " 0 1rem");

        return navbarLayout;
    }
}
