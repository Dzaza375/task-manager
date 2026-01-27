package com.example.task_manager.pagination;

import com.example.task_manager.exception.InvalidSortFieldException;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class PageableValidatorTest {
    private final PageableValidator pageableValidator = new PageableValidator();

    @Test
    void validate_shouldAllowWhitelistedFields() {
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by("status")
        );

        Pageable result = pageableValidator.validate(pageable);

        assertThat(result).isSameAs(pageable);
    }

    @Test
    void validate_shouldThrowInvalidSortFieldException() {
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by("password")
        );

        assertThatThrownBy(() -> pageableValidator.validate(pageable))
                .isInstanceOf(InvalidSortFieldException.class)
                .hasMessageContaining("password");
    }
}
