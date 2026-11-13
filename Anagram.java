import java.util.Scanner;
import java.util.Arrays;

public class Anagram {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter first string: ");
        String str1 = sc.hasNextLine() ? sc.nextLine() : "listen";

        System.out.print("Enter second string: ");
        String str2 = sc.hasNextLine() ? sc.nextLine() : "silent";

        // Remove spaces and convert to lowercase
        str1 = str1.replaceAll("\\s+", "").toLowerCase();
        str2 = str2.replaceAll("\\s+", "").toLowerCase();

        if (str1.length() != str2.length()) {
            System.out.println("Not an Anagram");
            return;
        }

        char[] a = str1.toCharArray();
        char[] b = str2.toCharArray();

        Arrays.sort(a);
        Arrays.sort(b);

        if (Arrays.equals(a, b)) {
            System.out.println("Result: Anagram");
        } else {
            System.out.println("Result: Not an Anagram");
        }

        sc.close();
    }
}
