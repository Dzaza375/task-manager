package com.example.task_manager.pagination;

import com.example.task_manager.exception.InvalidSortFieldException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PageableValidator {
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "title",
            "description",
            "dueDate",
            "status"
    );

    public Pageable validate(Pageable pageable) {
        pageable.getSort().forEach(order -> {
            if (!ALLOWED_SORT_FIELDS.contains(order.getProperty())) {
                throw new InvalidSortFieldException(order.getProperty());
            }
        });

        return pageable;
    }
}
