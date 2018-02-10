package stockexchange.com.stockexchange.repository.impl;


import org.springframework.stereotype.Repository;
import stockexchange.com.stockexchange.model.User;
import stockexchange.com.stockexchange.repository.UserRepository;

@Repository
public class SQLUserRepository implements UserRepository{
    @Override
    public User findByUsername(String login) {
        return null;
    }

    @Override
    public void persistUser(User user) {

    }
}
