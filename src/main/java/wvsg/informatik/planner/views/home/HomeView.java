package wvsg.informatik.planner.views.home;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.userdetails.UserDetails;

import wvsg.informatik.planner.data.service.DataService;
import wvsg.informatik.planner.security.SecurityService;
import wvsg.informatik.planner.views.MainAppLayout;

/**
 * Startseite der App, momentan leer
 * Zukunftspotential: "Dashboard" als Überblick der wichtigsten Funktionen, gespeicherte Listen, geteilte Listen, usw.
 */
@PageTitle("Home | plannly")
@Route(value = "/", layout = MainAppLayout.class)
@RouteAlias(value = "/home")
@PermitAll
public class HomeView extends HorizontalLayout {

    DataService dataService;

    SecurityService securityService;

    /**
     * Standard-Konstruktor mit Rendering der grundlegenden Elemente
     * alle Übergabeparameter mit Dependency Injection eingefügt
     */
    public HomeView(SecurityService securityService, DataService dataService) {
        this.securityService = securityService;
        this.dataService = dataService;
        UserDetails user = securityService.getAuthenticatedUser();
        setMargin(true);

        this.addClassNames(LumoUtility.Padding.Top.MEDIUM, LumoUtility.Padding.Left.LARGE, LumoUtility.Margin.NONE);
        H2 titleHeader = new H2("Welcome back, " + user.getUsername());
        titleHeader.addClassNames(LumoUtility.FontSize.XXXLARGE);
        add(titleHeader);
    }
}
