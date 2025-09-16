package wvsg.informatik.planner.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;
import wvsg.informatik.planner.data.entity.ListEntity;
import wvsg.informatik.planner.data.entity.UserEntity;
import wvsg.informatik.planner.data.repository.ListRepository;
import wvsg.informatik.planner.data.repository.UserRepository;
import wvsg.informatik.planner.data.service.DataService;
import wvsg.informatik.planner.security.SecurityService;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Hauptlayout der App, welches die notwendige Navigation der App (obere Navigationsleiste, Seitenleiste beinhaltet)
 */
public class MainAppLayout extends AppLayout {

    SecurityService securityService;

    ListRepository listRepository;

    UserRepository userRepository;

    DataService dataService;

    private String newListName;

    private TextField newListNameInput;

    /**
     * Standard-Konstruktor mit Rendering der grundlegenden Elemente
     * alle Übergabeparameter mit Dependency Injection eingefügt
     */
    public MainAppLayout(SecurityService securityService, ListRepository listRepository, UserRepository userRepository, DataService dataService) {
        this.securityService = securityService;
        this.listRepository = listRepository;
        this.userRepository = userRepository;
        this.dataService = dataService;

        // Alle verschiedenen Bestandteile zum Layout hinzufügen - Positionen (Drawer, Navbar) kommen vom Vaadin AppLayout
        addToDrawer(getSidebarLinks());
        Hr limiter = new Hr();
        limiter.getStyle().set("flex-shrink", "0");
        addToDrawer(limiter);
        addToDrawer(getUserLists());
        addToNavbar(getNavbarLayout());
    }

    // region Navbar
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
        DrawerToggle toggle = new DrawerToggle(); // Knopf, um Seitenleiste (Sidebar) zu verstecken/anzuzeigen

        HorizontalLayout logoLayout = getLogoLayout(); // Logo mit Text erstellen

        // Layout der linken Seite der Navigationsleiste
        HorizontalLayout navbarLayoutLeft = new HorizontalLayout();
        navbarLayoutLeft.add(toggle, logoLayout);
        navbarLayoutLeft.setAlignItems(FlexComponent.Alignment.CENTER);
        navbarLayoutLeft.getStyle().set("gap", "0");

        // Layout der gesamten Navigationsleiste
        HorizontalLayout navbarLayout = new HorizontalLayout(navbarLayoutLeft, getProfileMenu());
        navbarLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        navbarLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        navbarLayout.getStyle().set("width", "100%");
        navbarLayout.getStyle().set("margin-right", "1rem");

