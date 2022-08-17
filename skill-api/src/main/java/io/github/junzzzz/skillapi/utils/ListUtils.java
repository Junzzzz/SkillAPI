package io.github.junzzzz.skillapi.utils;

import java.util.*;
import java.util.function.Function;

/**
 * @author Jun
 * @date 2020/2/15.
 */
public class ListUtils {
    /**
     * List 转 Map
     *
     * @param list   欲转换的集合
     * @param getter Value到Key的映射
     * @param <K>    Key
     * @param <V>    Value
     * @return HashMap<K, V>
     */
    public static <K, V> Map<K, V> toMap(List<V> list, Function<V, K> getter) {
        Objects.requireNonNull(getter);
        final Map<K, V> result = new HashMap<>(list.size());
        for (V v : list) {
            result.put(getter.apply(v), v);
        }
        return result;
    }

    /**
     * 过滤集合
     *
     * @param list   待过滤集合
     * @param filter 过滤方法
     * @param remain 剩余元素集合
     * @param <T>    元素类型
     * @return 过滤后集合
     */
    public static <T> List<T> filter(List<T> list, Function<T, Boolean> filter, List<T> remain) {
        Objects.requireNonNull(filter);
        final List<T> result = new LinkedList<T>();
        for (T e : list) {
            if (filter.apply(e)) {
                result.add(e);
            } else {
                remain.add(e);
            }
        }
        return result;
    }
}

