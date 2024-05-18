package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User> {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        if (user.getEmail() == null) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        users.values().stream()
                .filter(us -> us.getEmail().equals(user.getEmail()))
                .findAny()
                .ifPresent(us -> {
                    throw new DuplicatedDataException("Этот имейл уже используется");
                });
        user.setId(getNextId(users));
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    private User updateUser(@RequestBody User newUser) {
        User user = users.get(newUser.getId());
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        users.values().stream()
                .filter(us -> us.getEmail().equals(newUser.getEmail()))
                .findAny()
                .ifPresent(us -> {
                    throw new DuplicatedDataException("Этот имейл уже используется");
                });
        if (newUser.getEmail() != null) {
            user.setEmail(newUser.getEmail());
        }
        if (newUser.getUsername() != null) {
            user.setUsername(newUser.getUsername());
        }
        if (newUser.getPassword() != null) {
            user.setPassword(newUser.getPassword());
        }
        users.put(newUser.getId(), user);
        return user;
    }
}