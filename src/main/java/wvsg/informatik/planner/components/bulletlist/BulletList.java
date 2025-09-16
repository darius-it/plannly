package wvsg.informatik.planner.components.bulletlist;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import wvsg.informatik.planner.data.entity.BulletEntity;
import wvsg.informatik.planner.data.entity.ListEntity;
import wvsg.informatik.planner.data.repository.BulletRepository;
import wvsg.informatik.planner.data.repository.ListRepository;
import wvsg.informatik.planner.data.repository.UserRepository;
import wvsg.informatik.planner.data.service.DataService;
import wvsg.informatik.planner.security.SecurityService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Komponente für eine Bullet-Liste.
 */
public class BulletList extends VerticalLayout {

    @Autowired
    BulletRepository bulletRepository;

    @Autowired
    DataService dataService;

    @Autowired
    ListRepository listRepository;

    @Autowired
    UserRepository userRepository;

    ListEntity listEntity;

    private String title;
    public String getTitle() {
        return title;
    }

    private UUID id;
    private boolean isDailyList;
    private Date createdAt;
    public List<BulletPoint> bullets;

    private boolean readOnlyList;
    public boolean getReadOnlyList(){
        return readOnlyList;
    }

    private H2 titleHeader;
    private VerticalLayout bulletContainer;
    private BulletPointCreator bulletCreator;
    public BulletEditor bulletEditor;

    /**
     * Konstruktor für die Bullet-Liste.
     *
     * @param bulletRepository Das Repository für Bullet-Entitäten.
     * @param dataService Der DataService.
     * @param listRepository Das Repository für List-Entitäten.
     * @param userRepository Das Repository für Benutzer.
     * @param listEntity Die List-Entität.
     * @param bulletEntities Eine Liste von Bullet-Entitäten.
     * @param readOnlyList Gibt an, ob die Liste schreibgeschützt ist.
     */
    public BulletList(BulletRepository bulletRepository, DataService dataService, ListRepository listRepository, UserRepository userRepository, ListEntity listEntity, List<BulletEntity> bulletEntities, boolean readOnlyList){
        this.bulletRepository = bulletRepository;
        this.dataService = dataService;
        this.listRepository = listRepository;
        this.userRepository = userRepository;
        this.listEntity = listEntity;

        this.readOnlyList = readOnlyList;

        titleHeader = new H2("");
        titleHeader.addClassNames(LumoUtility.FontSize.XXXLARGE, LumoUtility.Margin.Bottom.SMALL);
        HorizontalLayout headerLayout = new HorizontalLayout(titleHeader);
        if(!getReadOnlyList() && !listEntity.isDailyList()){
            headerLayout.add(new ListOptionsMenu(listEntity, listRepository, bulletRepository));
        }
        if (listEntity.isDailyList()) {
            DatePicker gotoDate = new DatePicker();
            gotoDate.addValueChangeListener(event -> UI.getCurrent().navigate("/daily/" + event.getValue().toString()));
            gotoDate.setPlaceholder(LocalDate.parse(listEntity.getTitle()).format(DateTimeFormatter.ofPattern("MMMM yyyy")));
            gotoDate.setWeekNumbersVisible(true);
            gotoDate.getStyle().set("max-width", "175px");
            gotoDate.addClassName("dailylist-dateselect");

            HorizontalLayout dateNavigationButtons = new HorizontalLayout(
                    new Button(VaadinIcon.ANGLE_LEFT.create(), e -> UI.getCurrent().navigate("/daily/" + LocalDate.parse(listEntity.getTitle()).minusDays(1))),
                    gotoDate,
                    new Button(VaadinIcon.ANGLE_RIGHT.create(), e -> UI.getCurrent().navigate("/daily/" + LocalDate.parse(listEntity.getTitle()).plusDays(1)))
            );
            dateNavigationButtons.addClassName(LumoUtility.Gap.XSMALL);

            headerLayout.add(dateNavigationButtons);
        }
        headerLayout.addClassNames(LumoUtility.Width.FULL,
                LumoUtility.JustifyContent.BETWEEN, LumoUtility.AlignItems.CENTER, LumoUtility.Padding.NONE);
        add(headerLayout);

        this.bulletContainer = new VerticalLayout();
        this.bulletContainer.addClassName(LumoUtility.Padding.NONE);
        this.bulletContainer.getStyle().set("gap", "0");
        add(bulletContainer);

        if(!getReadOnlyList()){
            bulletCreator = new BulletPointCreator(new BulletEntity("", "Dash", 0, 0, false, listEntity.getOwner(), listEntity), this);
            add(bulletCreator);
        }

        bullets = new ArrayList<>();

        if(!getReadOnlyList()) {
            bulletEditor = new BulletEditor(this);
            add(bulletEditor);
        }

        this.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Gap.XSMALL);
        this.getStyle().set("padding-left", "1.25rem");
        this.getStyle().set("width", "60%");
        this.getStyle().set("margin", "0 auto");
        this.addClassName("bulletlist-container");

