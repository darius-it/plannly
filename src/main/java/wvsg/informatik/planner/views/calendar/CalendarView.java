package wvsg.informatik.planner.views.calendar;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import wvsg.informatik.planner.views.MainAppLayout;

/**
 * Anzeige eines Kalenders zur vereinfachten Navigation der täglichen Listen
 */
@PageTitle("Calendar | plannly")
@Route(value = "/calendar", layout = MainAppLayout.class)
@PermitAll
public class CalendarView extends VerticalLayout {
    public CalendarView() {
        FullCalendar calendar = FullCalendarBuilder.create().build();
        calendar.addClassNames(LumoUtility.Height.FULL, LumoUtility.Width.FULL);

        this.setFlexGrow(1, calendar);
        this.addClassNames(LumoUtility.Height.FULL, LumoUtility.Width.FULL);

        calendar.addTimeslotClickedListener((event) -> {
            UI.getCurrent().navigate("/daily/" + event.getDate().toString());
        });

        HorizontalLayout calendarNavButtons = new HorizontalLayout(
                new Button(VaadinIcon.ANGLE_LEFT.create(), e -> calendar.previous()),
                new Button("Today", e -> calendar.today()),
                new Button(VaadinIcon.ANGLE_RIGHT.create(), e -> calendar.next())
        );
        HorizontalLayout calendarTopLayout = new HorizontalLayout(new H1("Calendar"), calendarNavButtons);
        calendarTopLayout.setAlignItems(Alignment.CENTER);
        calendarTopLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        calendarTopLayout.addClassName(LumoUtility.Width.FULL);

        add(calendarTopLayout);
        add(calendar);
    }
}
