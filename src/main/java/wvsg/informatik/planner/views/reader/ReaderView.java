package wvsg.informatik.planner.views.reader;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import wvsg.informatik.planner.components.bulletlist.BulletList;
import wvsg.informatik.planner.data.entity.ListEntity;
import wvsg.informatik.planner.data.repository.BulletRepository;
import wvsg.informatik.planner.data.repository.ListRepository;
import wvsg.informatik.planner.data.repository.UserRepository;
import wvsg.informatik.planner.data.service.DataService;
import wvsg.informatik.planner.views.ReaderLayout;

import java.util.Optional;
import java.util.UUID;

/**
 * Seite zum Anzeigen von öffentlichen Listen ohne Benutzerkonto
 * Restliche Logik analog zur ListView, nur mit ReaderLayout als Navigation
 */
@PageTitle("List | plannly")
@Route(value = "/reader", layout = ReaderLayout.class)
@AnonymousAllowed
public class ReaderView extends VerticalLayout implements HasUrlParameter<String> {

    @Autowired
    private ListRepository listRepository;

    @Autowired
    private BulletRepository bulletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataService dataService;

    private String parameterValue;

    public ReaderView(DataService dataService){
        this.dataService = dataService;
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        this.parameterValue = parameter;
        initialize();
    }

    private void initialize() {
        this.removeAll();

        if (parameterValue == null) {
            addListCouldNotBeFoundMessage();
            return;
        }

        UUID listId;
        try {
            listId = UUID.fromString(parameterValue);
        }
        catch (IllegalArgumentException exception) {
            add(new H1("ERROR: Not a valid list ID"));
            return;
        }

        Optional<ListEntity> currentList = listRepository.findById(listId);
        if (currentList.isEmpty()) {
            addListCouldNotBeFoundMessage();
            return;
        }

        boolean listIsPublic = currentList.get().isPublic();

        if(!listIsPublic){
            addListCouldNotBeFoundMessage();
            return;
        }

        BulletList bulletList = new BulletList(
                bulletRepository, dataService, listRepository, userRepository,
                currentList.get(), bulletRepository.findBulletsByList(currentList.get()), true
        );
        add(bulletList);
    }

    public void addListCouldNotBeFoundMessage(){
        add(new H1("ERROR: List could not be found"));
    }
}
