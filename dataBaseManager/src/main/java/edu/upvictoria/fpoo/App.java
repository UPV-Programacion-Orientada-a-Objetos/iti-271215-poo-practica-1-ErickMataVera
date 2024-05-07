package edu.upvictoria.fpoo;

import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        SQL sql = new SQL();
        Scanner scanner = new Scanner(System.in);

        sql.use("/home/erick/Escritorio/f");
        Comandos sl = new Comandos();



        while (true) {
            System.out.print("Ingrese su comando SQL (o 'exit' para salir): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("LOL")) {
                break;
            }

            try {
                sql.executeSQL(input);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }

    }
}
