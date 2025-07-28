import java.util.Scanner;

public class HW_5ex1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char inputChar = scanner.next().charAt(0);
        String keyboard = "qwertyuiopasdfghjklzxcvbnm";

        int index = keyboard.indexOf(inputChar);
        int leftIndex = (index - 1 + keyboard.length()) % keyboard.length();
        System.out.println(keyboard.charAt(leftIndex));
    }
}



