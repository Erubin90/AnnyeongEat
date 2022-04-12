package tech.erubin.annyeong_eat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.erubin.annyeong_eat.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    User findByTelegramUserId(String userId);
}
