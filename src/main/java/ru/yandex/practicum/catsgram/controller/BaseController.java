package ru.yandex.practicum.catsgram.controller;

import java.util.Map;

public abstract class BaseController<T> {
    protected long getNextId(Map<Long, T> map) {
        return map.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0) + 1;
    }
}