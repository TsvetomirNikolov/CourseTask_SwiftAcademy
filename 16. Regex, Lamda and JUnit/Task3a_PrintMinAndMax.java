package pkg03.console.applications;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class Task3a_PrintMinAndMax {
     public static void main(String[] args) {
        Scanner scanner = new Scanner (System.in);
//        First way with lists
        int inputDigit = Integer.parseInt(scanner.nextLine());
        ArrayList<Integer> list = new ArrayList<>();
        for (int start = 1; start <= inputDigit; start++) {
            int secondDigit = scanner.nextInt();
            list.add(secondDigit);
        }
        System.out.print(Collections.min(list)+ " ");
        System.out.println(Collections.max(list));
        
//        Second way to find Min and Max values
        int inputDigits = Integer.parseInt(scanner.nextLine());
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int start = 1; start <= inputDigits; start++) {
            int val = scanner.nextInt();
            if (val == 0) {
                break;
            }
            if (val < min) {
                min = val;
            }
            if (val > max) {
                max = val;
            }
        }
        System.out.println(min);
        System.out.println(max);
    }
}

   
    

