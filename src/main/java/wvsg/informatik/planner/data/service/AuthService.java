package wvsg.informatik.planner.data.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wvsg.informatik.planner.data.entity.UserEntity;
import wvsg.informatik.planner.data.repository.UserRepository;

/**
 * Service zur Verarbeitung von Signup und Login (v.a. Signup)
 */
@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Standard-Konstruktor mit Dependency Injection
     * @param passwordEncoder
     *  Password-Codierer, der von der Spring Security Library zum Encoding/Hashen von Passwörtern bereitgestellt wird
     */
    public AuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     *
     * @param user
     *  Ein temporäres Objekt für einen neuen Nutzer als Träger der Information, die auf neues Objekt übertragen wird
     * @throws Exception
     *  Fehler wird ausgegeben, wenn ein Nutzer mit dem eingegebenen Nutzernamen bereits existiert
     */
    public void registerUser(UserEntity user) throws Exception {
        // Überprüfen, ob Nutzer bereits existiert
        if(checkIfUserExists(user.getUsername())){
            throw new Exception("User already exists with this username");
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.printf("%s %s %s%n", user.getUsername(), user.getEmail(), user.getPassword());

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity); // zunächst alle Daten auf neues Objekt übertragen
        encodePassword(userEntity, user); // neues Objekt trägt Passwort nicht mehr im Klartext sondern codiert
        userEntity.setUsername("trobo");
        userEntity.setEmail("pog@gmail.com");

        userRepository.save(userEntity); // neues Objekt wird in Datenbank gespeichert
        System.out.println("User registration succeeded, saved in database");
    }

    /**
     * Einfache Methode zur Überprüfung, ob ein Nutzer in der Datenbank existiert
     * @param username
     *  Nutzername des zu suchenden Nutzers
     * @return
     *  Boolscher Wert, true wenn der Nutzer existiert, false wenn nicht
     */
    public boolean checkIfUserExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    /**
     * Codiert das Passwort für das neue, zu speichernde Nutzer-Objekt mit einem Hashing-Algorithmus
     * @param userEntity
     *  neues, zu speicherndes Nutzer-Objekt
     * @param user
     *  temporäres Nutzer-Objekt mit Passwort im Klartext, wird nach registerUser Methode verworfen
     *  (technischer Hintergrund: wird an die Methode übergeben, kurz verwedet aber danach keine Verwendung mehr -> Java Garbage Collector (GC) entleert Speicher, Objekt verschwindet)
     */
    private void encodePassword(UserEntity userEntity, UserEntity user){
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}
