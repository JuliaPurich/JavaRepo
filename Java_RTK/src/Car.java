import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Car {

    private String number;
    private String model;
    private String color;
    private long mileage;
    private double cost;

    public Car(String number, String model, String color, long mileage, double cost) {
        this.number = number;
        this.model = model;
        this.color = color;
        this.mileage = mileage;
        this.cost = cost;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getMileage() {
        return mileage;
    }

    public void setMileage(long mileage) {
        this.mileage = mileage;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Number: " + number + ", Model: " + model + ", Color: " + color + ", Mileage: " + mileage + ", Cost: " + cost;
    }

    public static void main(String[] args) {
        List<Car> cars = new ArrayList<>();
        cars.add(new Car("a123me", "Mercedes", "White", 0, 8300000));
        cars.add(new Car("b873of", "Volga", "Black", 0, 673000));
        cars.add(new Car("w487mn", "Lexus", "Grey", 76000, 900000));
        cars.add(new Car("p987hj", "Volga", "Red", 610, 704340));
        cars.add(new Car("c987ss", "Toyota", "White", 254000, 761000));
        cars.add(new Car("o983op", "Toyota", "Black", 698000, 740000));
        cars.add(new Car("p146op", "BMW", "White", 271000, 850000));
        cars.add(new Car("u893ii", "Toyota", "Purple", 210900, 440000));
        cars.add(new Car("l097df", "Toyota", "Black", 108000, 780000));
        cars.add(new Car("y876wd", "Toyota", "Black", 160000, 1000000));

        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите цвет для поиска: ");
        String colorToFind = scanner.nextLine();

        System.out.print("Введите пробег для поиска (0 для нулевого пробега): ");
        long mileageToFind = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Введите минимальную цену: ");
        long priceMin = scanner.nextLong();

        System.out.print("Введите максимальную цену: ");
        long priceMax = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Введите модель для поиска: ");
        String modelToFind = scanner.nextLine();

        // 1) Номера всех автомобилей, имеющих заданный цвет или нулевой пробег.

        System.out.print("Номера автомобилей по цвету или пробегу: ");
        cars.stream()
                .filter(car -> colorToFind.equals(car.getColor()) || car.getMileage() == mileageToFind)
                .forEach(car -> System.out.print(car.getNumber() + " "));
        System.out.println();

        // 3) Вывести цвет автомобиля с минимальной стоимостью.
        Optional<Car> minCostCar = cars.stream().min(Comparator.comparingDouble(Car::getCost));

        System.out.print("Цвет автомобиля с минимальной стоимостью: ");
        if (minCostCar.isPresent()) {
            System.out.println(minCostCar.get().getColor());
        } else {
            System.out.println("Автомобили отсутствуют.");
        }
    }
}
