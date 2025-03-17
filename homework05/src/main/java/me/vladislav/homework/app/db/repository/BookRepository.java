package me.vladislav.homework.app.db.repository;

import me.vladislav.homework.app.db.orm.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BookRepository extends JpaRepository<Book, Long> {
  Set<Book> findByUserId(Long userId);
}
