package wvsg.informatik.planner.views.dailyList;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import wvsg.informatik.planner.components.bulletlist.BulletList;
import wvsg.informatik.planner.data.entity.BulletEntity;
import wvsg.informatik.planner.data.entity.ListEntity;
import wvsg.informatik.planner.data.repository.BulletRepository;
import wvsg.informatik.planner.data.repository.ListRepository;
import wvsg.informatik.planner.data.repository.UserRepository;
import wvsg.informatik.planner.data.service.DataService;
import wvsg.informatik.planner.views.MainAppLayout;

import java.util.List;

/**
 * Anzeige von täglichen Listen, analog zur ListView
 * Mit Ausnahme, dass Listen, die nicht existieren erstellt werden
 */
@PageTitle("Daily list | plannly")
@Route(value = "/daily", layout = MainAppLayout.class)
@PermitAll
public class DailyListView extends VerticalLayout implements HasUrlParameter<String>{

    String parameterValue;

    DataService dataService;

    @Autowired
    BulletRepository bulletRepository;

    @Autowired
    ListRepository listRepository;

    @Autowired
    UserRepository userRepository;

    public DailyListView(DataService dataService) {
        this.dataService = dataService;
    }

    public void initialize() {
        this.removeAll();

        ListEntity list = dataService.getDailyListAndCreateIfNotExists(parameterValue);
        List<BulletEntity> bullets = bulletRepository.findBulletsByList(list);

        add(new BulletList(bulletRepository, dataService, listRepository, userRepository, list, bullets));
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        this.parameterValue = parameter;
        initialize();
    }
}