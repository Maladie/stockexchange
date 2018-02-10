package stockexchange.com.stockexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stockexchange.com.stockexchange.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String login);
    User findById(Long id);
}
