package wvsg.informatik.planner.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import wvsg.informatik.planner.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Die SecurityConfig-Klasse konfiguriert die Sicherheitseinstellungen der Anwendung.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    /**
     * Konfiguriert die HTTP-Sicherheit der Anwendung.
     * Erlaubt den Zugriff auf bestimmte Ressourcen wie Bilddateien und die Anmeldeseite ohne Authentifizierung.
     * Setzt die LoginView-Klasse als die Anmeldeseite.
     * @param http die HttpSecurity-Instanz zum Konfigurieren der Sicherheit
     * @throws Exception wenn ein Fehler bei der Konfiguration auftritt
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/images/*.png").permitAll()
                .requestMatchers("/signup").permitAll();
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    /**
     * Erzeugt eine Instanz des BCryptPasswordEncoder als PasswordEncoder-Bean.
     * @return der PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