        return navbarLayout;
    }

    /**
     * Erstellt ein Menü zur Verwaltung des momentan eingeloggten Nutzers
     * @return menuBar
     *      Finale Komponente mit Dropdown-Menü mit Benutzeroptionen wie z.B. Ausloggen
     */
    MenuBar getProfileMenu() {
        // Icons
        Icon userIcon = VaadinIcon.USER.create();
        userIcon.getStyle().set("width", "var(--lumo-icon-size-s)");
        userIcon.getStyle().set("height", "var(--lumo-icon-size-s)");

        Icon settingsIcon = VaadinIcon.COG.create();
        settingsIcon.getStyle().set("width", "var(--lumo-icon-size-s)");
        settingsIcon.getStyle().set("height", "var(--lumo-icon-size-s)");

        Icon logOutIcon = VaadinIcon.SIGN_OUT.create();
        logOutIcon.getStyle().set("width", "var(--lumo-icon-size-s)");
        logOutIcon.getStyle().set("height", "var(--lumo-icon-size-s)");

        // Knopf mit Dropdown-Menu erstellen, innerhalb Optionen für Navigation und Logout
        MenuBar menuBar = new MenuBar();
        MenuItem item = menuBar.addItem("User: " + securityService.getAuthenticatedUser().getUsername());
        SubMenu subMenu = item.getSubMenu();
        subMenu.addItem(new HorizontalLayout(userIcon, new Span("Profile"))).addClickListener(clickEvent -> UI.getCurrent().navigate("/profile"));
        subMenu.addItem(new HorizontalLayout(settingsIcon, new Span("Preferences"))).addClickListener(clickEvent -> UI.getCurrent().navigate("/preferences"));
        subMenu.add(new Hr());
        subMenu.addItem(new HorizontalLayout(logOutIcon, new Span("Log out"))).addClickListener(clickEvent -> securityService.logout());

        return menuBar;
    }
    // endregion

    // region Sidebar

    /**
     * Erstellt den Abschnitt für die Links der Navigation in der Seitenleiste
     * @return navLinks
     *      Finales Layout der Links zur Navigation
     */
    VerticalLayout getSidebarLinks() {
        VerticalLayout navLinks = new VerticalLayout();
        Anchor homeLink = getDrawerNavLinkWithIcon("/", VaadinIcon.HOME.create(), "Home");
        Anchor todayLink = getDrawerNavLinkWithIcon("/daily/" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE), VaadinIcon.CALENDAR_CLOCK.create(), "Today");
        Anchor listsLink = getDrawerNavLinkWithIcon("/calendar", VaadinIcon.CALENDAR.create(), "Calendar");
        Anchor starsLink = getDrawerNavLinkWithIcon("/stars", VaadinIcon.STAR.create(), "Stars");
        navLinks.add(homeLink, todayLink, listsLink, starsLink);
        navLinks.getStyle().set("gap", "1.5rem");
        navLinks.getStyle().set("padding-left", "1.2rem");

        return navLinks;
    }

    /**
     * Erzeugt ein Layout, welches alle Listen des aktuell eingeloggten Benutzers anzeigt
     * Zusätzlich erlaubt es, eine neue Liste zu erstellen
     * @return userLists
     *      Finales Layout mit den Listen des Nutzers
     */
    VerticalLayout getUserLists() {
        Dialog createNewListDialog = createNewListDialog(); // Pop-up Fenster zum Erstellen einer neuen Liste

        // Haupt-Layout mit grundsätzlichen Einstellungen
        VerticalLayout userLists = new VerticalLayout();
        userLists.getStyle().set("padding-left", "1.2rem");
        userLists.getStyle().set("padding-top", "0.5rem");
        userLists.getStyle().set("gap", "0");

        // Unterüberschrift
        H3 myListsHeader = new H3("My Lists");
        myListsHeader.addClassName(LumoUtility.FontSize.XLARGE);
        myListsHeader.getStyle().set("margin-bottom", "0.125rem");

        // Knopf, um eine neue Liste zu erstellen
        Button addNewListButton = new Button(VaadinIcon.PLUS.create());
        addNewListButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        addNewListButton.addClickListener(clickEvent -> {
            createNewListDialog.open();
            newListNameInput.focus();
        });
        addNewListButton.setTooltipText("Create a new list");

        // Zusammenfügen von Überschrift und Knopf zur Listenerstellung in einer horizontalen Linie
        HorizontalLayout myListsHeaderLayout = new HorizontalLayout(myListsHeader, addNewListButton);
        myListsHeaderLayout.addClassNames(LumoUtility.Width.FULL);
        myListsHeaderLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        myListsHeaderLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        userLists.add(myListsHeaderLayout);

        // Abrufen aller Listen eines Nutzers und als Link zur Navigation anzeigen
        VerticalLayout listLinks = new VerticalLayout();
        UserEntity loggedInUser = userRepository.findByUsername(securityService.getAuthenticatedUser().getUsername());
        List<ListEntity> userListsData = listRepository.getUserLists(loggedInUser);
        for (ListEntity list : userListsData) {
            if (!list.isDailyList()) {
                listLinks.add(getListLink(list.getId(), list.getTitle()));
            }
        }
        listLinks.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.Gap.SMALL);
        userLists.add(listLinks);

        return userLists;
    }

    /**
     * Erzeugt einen einzelnen Link für die Navigation in der Seitenleiste
     * @param href
     *      Link, zu dem navigiert wird
     * @param icon
     *      Icon, welches neben dem Link angezeigt wird
     * @param name
     *      Name, der angezeigt wird
     * @return link
     *      Finaler Link
     */
    Anchor getDrawerNavLinkWithIcon(String href, Icon icon, String name) {
        icon.setClassName(LumoUtility.TextColor.BODY);

        Span linkText = new Span(name);
        linkText.getStyle().set("line-height", "1.25rem");
        linkText.getStyle().set("padding-top", "0.35rem");

        HorizontalLayout layout = new HorizontalLayout(icon, linkText);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        Anchor link = new Anchor(href, layout);
        link.setClassName(LumoUtility.TextColor.BODY);

        return link;
    }

    /**
     * Erzeugt den Link für eine Liste in der Seitenleiste
     * @param listId
     *      Eindeutige ID (primary key) der Liste in der Datenbank
     * @param listName
     *      Name der Liste, der angezeigt werden soll
     * @return link
     *      Finaler Link
     */
    Anchor getListLink(UUID listId, String listName) {
        HorizontalLayout listLayout = new HorizontalLayout(new Span("•  "), new Span(listName));
        listLayout.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Gap.MEDIUM);
        Anchor link = new Anchor(String.format("/list/%s", listId), listLayout);
        link.setClassName(LumoUtility.TextColor.BODY);
        link.getStyle().set("font-weight", "semibold");
        link.getStyle().set("font-size", "1.2rem");

        return link;
    }

    /**
     * Pop-up Fenster, um eine neue Liste zu erzeugen
     * @return dialog
     *      Dialog Komponente für das Pop-up Fenster
     */
    Dialog createNewListDialog() {
        // Einrichten der Vaadin Komponente
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Create a new list");

        // Einfügen der Eingabemaske für den namen der zu erstellenden Liste
        TextField newListNameTextField = new TextField("List Name");
        newListNameTextField.setClassName(LumoUtility.Padding.Top.NONE);
        newListNameTextField.addValueChangeListener(e -> this.newListName = e.getValue());
        newListNameTextField.setPlaceholder("Enter a list name...");
        newListNameTextField.addClassName(LumoUtility.Margin.Right.XLARGE);
        this.newListNameInput = newListNameTextField;
        dialog.add(newListNameTextField);

        // Knopf, um neue Liste zu erstellen und in Datenbank zu speichern
        // Zusätzlich wird das Eingabefenster geschlossen und die Seite neu geladen, um die neue Liste in der Seitenleiste anzuzeigen
        Button saveButton = new Button("Save", e -> {
            dataService.createNewList(securityService.getAuthenticatedUser().getUsername(), this.newListName);
            dialog.close();
            UI.getCurrent().getPage().reload();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickShortcut(Key.ENTER);
        // Knopf, um Aktion abzubrechen und Fenster zu schließen
        Button cancelButton = new Button("Cancel", e -> dialog.close());

        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        return dialog;
    }
    // endregion
}
