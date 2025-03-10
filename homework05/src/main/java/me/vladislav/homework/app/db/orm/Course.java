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

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@Data
@AllArgsConstructor
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String title;

  @NotNull
  private String author;

  private String description;

  private Integer duration;

  @ManyToMany(mappedBy = "courses")
  private Set<User> users = new HashSet<>();

  protected Course() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Course course)) {
      return false;
    }
    return id != null && id.equals(course.id);
  }

  @Override
  public int hashCode() {
    return Course.class.hashCode();
  }
}
