package ro.alexportfolio.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.alexportfolio.backend.model.User;

import java.util.Optional;

@Repository
//@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

    @Transactional
    @Modifying
    void deleteByUserId(String userId);
}
