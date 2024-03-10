package com.github.barjb.todo.Group;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.barjb.todo.User.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "groups")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Group {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_id")
  private Long id;

  private String description;

  @ManyToOne()
  @JoinColumn(name = "USER_ID")
  @JsonManagedReference
  private User owner;

  @ManyToMany(mappedBy = "groups")
  @JsonManagedReference
  private Set<User> members;
}
