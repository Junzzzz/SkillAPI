package skillapi.utils;

import java.util.*;

/**
 * @author Jun
 * @date 2020/2/15.
 */
public class ListUtils {
    public static <T, R> List<List<T>> classify(List<T> list, Function<T, R> filter) {
        // 随便除以个2好了
        return classify(list, list.size() >> 1, filter);
    }

    public static <T, R> List<List<T>> classify(List<T> list, int typeNum, Function<T, R> filter) {
        requireNonNull(filter);
        Map<R, List<T>> result = new HashMap<R, List<T>>(typeNum);
        for (T t : list) {
            computeIfAbsent(result, filter.apply(t), new Function<R, List<T>>() {
                @Override
                public List<T> apply(R r) {
                    return new LinkedList<T>();
                }
            }).add(t);

        }
        return new ArrayList<List<T>>(result.values());
    }

    private static <K, V> V computeIfAbsent(Map<K, V> map, K key, Function<? super K, ? extends V> mappingFunction) {
        requireNonNull(mappingFunction);
        V v;
        if ((v = map.get(key)) == null) {
            V newValue;
            if ((newValue = mappingFunction.apply(key)) != null) {
                map.put(key, newValue);
                return newValue;
            }
        }

        return v;
    }

    public static <T, R> List<R> mapTo(List<T> list, Function<T, R> parser) {
        requireNonNull(parser);
        final List<R> result = new LinkedList<R>();
        for (T t : list) {
            final R temp = parser.apply(t);
            if (temp != null) {
                result.add(temp);
            }
        }
        return result;
    }

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
        requireNonNull(getter);
        final Map<K, V> result = new HashMap<K, V>(list.size());
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
        requireNonNull(filter);
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

    public static <T> List<T> filter(List<T> list, Function<T, Boolean> filter) {
        requireNonNull(filter);
        final List<T> result = new LinkedList<T>();
        for (T e : list) {
            if (filter.apply(e)) {
                result.add(e);
            }
        }
        return result;
    }

    public static <T, R> List<T> distinctNear(List<T> list, Function<T, R> getter) {
        return distinctNear(list, getter, false);
    }

    public static <T, R> List<T> distinctNear(List<T> list, Function<T, R> getter, boolean saveLast) {
        requireNonNull(getter);
        final LinkedList<T> result = new LinkedList<T>();
        T lastObject = null;
        R last = null;
        for (T t : list) {
            final R now = getter.apply(t);
            if (!now.equals(last)) {
                if (saveLast && !result.isEmpty() && !result.getLast().equals(lastObject)) {
                    result.add(lastObject);
                }
                result.add(t);
            }
            lastObject = t;
            last = now;
        }
        if (saveLast && !result.isEmpty() && !result.getLast().equals(lastObject)) {
            result.addLast(lastObject);
        }
        return result;
    }

    private static <T> T requireNonNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public interface Function<T, R> {
        /**
         * Applies this function to the given argument.
         *
         * @param t the function argument
         * @return the function result
         */
        R apply(T t);
    }
}

