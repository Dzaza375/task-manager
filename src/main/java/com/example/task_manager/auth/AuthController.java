package com.example.task_manager.auth;

import com.example.task_manager.jwt.JwtResponses;
import com.example.task_manager.user.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public void register(@RequestBody @Valid UserDto userDto){
        authService.register(userDto);
    }

    @PostMapping("/admin-register")
    @ResponseStatus(CREATED)
    public void adminRegister(@RequestBody @Valid UserDto userDto,
                                 @RequestParam String adminCode){
        authService.adminRegister(userDto, adminCode);
    }

    @PostMapping("/login")
    @ResponseStatus(OK)
    public JwtResponses login(@RequestBody @Valid UserDto userDto) {
        authService.login();
    }

    //POST	/api/auth/login	Логин (возврат JWT)	⬜ Открыт
    //GET	/api/tasks	Получить все задачи	✅ USER/ADMIN
    //POST	/api/tasks	Создать задачу	✅ USER/ADMIN
    //PUT	/api/tasks/{id}	Обновить задачу	✅ USER
    //DELETE	/api/tasks/{id}	Удалить задачу	✅ ADMIN
}
