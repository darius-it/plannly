package wvsg.informatik.planner.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Der SecurityService bietet Methoden zum Zugriff auf Authentifizierungsinformationen.
 */
@Component
public class SecurityService {

    private final AuthenticationContext authenticationContext;

    /**
     * Konstruktor des SecurityService.
     * @param authenticationContext der AuthenticationContext für die Authentifizierung
     */
    public SecurityService(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    /**
     * Gibt den aktuell authentifizierten Benutzer zurück.
     * @return der UserDetails-Objekt des authentifizierten Benutzers
     */
    public UserDetails getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class).get();
    }

    /**
     * Führt eine Abmeldung des aktuellen Benutzers durch.
     */
    public void logout() {
        authenticationContext.logout();
    }
}
