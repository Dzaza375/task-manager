package com.example.task_manager.tasks;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskController {
    //GET	/api/tasks	Получить все задачи	✅ USER/ADMIN
    //POST	/api/tasks	Создать задачу	✅ USER/ADMIN
    //PUT	/api/tasks/{id}	Обновить задачу	✅ USER
    //DELETE	/api/tasks/{id}	Удалить задачу	✅ ADMIN
}
