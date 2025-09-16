package wvsg.informatik.planner;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Einstiegspunkt der Vaadin-Applikation, mit Spring Boot als Basis.
 * Definiert verschiedene Grundparameter der App,
 * wie z.B. das Erscheinungsbild der Vaadin Flow Komponenten und die Einstellungen für den PWA-Modus (Progressive Web App)
 */
@SpringBootApplication
@Theme(value = "plannerapp")
@PWA(name = "plannly",
        shortName = "plannly")
public class Application implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
