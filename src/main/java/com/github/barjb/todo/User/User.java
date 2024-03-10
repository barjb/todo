package com.github.barjb.todo.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.barjb.todo.Group.Group;
import com.github.barjb.todo.Task.Task;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  private String description;

  @OneToMany(mappedBy = "user")
  @JsonBackReference
  private List<Task> taskList;

  @ManyToMany()
  @JoinTable(
      name = "users_groups",
      joinColumns = {@JoinColumn(name = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "group_id")})
  @JsonBackReference
  private Set<Group> groups;

  @OneToMany(mappedBy = "owner")
  @JsonBackReference
  private Set<Group> ownedGroups;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
  private LocalDateTime deletedAt;
}
