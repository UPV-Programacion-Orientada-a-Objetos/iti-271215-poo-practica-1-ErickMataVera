package edu.upvictoria.fpoo;


import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Comandos extends SQL{
    public Comandos() {}

    public Comandos (String ruta){
    super(ruta);

    }
    public void lineaComandos(String sql) throws IOException {
        Matcher comparacion;

        if ((comparacion = Pattern.compile("USE (.+);$", Pattern.CASE_INSENSITIVE).matcher(sql)).find()) {
            String path = comparacion.group(1);
            use(path);
            return;
        }

        if ((comparacion = Pattern.compile("DROP TABLE (\\w+);$", Pattern.CASE_INSENSITIVE).matcher(sql)).find()) {
            String tableName = comparacion.group(1);
            dropTable(comparacion.group(1));
            return;
        }

        if ((comparacion = Pattern.compile("SHOW TABLES;$", Pattern.CASE_INSENSITIVE).matcher(sql)).find()) {
            showTables();
            return;
        }


        if ((comparacion = Pattern.compile("SELECT\\s+\\*\\s+FROM (.+);$", Pattern.CASE_INSENSITIVE).matcher(sql)).find()) {
            String tableName = comparacion.group(1);
            System.out.println(comparacion.group(1));
            select(tableName);
            return;
        }

        if ((comparacion = Pattern.compile("CREATE TABLE (\\w+) \\((.+)\\);", Pattern.CASE_INSENSITIVE).matcher(sql)).find()) {
            String tableName = comparacion.group(1);
            String columnasSeparadas = comparacion.group(2).trim();
            System.out.println(columnasSeparadas);

            createTable(tableName, columnasSeparadas);
            return;
        }

        if ((comparacion = Pattern.compile("INSERT INTO (\\w+) \\((.+)\\) VALUES \\((.+)\\);", Pattern.CASE_INSENSITIVE).matcher(sql)).find()) {
            String tableName = comparacion.group(1);
            String columns = comparacion.group(2);
            String values = comparacion.group(3);

            insertInto(tableName, columns, values);
            return;
        }

        if((comparacion=Pattern.compile("DELETE\\s+FROM\\s+(\\w+)\\s+WHERE\\s+(.+)$", Pattern.CASE_INSENSITIVE).matcher(sql)).find()) {
            String tableName = comparacion.group(1);
            String condition = comparacion.group(2);

            deleteFromTable(tableName,condition);

            return;
        }


        System.out.println("Comando no detectado");

    }
}
