package stockexchange.com.stockexchange.service.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import stockexchange.com.stockexchange.model.User;
import stockexchange.com.stockexchange.repository.UserTokenRepository;
import stockexchange.com.stockexchange.utils.CacheUtil;
import stockexchange.com.stockexchange.utils.EmptyCheck;


@Async
@Component
public class AsyncTokenExpire {
    private static Logger logger = LogManager.getLogger(AsyncTokenExpire.class);
    @Autowired
    private static UserTokenRepository userTokenRepository;

    /**
     * Async expired token removal (cache + DB) if exist
     * @param expiredToken expired token id
     */
    public static void expireToken(String expiredToken) {
        if (CacheUtil.getFromCache(expiredToken) instanceof User) {
            logger.debug("Token Expire Message Received ---> " + expiredToken);
            User user = (User) CacheUtil.getFromCache(expiredToken);
            if (!EmptyCheck.isNullObject(user)) {
                CacheUtil.removeFromCache(expiredToken);
            }

            if (EmptyCheck.isNotNullOrEmpty(expiredToken))
                if (user != null) {
                    userTokenRepository.updateUserTokenStatus(expiredToken, TokenStatus.EXPIRED_TIME, user.getId());
                }
            logger.info(expiredToken + " is expired.");
        }
    }
}
