package me.vladislav.homework.app.db.orm;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@AllArgsConstructor
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String title;

  @NotNull
  private String author;

  @ManyToMany(mappedBy = "books")
  private List<User> users = new ArrayList<>();

  protected Book() {
  }

  public Book(Long id, String title, String author) {
    this.id = id;
    this.title = title;
    this.author = author;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Book book)) {
      return false;
    }
    return id != null && id.equals(book.id);
  }

  @Override
  public int hashCode() {
    return Book.class.hashCode();
  }
}
