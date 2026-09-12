import java.lang.*;
public class Assignment1q3 {
    public static void main(String[] args) {
        int age = 19;
        boolean validID = true;
        if (age >= 18) {
            if(validID) {
                System.out.println("You are eligible to vote.");
            }else {
                System.out.println("You are eligible by age,but dont have a valid ID.");
            }
        }else {
            System.out.println("You are not eligible to vote.");
        }
        int age1 = 17;
        boolean validID1 = true;
        if (age1 >= 18) {
            if(validID1) {
                System.out.println("You are eligible to vote.");
            }else {
                System.out.println("You are eligible by age,but dont have a valid ID.");
            }
        }else {
            System.out.println("You are not eligible to vote.");
        }
        int age2 = 20;
        boolean validID2 = false;
        if (age2 >= 18) {
            if(validID2) {
                System.out.println("You are eligible to vote.");
            }else {
                System.out.println("You are eligible by age,but dont have a valid ID.");
            }
        }else {
            System.out.println("You are not eligible to vote.");
        }
    }
    }