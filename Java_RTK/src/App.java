import java.util.Random;
import java.util.Scanner;

class Телевизор {
    private String марка;
    private int диагональ;
    private boolean смартТВ;

    // Конструктор класса Телевизор
    public Телевизор(String марка, int диагональ, boolean смартТВ) {
        this.марка = марка;
        this.диагональ = диагональ;
        this.смартТВ = смартТВ;
    }

    // Методы для получения информации о телевизоре
    public String getМарка() {
        return марка;
    }

    public int getДиагональ() {
        return диагональ;
    }

    public boolean isСмартТВ() {
        return смартТВ;
    }

    @Override
    public String toString() {
        return "Телевизор: " + марка + ", Диагональ: " + диагональ + " дюймов, Смарт ТВ: " + (смартТВ ? "Да" : "Нет");
    }
}

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Создание экземпляров телевизоров с вводом от пользователя
        System.out.println("Введите количество телевизоров:");
        int количество = scanner.nextInt();

        Телевизор[] телевизоры = new Телевизор[количество];

        for (int i = 0; i < количество; i++) {
            System.out.println("Телевизор " + (i + 1) + ":");
            System.out.print("Введите марку: ");
            String марка = scanner.next();

            // Генерация диагонали от 32 до 75 дюймов
            int диагональ = 32 + random.nextInt(44); // 0-44 + 32 = 32-75

            boolean смартТВ = random.nextBoolean(); // Случайное значение для Смарт ТВ

            телевизоры[i] = new Телевизор(марка, диагональ, смартТВ);
        }

        // Вывод информации о всех телевизорах
        System.out.println("\nИнформация о телевизорах:");
        for (Телевизор телевизор : телевизоры) {
            System.out.println(телевизор);
        }

        scanner.close();
    }
}
