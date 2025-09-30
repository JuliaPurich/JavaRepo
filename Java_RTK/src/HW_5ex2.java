
import java.util.Scanner;

public class HW_5ex2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String sequence = scanner.nextLine();

        int count = 0;
        int i = 0;
        while (i < sequence.length() - 4) {
            String substring = sequence.substring(i, i + 5);
            if (substring.equals(">>-->")) {
                count++;
                i += 5;
            } else if (substring.equals("<--<<")) {
                count++;
                i += 5;
            } else {
                i++;
            }
        }
        System.out.println(count);
    }
}