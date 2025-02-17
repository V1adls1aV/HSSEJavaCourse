package me.vladislav.homework02.app.db.repository.book;

import me.vladislav.homework02.app.dto.service.Book;
import me.vladislav.homework02.app.dto.service.BookData;

import java.util.Optional;

public interface BookRepository {
  Book create(BookData book);

  Optional<Book> getById(Long id);

  /**
   * It will overwrite existing book object with the same id.
   * If there is no book to update return `Optional.empty()` (book with passed `id` does not exist), over wise, return current object after update.
   * <p>
   * So, you can create pipelines like without any exceptions:
   * <p>
   * Book updatedBook = bookRepository.update(book).orElse(bookRepository.create(book.getBookData()));
   * <p>
   * In such way we can "put" object no matter was it deleted or not.
   *
   * @param book The book to update.
   */
  Optional<Book> update(Book book);

  void delete(Long id);
}
