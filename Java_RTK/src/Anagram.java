import java.util.Arrays;
import java.util.Scanner;

public class Anagram {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите первую строку (s):");
        String s = scanner.nextLine();

        System.out.println("Введите вторую строку (t):");
        String t = scanner.nextLine();

        boolean isAnagram = areAnagrams(s, t);

        System.out.println(isAnagram);

        scanner.close();
    }


    public static boolean areAnagrams(String s, String t) {
        // 1. Базовая проверка: Если длины строк разные, то они не анаграммы.
        if (s.length() != t.length()) {
            return false;
        }

        // 2. Преобразование строк в массивы символов и сортировка.
        char[] sChars = s.toLowerCase().toCharArray(); // Приводим к нижнему регистру для регистронезависимой проверки.
        char[] tChars = t.toLowerCase().toCharArray(); // Это важное уточнение, которого не было в предыдущем ответе.
        Arrays.sort(sChars);
        Arrays.sort(tChars);

        // 3. Сравнение отсортированных массивов символов.
        return Arrays.equals(sChars, tChars);
    }
}

