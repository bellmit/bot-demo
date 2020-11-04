package com.syozzz.bot.repository;

import com.syozzz.bot.task.TaskProp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * task repo
 * @author syozzz
 * @version 1.0
 * @date 2020-11-03
 */
@Repository
public interface TaskRepository extends JpaRepository<TaskProp, String> {

}
