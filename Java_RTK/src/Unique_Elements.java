import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

    public class Unique_Elements {


        public static <T> Set<T> getUniqueElements(ArrayList<T> list) {
            if (list == null || list.isEmpty()) {
                return new HashSet<>(); // Возвращаем пустой Set для null или пустого списка
            }

            return new HashSet<>(list); // HashSet автоматически отфильтровывает дубликаты
        }

        public static void main(String[] args) {
            ArrayList<Integer> numbers = new ArrayList<>();
            numbers.add(1);
            numbers.add(2);
            numbers.add(2);
            numbers.add(3);
            numbers.add(4);
            numbers.add(4);
            numbers.add(5);

            Set<Integer> uniqueNumbers = getUniqueElements(numbers);
            System.out.println("Уникальные элементы: " + uniqueNumbers); // Вывод: Уникальные элементы: [1, 2, 3, 4, 5]

            ArrayList<String> strings = new ArrayList<>();
            strings.add("apple");
            strings.add("banana");
            strings.add("apple");
            strings.add("orange");

            Set<String> uniqueStrings = getUniqueElements(strings);
            System.out.println("Уникальные строки: " + uniqueStrings); // Вывод: Уникальные строки: [orange, banana, apple]

            ArrayList<Integer> emptyList = new ArrayList<>();
            Set<Integer> uniqueFromEmpty = getUniqueElements(emptyList);
            System.out.println("Уникальные элементы из пустого списка: " + uniqueFromEmpty); // Вывод: Уникальные элементы из пустого списка: []

            ArrayList<Integer> nullList = null;
            Set<Integer> uniqueFromNull = getUniqueElements(nullList);
            System.out.println("Уникальные элементы из null списка: " + uniqueFromNull); // Вывод: Уникальные элементы из null списка: []
        }
    }


