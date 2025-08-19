import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

class Person {
    private String name;
    private double money;
    private List<Product> products;

    public Person(String name, double money) {
        setName(name);
        setMoney(money);
        this.products = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Имя не может быть пустым.");
            throw new IllegalArgumentException("Имя не может быть пустым.");
        }
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        if (money < 0) {
            System.out.println("Деньги не могут быть отрицательными.");
            throw new IllegalArgumentException("Деньги не могут быть отрицательными.");
        }
        this.money = money;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void buyProduct(Product product) {
        if (money >= product.getPrice()) {
            products.add(product);
            money -= product.getPrice();
            System.out.println(name + " купил " + product.getName());
        } else {
            System.out.println(name + " не хватает денег на " + product.getName());
        }
    }
}

class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        setName(name);
        setPrice(price);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Название продукта не может быть пустым.");
            throw new IllegalArgumentException("Название продукта не может быть пустым.");
        }
        if (name.matches("\\d+")) {
            System.out.println("Название продукта не должно содержать только цифры.");
            throw new IllegalArgumentException("Название продукта не должно содержать только цифры.");
        }
        if (name.length() < 3) {
            System.out.println("Название продукта не может быть короче 3 символов.");
            throw new IllegalArgumentException("Название продукта не может быть короче 3 символов.");
        }
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            System.out.println("Цена продукта не может быть 0 или отрицательной.");
            throw new IllegalArgumentException("Цена продукта не может быть 0 или отрицательной.");
        }
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}

class DiscountProduct extends Product {
    private double discount;
    private LocalDate expirationDate;

    public DiscountProduct(String name, double price, double discount, LocalDate expirationDate) {
        super(name, price);
        setDiscount(discount);
        this.expirationDate = expirationDate;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        if (discount < 0 || discount > 1) {
            System.out.println("Скидка должна быть в диапазоне от 0 до 1.");
            throw new IllegalArgumentException("Скидка должна быть в диапазоне от 0 до 1.");
        }
        this.discount = discount;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public double getPrice() {
        if (LocalDate.now().isBefore(expirationDate)) {
            return super.getPrice() * (1 - discount);
        } else {
            return super.getPrice(); // Возвращает полную цену, если срок действия скидки истек
        }
    }

    @Override
    public String toString() {
        return "DiscountProduct{" +
                "name='" + getName() + '\'' +
                ", price=" + super.getPrice() + // Используем super.getPrice() чтобы получить базовую цену
                ", discount=" + discount +
                ", expirationDate=" + expirationDate +
                '}';
    }
}

public class App {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        List<Person> people = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        // Ввод покупателей
        System.out.println("Введите покупателей в формате: Имя=Деньги (Например: Иван=100). Для завершения введите 'END'");
        String input = scanner.nextLine();
        while (!input.equals("END")) {
            if (input.contains("=")) { // Добавлена проверка на наличие "="
                String[] parts = input.split("=");
                String name = parts[0];
                double money = Double.parseDouble(parts[1]);
                try {
                    Person person = new Person(name, money);
                    people.add(person);
                } catch (IllegalArgumentException e) {
                    // Обработка исключений в конструкторе Person происходит в самом конструкторе.
                    // Ничего дополнительно делать не нужно.
                }
            } else {
                System.out.println("Неверный формат ввода покупателя. Используйте 'Имя=Деньги' или 'END'.");
            }
            input = scanner.nextLine();
        }

        // Ввод продуктов
        System.out.println("Введите продукты. Для обычного продукта: Название=Цена (Например: Хлеб=25).");
        System.out.println("Для скидочного продукта: Название=Цена=Скидка=Год-Месяц-День (Например: Яблоко=50=0.2=2024-01-01). Для завершения введите 'END'");
        input = scanner.nextLine();
        while (!input.equals("END")) {
            if (input.contains("=")) {
                String[] parts = input.split("=");
                try {
                    if (parts.length == 2) {
                        // Обычный продукт
                        String name = parts[0];
                        double price = Double.parseDouble(parts[1]);
                        Product product = new Product(name, price);
                        products.add(product);
                    } else if (parts.length == 4) {
                        // Скидочный продукт
                        String name = parts[0];
                        double price = Double.parseDouble(parts[1]);
                        double discount = Double.parseDouble(parts[2]);
                        LocalDate expirationDate = LocalDate.parse(parts[3]);
                        DiscountProduct product = new DiscountProduct(name, price, discount, expirationDate);
                        products.add(product);
                    } else {
                        System.out.println("Неверный формат ввода продукта.");
                    }
                } catch (IllegalArgumentException e) {
                    // Обработка исключений в конструкторах Product/DiscountProduct происходит в самих конструкторах.
                    // Ничего дополнительно делать не нужно.
                } catch (Exception e) {
                    System.out.println("Ошибка при разборе ввода продукта: " + e.getMessage()); // Ловим NumberFormatException и DateTimeParseException
                }
            } else {
                System.out.println("Неверный формат ввода продукта. Используйте 'Название=Цена' или 'Название=Цена=Скидка=Год-Месяц-День' или 'END'.");
            }
            input = scanner.nextLine();
        }

        // Процесс покупки
        System.out.println("Начинаем процесс покупки. Покупатели будут по очереди выбирать продукты.");
        System.out.println("Введите название продукта для покупки покупателем.  Для завершения введите 'END'");

        int personIndex = 0;
        input = scanner.nextLine();
        while (!input.equals("END")) {
            Person currentPerson = people.get(personIndex % people.size()); //Циклический перебор покупателей

            Product chosenProduct = null;
            for (Product product : products) {
                if (product.getName().equals(input)) {
                    chosenProduct = product;
                    break;
                }
            }

            if (chosenProduct != null) {
                currentPerson.buyProduct(chosenProduct);
            } else {
                System.out.println("Продукт с названием '" + input + "' не найден.");
            }

            personIndex++;
            input = scanner.nextLine();
        }

        // Вывод информации о покупках каждого покупателя
        System.out.println("\n--- Результаты ---");
        for (Person person : people) {
            System.out.print(person.getName() + " - ");
            if (person.getProducts().isEmpty()) {
                System.out.println("Ничего не куплено");
            } else {
                List<String> productNames = new ArrayList<>();
                for (Product product : person.getProducts()) {
                    productNames.add(product.getName());
                }
                System.out.println(String.join(", ", productNames));
            }
        }

        scanner.close();
    }
}
