package edu.upvictoria.fpoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) {
        Comandos comandos = new Comandos();
        comandos.use("/home/erick/Escritorio/f"); //para no poner la ruta a cada rato xd

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input;

        try {
            while (true) {
                System.out.print("Ingrese su comando SQL: ");
                input = br.readLine();

                if (input.equals("LOL")) {
                    break;
                }
                comandos.lineaComandos(input);
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
