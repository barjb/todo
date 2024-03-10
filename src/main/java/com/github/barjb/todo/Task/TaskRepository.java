package com.github.barjb.todo.Task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface TaskRepository
    extends CrudRepository<Task, Long>,
        PagingAndSortingRepository<Task, Long>,
        JpaSpecificationExecutor<Task> {

  @Query(
      "SELECT t FROM Task t WHERE t.user.id=(SELECT u.id FROM User u WHERE u.username=%:owner%) AND t.id = %:id% AND t.deletedAt = NULL")
  Task findByIdAndOwner(Long id, String owner);

  // Napisac query do wyciagania id po username
  @Query(
      "SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END FROM Task t WHERE t.user.id=(SELECT u.id FROM User u WHERE u.username=%:owner%) AND t.id = %:id% and t.deletedAt = NULL")
  boolean existsByIdAndOwner(Long id, String owner);

  @Query(
      "SELECT t FROM Task t WHERE t.user.id=(SELECT u.id FROM User u WHERE u.username=%:owner%) AND t.deletedAt = NULL")
  Page<Task> findByOwner(String owner, PageRequest amount);

  @Modifying
  @Query("UPDATE Task t SET t.deletedAt = CURRENT_TIMESTAMP WHERE t.id = :id")
  void deleteTaskByTaskId(@Param("id") Long id);
}
