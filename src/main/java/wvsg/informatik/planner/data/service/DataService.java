package wvsg.informatik.planner.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import wvsg.informatik.planner.data.entity.ListEntity;
import wvsg.informatik.planner.data.entity.UserEntity;
import wvsg.informatik.planner.data.repository.ListRepository;
import wvsg.informatik.planner.data.repository.UserRepository;
import wvsg.informatik.planner.security.SecurityService;

/**
 * Service für komplexere Aufgaben zur Verwaltung von Daten in der Datenbank (v.a. Listen, Bullets)
 * -> grundsätzlich alles, was nicht mit einzelnen Queries in den Repositories erledigt werden kann
 */
@Service
public class DataService {

    @Autowired
    public ListRepository listRepository;

    @Autowired
    public UserRepository userRepository;

    SecurityService securityService;

    public DataService(SecurityService securityService) {
        this.securityService = securityService;
    }

    /**
     * Erstellt eine neue Liste mit einem bestimmten Titel für einen übergebenen Nutzer und speichert diese in der Datenbank
     * @param username
     *  Nutzer, dem die Liste gehört (normalerweise momentan eingeloggter Nutzer)
     * @param title
     *  Titel der neuen Liste
     */
    public void createNewList(String username, String title) {
        // aus Nutzernamen volle Entity erhalten, da die ListEntity aufgrund der Relation der Klassen eine UserEntity erwartet
        UserEntity dbUser = userRepository.findByUsername(username);

        // neue ListEntity erstellen und in Datenbank abspeichern
        ListEntity newList = new ListEntity();
        newList.setTitle(title);
        newList.setOwner(dbUser);
        newList.setDailyList(false);
        listRepository.save(newList);
    }

    /**
     * Methode, um ein UserDetails Objekt des momentan eingeloggten Nutzers in Spring Security in ein mit unseren Daten kompatibles UserEntity Objekt umzuwandeln/zu erhalten
     * @return ein UserEntity Objekt des aktuell eingeloggten Nutzers
     */
    public UserEntity getCurrentlyAuthenticatedUserEntity() {
        UserDetails user = securityService.getAuthenticatedUser();
        return userRepository.findByUsername(user.getUsername());
    }

    public ListEntity getDailyListAndCreateIfNotExists(String dateString) {
        ListEntity dailyList =  listRepository.findDailyListByDateStringTitle(dateString, getCurrentlyAuthenticatedUserEntity());

        if (dailyList == null) {
            ListEntity newList = new ListEntity();
            newList.setTitle(dateString);
            newList.setOwner(getCurrentlyAuthenticatedUserEntity());
            newList.setDailyList(true);
            listRepository.save(newList);
            dailyList = newList;
        }

        return dailyList;
    }
}
