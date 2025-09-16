package wvsg.informatik.planner.components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Die CalendarComponent ist eine benutzerdefinierte Komponente, die einen Kalender mit auswählbaren Datumsfeldern darstellt.
 */
public class CalendarComponent extends HorizontalLayout {

    private LocalDate currentDate;
    private final List<Button> dateButtons = new ArrayList<>();
    private final Button leftButton;
    private final Button rightButton;

    public CalendarComponent() {
        setClassName("calendar-component");

        currentDate = LocalDate.now();

        // Erzeugt die Datumsbuttons
        for (int i = -6; i <= 6; i++) {
            Button button = new Button();
            button.setText(getDateString(i));
            button.addClickListener((ComponentEventListener<ClickEvent<Button>>) e -> {
                selectDate(button);
            });
            if (i == 0) {
                button.addClassName("selected");
                button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
                button.getStyle().set("background-color", "#0D3B66");
            } else if (i < 0) {
                button.addClassName("small");
                button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_SMALL);
            } else {
                button.addClassName("small");
                button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_SMALL);
            }
            dateButtons.add(button);
        }

        // Erzeugt die Links- und Rechts-Pfeil-Buttons
        leftButton = new Button("<");
        leftButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        leftButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) e -> {
            selectDate(dateButtons.get(5));
        });

        rightButton = new Button(">");
        rightButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        rightButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) e -> {
            selectDate(dateButtons.get(7));
        });

        // Fügt die Komponenten zum Layout hinzu
        add(leftButton);
        dateButtons.forEach(this::add);
        add(rightButton);

        selectDate(dateButtons.get(0)); // Wählt standardmäßig das mittlere Datum aus
    }


    private void selectDate(Button button) {
        dateButtons.forEach(b -> b.removeClassName("selected"));
        button.addClassName("selected");
        button.getStyle().set("background-color", "#0D3B66");

        currentDate = getDate(button);

        dateButtons.forEach(b -> {
            int i = dateButtons.indexOf(b) - 6;
            b.setText(getDateString(i));
            if (i == 0) {
                b.removeThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
                b.setWidth("100px");
                b.setHeight("35px");
                b.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
                b.getStyle().set("background-color", "#0D3B66");
            } else if (i < 0) {
                b.removeThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
                double scale = 1 + i / 8.0;
                b.setWidth((int) (100 * scale) + "px");
                b.setHeight((int) (35 * scale) + "px");
                b.addThemeVariants(ButtonVariant.LUMO_SMALL);
                b.getStyle().remove("background-color");
                b.getElement().getStyle().set("margin-left", (int) (20 * (1 - scale)) + "px");
            } else {
                b.removeThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
                double scale = 1 - i / 8.0;
                b.setWidth((int) (100 * scale) + "px");
                b.setHeight((int) (35 * scale) + "px");
                b.addThemeVariants(ButtonVariant.LUMO_SMALL);
                b.getStyle().remove("background-color");
                b.getElement().getStyle().set("margin-left", (int) (20 * (1 - scale)) + "px");
            }
        });

        // Verschiebt den ausgewählten Button in die Mitte

    }


    private void updateDates(LocalDate date) {
        for (int i = -6; i <= 6; i++) {
            Button button = dateButtons.get(i + 6);
            button.setText(getDateString(i));
            if (date.plusDays(i).isEqual(currentDate)) {
                button.addClassName("selected");
                button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
                button.getStyle().set("background-color", "#0D3B66");
            } else if (i < 0) {
                button.removeClassName("selected");
                button.removeThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
                button.addClassName("small");
                button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_SMALL);
                button.getStyle().remove("background-color");
            } else {
                button.removeClassName("selected");
                button.removeThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
                button.addClassName("small");
                button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_SMALL);
                button.getStyle().remove("background-color");
            }
        }
    }

    private void updateLeftArrow() {


        int index = dateButtons.indexOf(dateButtons.stream()
                .filter(b -> b.getClassName().contains("selected"))
                .findFirst().orElse(null));
        if (index == -1 || index == 0) {
            leftButton.setEnabled(false);
        } else {
            leftButton.setEnabled(true);
            leftButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) e -> {
                selectDate(dateButtons.get(index - 1));
            });
        }

    }

    private void updateRightArrow() {
        int index = dateButtons.indexOf(dateButtons.stream()
                .filter(b -> b.getClassName().contains("selected"))
                .findFirst().orElse(null));
        if (index == -1 || index == dateButtons.size() - 1) {
            rightButton.setEnabled(false);
        } else {
            rightButton.setEnabled(true);
            rightButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) e -> {
                selectDate(dateButtons.get(index + 1));
            });
        }
    }
    private String getDateString(int offset) {
        LocalDate date = currentDate.plusDays(offset);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM d, uuuu");

        return date.format(formatter);
    }

    private LocalDate getDate(Button button) {
        String dateString = button.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM d, uuuu");

        return LocalDate.parse(dateString, formatter);
    }
}
