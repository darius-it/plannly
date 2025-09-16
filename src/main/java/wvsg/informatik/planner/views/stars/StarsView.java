package wvsg.informatik.planner.views.stars;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import wvsg.informatik.planner.data.entity.BulletEntity;
import wvsg.informatik.planner.data.entity.UserEntity;
import wvsg.informatik.planner.data.repository.BulletRepository;
import wvsg.informatik.planner.data.service.DataService;
import wvsg.informatik.planner.views.MainAppLayout;

import java.util.List;

/**
 * Seite zur Anzeige von Stars, die als Lesezeichen für Bullets dienen
 */
@PageTitle("Stars | plannly")
@Route(value = "/stars", layout = MainAppLayout.class)
@PermitAll
public class StarsView extends VerticalLayout {
    BulletRepository bulletRepository;
    DataService dataService;

    /**
     * Standard-Konstruktor mit Rendering der grundlegenden Elemente
     * @param dataService
     *      Service zur Verwaltung der Daten (Bullets, etc.) in der Datenbank, mit DI (Dependency Injection) eingefügt
     * @param bulletRepository
     *      Repository zum Abfragen von Daten zu einzelnen Bulletpoints aus der Datenbank, mit DI eingefügt
     */
    public StarsView(DataService dataService, BulletRepository bulletRepository) {
        this.bulletRepository = bulletRepository;
        this.dataService = dataService;

        // Whitespace an Seiten hinzufügen
        this.addClassNames(LumoUtility.Padding.Top.MEDIUM, LumoUtility.Padding.Left.LARGE);

        // Titel erstellen und Größe etc. anpassen
        H2 titleHeader = new H2("Stars ✨");
        titleHeader.addClassNames(LumoUtility.FontSize.XXXLARGE, LumoUtility.Margin.Bottom.SMALL);
        add(titleHeader);

        // UserEntity/Objekt des aktuell eingeloggten Nutzers erhalten
        UserEntity currentUser = dataService.getCurrentlyAuthenticatedUserEntity();

        // Alle Stars des Nutzers erhalten und mit Link zur Liste anzeigen
        List<BulletEntity> bullets = bulletRepository.getAllStarsOfUser(currentUser);
        for (BulletEntity bullet : bullets) {
            // Anzeige eines Stars formattieren und anpassen
            HorizontalLayout starText = new HorizontalLayout(new Span("•  "), new Span(bullet.getContent()));
            starText.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.MEDIUM);

            // Link zur Liste, in der sich der Bulletpoint befindet
            Anchor listLink = new Anchor(String.format("/list/%s", bullet.getList().getId().toString()), new Icon(VaadinIcon.EXTERNAL_LINK));
            listLink.addClassName(LumoUtility.Margin.Left.MEDIUM);

            HorizontalLayout starLayout = new HorizontalLayout(starText, listLink);
            add(starLayout);
        }
    }
}