        setData(listEntity, bulletEntities);
    }

    /**
     * Konstruktor für die Bullet-Liste.
     *
     * @param bulletRepository Das Repository für Bullet-Entitäten.
     * @param dataService Der DataService.
     * @param listRepository Das Repository für List-Entitäten.
     * @param userRepository Das Repository für Benutzer.
     * @param listEntity Die List-Entität.
     * @param bulletEntities Eine Liste von Bullet-Entitäten.
     */
    public BulletList(BulletRepository bulletRepository, DataService dataService, ListRepository listRepository, UserRepository userRepository, ListEntity listEntity, List<BulletEntity> bulletEntities){
        this(bulletRepository, dataService, listRepository, userRepository, listEntity, bulletEntities, false);
    }

    //region Load data from database

    /**
     * Setzt die Daten der Bullet-Liste aus der Datenbank.
     *
     * @param listEntity Die List-Entität.
     * @param bulletEntities Eine Liste von Bullet-Entitäten.
     */
    public void setData(ListEntity listEntity, List<BulletEntity> bulletEntities){

        if (listEntity.isDailyList()) {
            this.title = LocalDate.parse(listEntity.getTitle()).format(DateTimeFormatter.ofPattern("EEEE, dd. MMMM"));
        }
        else {
            this.title = listEntity.getTitle();
        }

        this.id = listEntity.getId();
        this.isDailyList = listEntity.isDailyList();
        this.createdAt = listEntity.getCreatedAt();

        setBullets(bulletEntities);

        redrawData();
    }

    /**
     * Setzt die Daten der Bullet-Liste aus der Datenbank.
     *
     * @param listEntity Die List-Entität.
     */
    public void setData(ListEntity listEntity){

        this.title = listEntity.getTitle();
        this.id = listEntity.getId();
        this.isDailyList = listEntity.isDailyList();
        this.createdAt = listEntity.getCreatedAt();

        redrawData();
    }

    /**
     * Setzt die Bullet-Entitäten der Bullet-Liste.
     *
     * @param bulletEntities Eine Liste von Bullet-Entitäten.
     */
    public void setBullets(List<BulletEntity> bulletEntities){
        bullets.clear();

        for (BulletEntity bulletEntity:bulletEntities) {
            BulletPoint b = new BulletPoint(bulletEntity, this);
            bullets.add(b);
        }
        sortBulletPoints();
        fixPositions();
        redrawBullets();
    }
    //endregion

    //region updating components value

    /**
     * Aktualisiert die Anzeige der Daten.
     */
    public void redrawData(){
        titleHeader.setText(this.title);
    }

    /**
     * Aktualisiert die Anzeige der Bullet-Elemente.
     */
    public void redrawBullets(){
        bulletContainer.removeAll();

        for (BulletPoint b : bullets) {
            bulletContainer.add(b);
        }
    }
    //endregion

    /**
     * Fügt ein neues Bullet-Element hinzu.
     *
     * @param bulletEntity Das Bullet-Element.
     */
    public void addBulletPoint(BulletEntity bulletEntity){
        BulletPoint b = new BulletPoint(bulletEntity, this);

        bullets.add(b);
        bulletContainer.add(b);

        bulletRepository.save(bulletEntity);
    }

    /**
     * Aktualisiert die Positionen der Bullet-Elemente und zeichnet sie neu.
     */
    public void redrawPositions(){
        sortBulletPoints();
        redrawBullets();
    }

    /**
     * Sortiert die Bullet-Elemente nach ihren Positionen.
     */
    public void sortBulletPoints(){
        Collections.sort(bullets);
    }

    /**
     * Korrigiert die Positionen der Bullet-Elemente.
     */
    public void fixPositions(){
        if(bullets.size() > 1){
            for(int i = 0; i<bullets.size(); i++){
                BulletPoint current = bullets.get(i);
                current.setPositionWithoutRedraw(i);
            }
        }
    }
}
