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

            System.out.println("Имя не может быть пустым");

            throw new IllegalArgumentException("Имя не может быть пустым");

        }

        if (name.length() < 3) {

            System.out.println("Имя не может быть короче 3 символов");

            throw new IllegalArgumentException("Имя не может быть короче 3 символов");

        }

        this.name = name;

    }

    public double getMoney() {

        return money;

    }

    public void setMoney(double money) {

        if (money < 0) {

            System.out.println("Деньги не могут быть отрицательными");

            throw new IllegalArgumentException("Деньги не могут быть отрицательными");

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

            System.out.println(name + " не может позволить себе " + product.getName());

        }

    }

    @Override

    public String toString() {

        return "Person{" +

                "name='" + name + '\'' +

                ", money=" + money +

                ", products=" + products +

                '}';

    }

    @Override

    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return Double.compare(person.money, money) == 0 && Objects.equals(name, person.name) && Objects.equals(products, person.products);

    }

    @Override

    public int hashCode() {

        return Objects.hash(name, money, products);

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

        this.name = name;

    }

    public double getPrice() {

        return price;

    }

    public void setPrice(double price) {

        if (price < 0) {

            System.out.println("Цена продукта не может быть отрицательной.");

            throw new IllegalArgumentException("Цена продукта не может быть отрицательной.");

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

public class App {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        List<Person> people = new ArrayList<>();

        List<Product> products = new ArrayList<>();

        // Ввод покупателей

        System.out.println("Введите покупателей в формате: Имя=Деньги (Например: Иван=100). Для завершения введите 'END'");

        String input = scanner.nextLine();

        while (!input.equals("END")) {

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

            input = scanner.nextLine();

        }

        // Ввод продуктов

        System.out.println("Введите продукты в формате: Название=Цена (Например: Хлеб=25). Для завершения введите 'END'");

        input = scanner.nextLine();

        while (!input.equals("END")) {

            String[] parts = input.split("=");

            String name = parts[0];

            double price = Double.parseDouble(parts[1]);

            try {

                Product product = new Product(name, price);

                products.add(product);

            } catch (IllegalArgumentException e) {

                // Обработка исключений в конструкторе Product происходит в самом конструкторе.

                // Ничего дополнительно делать не нужно.

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
