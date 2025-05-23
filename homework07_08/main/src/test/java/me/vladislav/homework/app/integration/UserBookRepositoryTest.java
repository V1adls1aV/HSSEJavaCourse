package me.vladislav.homework.app.integration;

import me.vladislav.homework.app.TestContainersConfig;
import me.vladislav.homework.app.db.orm.Book;
import me.vladislav.homework.app.db.orm.User;
import me.vladislav.homework.app.db.repository.BookRepository;
import me.vladislav.homework.app.db.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserBookRepositoryTest extends TestContainersConfig {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private BookRepository bookRepository;

  @Test
  public void findBooksByUserId() {
    User user = userRepository.save(new User(null, "vlad", "me@vlad.ru"));
    assertNotNull(user.getId());

    Book book1 = bookRepository.save(new Book(null, "Book 1", "Someone Kind", user));
    assertNotNull(book1.getId());
    assertEquals("Book 1", book1.getTitle());
    assertEquals("Someone Kind", book1.getAuthor());

    Book book2 = bookRepository.save(new Book(null, "Book 2", "Kind", user));
    assertNotNull(book2.getId());
    assertEquals("Book 2", book2.getTitle());
    assertEquals("Kind", book2.getAuthor());

    Set<Book> books = bookRepository.findByUserId(user.getId());
    assertEquals(2, books.size());
    assertTrue(books.stream().anyMatch(b -> b.getTitle().equals("Book 1")));
    assertTrue(books.stream().anyMatch(b -> b.getTitle().equals("Book 2")));
  }

  @Test
  public void findNothingByUserId() {
    User user = userRepository.save(new User(null, "vlad", "me@vlad.ru"));
    assertNotNull(user.getId());

    Set<Book> books = bookRepository.findByUserId(user.getId());
    assertEquals(0, books.size());
  }
}
