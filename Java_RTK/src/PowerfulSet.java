import java.util.HashSet;
import java.util.Set;

public class PowerfulSet {


    public static <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1); // Создаем копию set1, чтобы не изменить его.
        result.retainAll(set2); // Оставляем в result только те элементы, которые есть и в set2.
        return result;
    }


    public static <T> Set<T> union(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1); // Создаем копию set1, чтобы не изменить его.
        result.addAll(set2); // Добавляем в result все элементы из set2.
        return result;
    }


    public static <T> Set<T> relativeComplement(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1); // Создаем копию set1, чтобы не изменить его.
        result.removeAll(set2); // Удаляем из result все элементы, которые есть и в set2.
        return result;
    }

    public static void main(String[] args) {
        Set<Integer> set1 = new HashSet<>();
        set1.add(1);
        set1.add(2);
        set1.add(3);

        Set<Integer> set2 = new HashSet<>();
        set2.add(0);
        set2.add(1);
        set2.add(2);
        set2.add(4);

        System.out.println("Set1: " + set1);
        System.out.println("Set2: " + set2);

        Set<Integer> intersection = PowerfulSet.intersection(set1, set2);
        System.out.println("Intersection: " + intersection); // Вывод: Intersection: [1, 2]

        Set<Integer> union = PowerfulSet.union(set1, set2);
        System.out.println("Union: " + union); // Вывод: Union: [0, 1, 2, 3, 4]

        Set<Integer> relativeComplement = PowerfulSet.relativeComplement(set1, set2);
        System.out.println("Relative complement: " + relativeComplement); // Вывод: Relative complement: [3]
    }
}
