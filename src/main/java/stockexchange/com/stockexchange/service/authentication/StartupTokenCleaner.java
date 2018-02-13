package stockexchange.com.stockexchange.service.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import stockexchange.com.stockexchange.repository.UserTokenRepository;

@Service
public class StartupTokenCleaner implements ApplicationListener<ContextRefreshedEvent> {

    private final UserTokenRepository repository;

    @Autowired
    public StartupTokenCleaner(UserTokenRepository repository) {
        this.repository = repository;
    }

    /**
     * Removes all token entries from UserToken table invoked after application init
     * @param contextRefreshedEvent Application context refreshed event
     */
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        repository.deleteAll();
    }
}
