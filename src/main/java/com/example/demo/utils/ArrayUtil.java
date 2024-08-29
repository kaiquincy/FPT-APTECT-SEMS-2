package com.example.demo.utils;

import java.util.List;

public class ArrayUtil {
    public static <E> boolean isEmpty(List<E> list) {
        return list == null || list.isEmpty();
    }

    public static <E> boolean isNotEmpty(List<E> list) {
        return list != null && !list.isEmpty();
    }

    public static <E> List<E> diff(List<E> source, List<E> list) {
        return source.stream().filter(e -> !list.contains(e)).toList();
    }
}