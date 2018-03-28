package ru.track.cypher;

import java.util.*;

import org.jetbrains.annotations.NotNull;

/**
 * Вспомогательные методы шифрования/дешифрования
 */
public class CypherUtil {

    public static final String SYMBOLS = "abcdefghijklmnopqrstuvwxyz";

    /**
     * Генерирует таблицу подстановки - то есть каждой буква алфавита ставится в соответствие другая буква
     * Не должно быть пересечений (a -> x, b -> x). Маппинг уникальный
     *
     * @return таблицу подстановки шифра
     */
    @NotNull
    public static Map<Character, Character> generateCypher() {

        List<Character> list = new ArrayList<>();
        Map<Character, Character> map = new HashMap<>();

        for (int i = 0; i < SYMBOLS.length(); i++){
            list.add(SYMBOLS.charAt(i));
        }

        List<Character> code = new ArrayList<>(list);
        Collections.shuffle(code);

        for (int i = 0; i < SYMBOLS.length(); i++){
            map.put(list.get(i),code.get(i));
        }

        return map;
    }

}
