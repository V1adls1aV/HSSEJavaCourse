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
@Table(name = "universities")
@Data
@AllArgsConstructor
public class University {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String name;

  @NotNull
  private String city;

  private String description;

  @NotNull
  private Integer rateKrutosty;

  @ManyToMany(mappedBy = "universities")
  private Set<User> users = new HashSet<>();

  protected University() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof University university)) {
      return false;
    }
    return id != null && id.equals(university.id);
  }

  @Override
  public int hashCode() {
    return University.class.hashCode();
  }
}
