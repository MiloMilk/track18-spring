package ru.track.cypher;

import java.util.*;

import org.jetbrains.annotations.NotNull;

public class Decoder {

    // Расстояние между A-Z -> a-z
    public static final int SYMBOL_DIST = 32;

    private Map<Character, Character> cypher;

    /**
     * Конструктор строит гистограммы открытого домена и зашифрованного домена
     * Сортирует буквы в соответствие с их частотой и создает обратный шифр Map<Character, Character>
     *
     * @param domain - текст по кторому строим гистограмму языка
     */
    public Decoder(@NotNull String domain, @NotNull String encryptedDomain) {

        Map<Character, Integer> domainHist = createHist(domain);
        Map<Character, Integer> encryptedDomainHist = createHist(encryptedDomain);

        cypher = new LinkedHashMap<>();

        for (int i = 0; i < domainHist.keySet().size(); i++ ){
            cypher.put((Character) encryptedDomainHist.keySet().toArray()[i], (Character) domainHist.keySet().toArray()[i]);
        }

    }

    public Map<Character, Character> getCypher() {
        return cypher;
    }

    /**
     * Применяет построенный шифр для расшифровки текста
     *
     * @param encoded зашифрованный текст
     * @return расшифровка
     */
    @NotNull
    public String decode(@NotNull String encoded) {

        String low = encoded.toLowerCase();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < low.length(); i++){
            if (Character.isLetter(low.charAt(i))) {
                builder.append(cypher.get(low.charAt(i)));
            } else {
                builder.append(low.charAt(i));
            }
        }

        return builder.toString();
    }

    /**
     * Считывает входной текст посимвольно, буквы сохраняет в мапу.
     * Большие буквы приводит к маленьким
     *
     *
     * @param text - входной текст
     * @return - мапа с частотой вхождения каждой буквы (Ключ - буква в нижнем регистре)
     * Мапа отсортирована по частоте. При итерировании на первой позиции наиболее частая буква
     */
    @NotNull
    Map<Character, Integer> createHist(@NotNull String text) {

        String low = text.toLowerCase();
        Map<Character, Integer> map = new HashMap<>();

        for (int i = 0; i < text.length(); i++){
            if (Character.isLetter(low.charAt(i))) {

                if (map.containsKey(low.charAt(i))) {
                    map.put(low.charAt(i), map.get(low.charAt(i)) + 1);
                } else {
                    map.put(low.charAt(i), 1);
                }
            }

        }

        List<Map.Entry<Character, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((o2, o1) -> o1.getValue().compareTo(o2.getValue()));

        Map<Character, Integer> result = new LinkedHashMap<>();

        for (Map.Entry<Character, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

}
