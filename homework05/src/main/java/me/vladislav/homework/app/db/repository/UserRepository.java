package me.vladislav.homework.app.db.repository;

import me.vladislav.homework.app.db.orm.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
