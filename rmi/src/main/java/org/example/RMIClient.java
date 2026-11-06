package org.example;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {

    public static void main(String[] args) {

        try {
            Registry registry =
                    LocateRegistry.getRegistry("localhost", 1099);

            Calculator calculator =
                    (Calculator) registry.lookup("CalculatorService");

            int sum = calculator.add(10, 20);
            int difference = calculator.subtract(20, 5);

            System.out.println("Sum: " + sum);
            System.out.println("Difference: " + difference);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
