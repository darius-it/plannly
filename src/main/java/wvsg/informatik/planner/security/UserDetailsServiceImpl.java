package wvsg.informatik.planner.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wvsg.informatik.planner.data.entity.UserEntity;
import wvsg.informatik.planner.data.repository.UserRepository;

/**
 * Der UserDetailsServiceImpl implementiert den UserDetailsService und stellt Methoden zur Benutzerdetails-Verwaltung bereit.
 * Dieser wird benötigt, da Spring Security nicht direkt kompatibel ist mit
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Konstruktor des UserDetailsServiceImpl.
     * @param userRepository das UserRepository für den Zugriff auf Benutzerdaten
     */
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Lädt Benutzerdetails basierend auf dem Benutzernamen.
     * @param username der Benutzername des Benutzers
     * @return die UserDetails-Objekt des Benutzers
     * @throws UsernameNotFoundException falls kein Benutzer mit dem angegebenen Benutzernamen gefunden wurde
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user with this username exists: " + username);
        } else {
            return User.withUsername(user.getUsername()).password(user.getPassword()).authorities("USER").build();
        }
    }
}
