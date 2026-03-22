package com.example.task_manager.filters;

import com.example.task_manager.model.task.Task;
import com.example.task_manager.model.task.TaskStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class TaskSpecs {
    public static Specification<Task> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
                if (title == null) {
                    return null;
                }
                return criteriaBuilder.equal(root.get("title"), title);
        };
    }

    public static Specification<Task> hasDescription(String description) {
        return (root, query, criteriaBuilder) -> {
            if (description == null) {
                return null;
            }
            return criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("description")),
                                        "%" + description.toLowerCase() + "%");
        };
    }

    public static Specification<Task> byDueDateRange(LocalDate from, LocalDate to) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (from != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), from)
                );
            }

            if (to != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), to)
                );
            }

            return predicates.isEmpty()
                    ? criteriaBuilder.conjunction()
                    : criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Task> build(TaskFilter filter) {
        return hasTitle(filter.getTitle())
                .and(hasDescription(filter.getDescription()))
                .and(byDueDateRange(filter.getDueDateFrom(), filter.getDueDateTo()))
                .and(hasStatus(filter.getStatus()));
    }
}
