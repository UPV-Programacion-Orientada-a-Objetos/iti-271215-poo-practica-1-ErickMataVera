package edu.upvictoria.fpoo;


import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SQL {
    private String rutaTrabajo;

    public SQL() {}
    public SQL(String rutaTrabajo) {this.rutaTrabajo = rutaTrabajo;}


    public void use(String path){
        File directorio = new File(path);

        if(directorio.exists() || directorio.isDirectory()){
            this.rutaTrabajo = path;
            System.out.println("Ruta establecida correctamete");
        }else{
            System.out.println("Verificar directorio de nuevo");
        }
    }

    //COMANDO_PARA_MOSTRAR
    public void showTables() {
        File [] files = new File(this.rutaTrabajo).listFiles();
        if(files != null){
            for (File file : files) {
                if(file.isFile() && file.getName().endsWith("csv")){
                    System.out.println(file.getName());
                }
            }
        }else{
            System.out.println("No existen tablas");
        }
    }


    //COMANDO_CREAR_TABLAS
    public void createTable(String tableName, String columns) {
        File tableFile = new File(this.rutaTrabajo, tableName + ".csv");

        if (tableFile.exists()) {
            System.out.println("la tabla ya existe en la base de datos, intente con otro nombre");
            return;
        }

        String []columnasSeparadas= columns.trim().split(",");//separar columas
        StringBuilder cabezeras = new StringBuilder();//cabezeras
        for (String columnita : columnasSeparadas) {

            String []partes = columnita.trim().split("\\s+");//sacar del string 2 partes creando un string que se llame partes

            String nombreColumna = partes[0];
            String tipoDato= partes[1];

            cabezeras.append(nombreColumna).append(",");
        }
        cabezeras.deleteCharAt(cabezeras.length() - 1);

        try (FileWriter fw = new FileWriter(tableFile)) {
            fw.write(cabezeras.toString());
            System.out.println("Tabla creada");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //COMANDO_BORRAR
    public void dropTable(String tableName) throws IOException {
        File table =  new File(this.rutaTrabajo,tableName + ".csv");
        System.out.print("¿Deseas eliminar la tabla?" + "S/N:");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String dec = null;

        try {
            dec = br.readLine().toLowerCase();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            br.close();
        }

        if (dec.equals("s")) {
            table.delete();
            System.out.println("La tabla ha sido eliminada");
        } else {
            System.out.println("Operacion cancelada");
        }
    }


    //COMANDO SELECT
    public   void select(String tableName) {
        String csvFilePath = this.rutaTrabajo + "/" + tableName + ".csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                for (String value : data) {
                    System.out.print(value + "\t");
                }
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("Error " + tableName + "': " + e.getMessage());
        }
    }


    public void lineaComandos(String sql) throws IOException {
        Matcher comparacion;

        if ((comparacion = Pattern.compile("USE (.+);$",Pattern.CASE_INSENSITIVE).matcher(sql)).find()) {
            String path = comparacion.group(1);
            use(path);
            return;
        }

        if ((comparacion = Pattern.compile("DROP TABLE (\\w+);$",Pattern.CASE_INSENSITIVE).matcher(sql)).find()) {
            String tableName = comparacion.group(1);
            dropTable(comparacion.group(1));
            return;
        }

        if ((comparacion = Pattern.compile("SHOW TABLES;$",Pattern.CASE_INSENSITIVE).matcher(sql)).find()) {
            showTables();
            return;
        }


        if((comparacion = Pattern.compile("SELECT\\s+\\*\\s+FROM (.+);$",Pattern.CASE_INSENSITIVE).matcher(sql)).find()){
            String tableName = comparacion.group(1);
            System.out.println(comparacion.group(1));
            select(tableName);
            return;
        }



        if ((comparacion = Pattern.compile("CREATE TABLE (\\w+) \\((.+)\\);$", Pattern.CASE_INSENSITIVE).matcher(sql)).find()) {
            String tableName = comparacion.group(1);
            String columnDefinitions = comparacion.group(2);
            createTable(tableName, columnDefinitions);
            return;
        }

        System.out.println("Comando no reconocido: " + sql);
    }


}
