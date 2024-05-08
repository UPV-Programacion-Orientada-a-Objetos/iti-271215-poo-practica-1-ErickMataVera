package edu.upvictoria.fpoo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Comandos extends SQL{
    private static final Map<String, Runnable> comandos = new HashMap<>();
    {
        comandos.put("SHOW TABLES",this::showTables);
        comandos.put("DROP TABLE", () -> {
            try {
                dropTable("upv");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        comandos.put("CREATE TABLE",()->createTable("Erick","edad"));
        comandos.put("DATA",()->select("Erick"));
    }

    public void lineaComandos(String comando) {
        Runnable function = comandos.get(comando.toUpperCase());
        if (function != null) {
            function.run();
        }else{
            System.out.println("Comando no encontrado");
        }
    }

}
