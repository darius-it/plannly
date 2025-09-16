package wvsg.informatik.planner.views.list;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import wvsg.informatik.planner.components.bulletlist.BulletList;
import wvsg.informatik.planner.data.entity.ListEntity;
import wvsg.informatik.planner.data.repository.BulletRepository;
import wvsg.informatik.planner.data.repository.ListRepository;
import wvsg.informatik.planner.data.repository.UserRepository;
import wvsg.informatik.planner.data.service.DataService;
import wvsg.informatik.planner.views.MainAppLayout;

import java.util.Optional;
import java.util.UUID;

/**
 * Ansicht für die Anzeige von Listen von eingeloggten Nutzern, die die nötigen Zugriffsrechte besitzen
 */
@PageTitle("List | plannly")
@Route(value = "/list", layout = MainAppLayout.class)
@PermitAll
public class ListView extends VerticalLayout implements HasUrlParameter<String> {

    @Autowired
    private ListRepository listRepository;

    @Autowired
    private BulletRepository bulletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataService dataService;

    private String parameterValue;

    /**
     * Konstruktor der Klasse "ListView".
     * @param dataService
     *      der DataService für den Zugriff auf die Daten
     */
    public ListView(DataService dataService){
        this.dataService = dataService;
    }

    /**
     * Methode, um den Wert des URL-Parameters zu setzen und die Initialisierung durchzuführen.
     * @param event
     *      das BeforeEvent, das Informationen über die Navigation enthält
     * @param parameter
     *      der Wert des URL-Parameters
     */
    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        this.parameterValue = parameter;
        initialize();
    }

    /**
     * Methode zur Initialisierung der Ansicht
     * (Anzeige der BulletList Komponente für eine Liste oder Anzeige einer Fehlermeldung)
     */
    private void initialize() {
        this.removeAll(); // Seite zuerst löschen, wenn sie neu aufgerufen wird, um Duplikate zu verhindern

        // Wenn kein Parameter in Link enthalten, Fehler ausgeben
        if (parameterValue == null) {
            addListCouldNotBeFoundMessage();
            return;
        }

        // Fehler ausgeben, wenn Parameter keine UUID ist
        UUID listId;
        try {
            listId = UUID.fromString(parameterValue);
        }
        catch (IllegalArgumentException exception) {
            add(new H1("ERROR: Not a valid list ID"));
            return;
        }

        // Fehler ausgeben, wenn liste nicht existiert oder der Nutzer keine Zugriffsrechte hat
        Optional<ListEntity> currentList = listRepository.findById(listId);
        if (currentList.isEmpty()) {
            addListCouldNotBeFoundMessage();
            return;
        }

        boolean userOwnsList = currentList.get().getOwnerId().equals(dataService.getCurrentlyAuthenticatedUserEntity().getId());
        if(!userOwnsList){
            addListCouldNotBeFoundMessage();
            return;
        }

        // BulletList Komponente mit Listeninhalt anzeigen
        BulletList bulletList = new BulletList(
                bulletRepository, dataService, listRepository, userRepository,
                currentList.get(), bulletRepository.findBulletsByList(currentList.get())
        );
        add(bulletList);
    }

    /**
     * Methode, die zum Anzeigen einer Fehlermeldung verwendet wird, wenn die angegebene Liste nicht existiert
     * oder der Nutzer nicht die Rechte besitzt, sie zu öffnen
     */
    public void addListCouldNotBeFoundMessage(){
        add(new H1("ERROR: List could not be found"));
    }
}
