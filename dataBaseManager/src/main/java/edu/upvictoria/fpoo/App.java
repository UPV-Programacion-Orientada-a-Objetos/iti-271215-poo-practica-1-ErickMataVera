package edu.upvictoria.fpoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) {
        SQL sql = new SQL();
        sql.use("/home/erick/Escritorio/f");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input;

        try {
            while (true) {
                System.out.print("Ingrese su comando SQL: ");
                input = br.readLine();

                if (input.equals("LOL")) {
                    break;
                }

                sql.lineaComandos(input);
            }

            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
