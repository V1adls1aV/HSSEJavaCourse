package me.vladislav.homework.app.db.repository;

import me.vladislav.homework.app.db.orm.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
