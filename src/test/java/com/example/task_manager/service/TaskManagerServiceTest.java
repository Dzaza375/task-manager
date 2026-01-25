package com.example.task_manager.service;

import com.example.task_manager.mapper.TaskMapper;
import com.example.task_manager.repo.AuthRepo;
import com.example.task_manager.dto.task.TaskDto;
import com.example.task_manager.exception.NotEnoughRightException;
import com.example.task_manager.exception.TaskNotFoundException;
import com.example.task_manager.exception.UserWithUsernameNotExistsException;
import com.example.task_manager.model.task.Task;
import com.example.task_manager.model.task.TaskStatus;
import com.example.task_manager.repo.TaskManagerRepo;
import com.example.task_manager.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.task_manager.model.user.UserRoles.USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskManagerServiceTest {
    @Mock private TaskManagerRepo taskManagerRepo;
    @Mock private AuthRepo authRepo;
    @Mock private TaskMapper taskMapper;

    @InjectMocks
    TaskManagerService taskManagerService;

    private static  final Long TEST_USER_ID = 0L;
    private static final String TEST_USERNAME = "tom";
    private static final String TEST_PASSWORD = "pass1234";
    private static final String TEST_EMAIL = "tom@gmail.com";

    private static  final Long TEST_TASK_ID = 0L;
    private static final String TEST_TITLE = "Make a report";
    private static final String TEST_DESCRIPTION = "Make a report for my boss until it too late";
    private static final LocalDate TEST_DUE_DATE = LocalDate.now();
    private static  final TaskStatus TEST_STATUS = TaskStatus.IN_PROGRESS;

    User testUser;
    TaskDto testTaskDto;
    Task testTask;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(TEST_USER_ID);
        testUser.setUsername(TEST_USERNAME);
        testUser.setPassword(TEST_PASSWORD);
        testUser.setEmail(TEST_EMAIL);
        testUser.setRole(USER);

        testTaskDto = new TaskDto();
        testTaskDto.setTitle(TEST_TITLE);
        testTaskDto.setDescription(TEST_DESCRIPTION);
        testTaskDto.setDueDate(TEST_DUE_DATE);
        testTaskDto.setStatus(TEST_STATUS);

        testTask = new Task();
        testTask.setId(TEST_TASK_ID);
        testTask.setTitle(TEST_TITLE);
        testTask.setDescription(TEST_DESCRIPTION);
        testTask.setDueDate(TEST_DUE_DATE);
        testTask.setStatus(TEST_STATUS);
        testTask.setAssignedTo(testUser);
    }

    @Test
    void getAllTasks_shouldReturnListOfAllTasks() {
        List<Task> tasks = List.of(new Task(), new Task());
        List<TaskDto> taskDtos = List.of(new TaskDto(), new TaskDto());

        when(taskManagerRepo.findAll()).thenReturn(tasks);
        when(taskMapper.taskDtos(tasks)).thenReturn(taskDtos);

        assertThat(taskManagerService.getAllTasks()).isEqualTo(taskDtos);
        verify(taskManagerRepo).findAll();
        verify(taskMapper).taskDtos(tasks);
    }

    @Test
    void createTask_shouldSaveNewTask() {
        when(authRepo.findByUsername(anyString())).thenReturn(Optional.of(testUser));

        taskManagerService.createTask(testTaskDto, TEST_USERNAME);
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

        verify(taskManagerRepo).save(captor.capture());
        Task savedTask = captor.getValue();

        assertThat(savedTask.getTitle()).isEqualTo(TEST_TITLE);
        assertThat(savedTask.getDescription()).isEqualTo(TEST_DESCRIPTION);
        assertThat(savedTask.getDueDate()).isEqualTo(TEST_DUE_DATE);
        assertThat(savedTask.getStatus()).isEqualTo(TEST_STATUS);
    }

    @Test
    void createTask_shouldThrowUserWithUsernameNotExistsException() {
        when(authRepo.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskManagerService.createTask(testTaskDto, TEST_USERNAME))
                .isInstanceOf(UserWithUsernameNotExistsException.class)
                .hasMessageContaining(TEST_USERNAME);

        verify(taskManagerRepo, never()).save(any());
    }

    @Test
    void updateTask_shouldUpdateExistingTask() {
        when(taskManagerRepo.findById(anyLong())).thenReturn(Optional.of(testTask));

        taskManagerService.updateTask(TEST_TASK_ID, testTaskDto, TEST_USERNAME, true);

        assertThat(testTask.getTitle()).isEqualTo(TEST_TITLE);
        assertThat(testTask.getDescription()).isEqualTo(TEST_DESCRIPTION);
        assertThat(testTask.getDueDate()).isEqualTo(TEST_DUE_DATE);
        assertThat(testTask.getStatus()).isEqualTo(TEST_STATUS);

        verify(taskManagerRepo).findById(TEST_TASK_ID);
        verify(taskManagerRepo, never()).save(any());
    }

    @Test
    void updateTask_shouldThrowTaskNotFoundException() {
        when((taskManagerRepo.findById(anyLong()))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskManagerService.updateTask(TEST_TASK_ID, testTaskDto, TEST_USERNAME, true))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(String.valueOf(TEST_TASK_ID));
    }

    @Test
    void updateTask_shouldThrowNotEnoughRightException_whenUsernameAndIsAdminAreNotValid() {
        when(taskManagerRepo.findById(anyLong())).thenReturn(Optional.of(testTask));

        assertThatThrownBy(() -> taskManagerService.updateTask(TEST_TASK_ID, testTaskDto, "nonexistent", false))
                .isInstanceOf(NotEnoughRightException.class);
    }

    @Test
    void deleteTask_shouldDeleteExistingTask() {
        when(taskManagerRepo.findById(anyLong())).thenReturn(Optional.of(testTask));

        taskManagerService.deleteTask(TEST_TASK_ID, TEST_USERNAME, true);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskManagerRepo).delete(captor.capture());
        Task deletedTask = captor.getValue();

        assertThat(deletedTask.getTitle()).isEqualTo(TEST_TITLE);
        assertThat(deletedTask.getDescription()).isEqualTo(TEST_DESCRIPTION);
        assertThat(deletedTask.getDueDate()).isEqualTo(TEST_DUE_DATE);
        assertThat(deletedTask.getStatus()).isEqualTo(TEST_STATUS);
    }

    @Test
    void deleteTask_shouldThrowTaskNotFoundException() {
        when((taskManagerRepo.findById(anyLong()))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskManagerService.deleteTask(TEST_TASK_ID, TEST_USERNAME, true))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(String.valueOf(TEST_TASK_ID));
    }

    @Test
    void deleteTask_shouldThrowNotEnoughRightException_whenUsernameAndIsAdminAreNotValid() {
        when(taskManagerRepo.findById(anyLong())).thenReturn(Optional.of(testTask));

        assertThatThrownBy(() -> taskManagerService.deleteTask(TEST_TASK_ID, "nonexistent", false))
                .isInstanceOf(NotEnoughRightException.class);
    }
}