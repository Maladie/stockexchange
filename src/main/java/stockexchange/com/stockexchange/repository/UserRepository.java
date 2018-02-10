package stockexchange.com.stockexchange.repository;

import stockexchange.com.stockexchange.model.User;

public interface UserRepository {
    User findByUsername(String login);
    void persistUser(User user);
    User findById(Long id);
}
