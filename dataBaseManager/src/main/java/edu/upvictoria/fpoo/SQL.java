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
        if(files!=null){
            for(File file : files){
                if(file.exists()&& file.getName().endsWith(".csv")){
                    System.out.println(file.getName());
                }
            }
        }else{
            System.out.println("No existen tablas");
        }
    }


    //COMANDO_CREAR_TABLAS



    public void createTable(String nombreTabla, String columnas) {
        File table = new File(this.rutaTrabajo, nombreTabla + ".csv");

        if (table.exists()) {
            System.out.println("La tabla ya existe");
            return;
        }

        String[] columnasSeparadas = columnas.trim().split(",");
        StringBuilder cabeceras = new StringBuilder();
        System.out.println(columnasSeparadas.length);

        File archivoTipos = new File(this.rutaTrabajo, nombreTabla + "_tipos.csv");

        try (FileWriter fwTipos = new FileWriter(archivoTipos)) {
            fwTipos.write("columna,tipo de dato,Obligatorio,Llave\n");

            for (String columna : columnasSeparadas) {
                String[] partes = columna.trim().split("\\s+");

                String nombreColumna = partes[0];
                String tipoDato = partes[1];
                String obligatorio = partes[2];
                String llave = partes[3];
                //te habientas validaciones de esta cosa por dato, ya que si ingresa un dato que no es pues lo retachas

                cabeceras.append(nombreColumna).append(",");
                fwTipos.append(nombreColumna).append(",").append(tipoDato).append(",").append(obligatorio).append(",").append(llave).append(",");
                System.out.println();
            }

            cabeceras.deleteCharAt(cabeceras.length() - 1);

            try (FileWriter fw = new FileWriter(table)) {
                fw.write(cabeceras.toString());
                System.out.println("Tabla creada");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //COMANDO_BORRAR
    public void dropTable(String tableName) throws IOException {
        File table =  new File(this.rutaTrabajo,tableName + ".csv");

        System.out.println("Estas seguro que deseas eliminar la tabla: S/N");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String dec = null;

        try{
             dec = br.readLine().toLowerCase();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            br.close();
        }

        if(dec.equals("s")){
            table.delete();
        }else{
            System.out.println("tabla no eliminada");
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
            System.out.println(e.getMessage());
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



        if ((comparacion = Pattern.compile("CREATE TABLE (\\w+) \\((.+)\\);", Pattern.CASE_INSENSITIVE).matcher(sql)).find()) {
            String tableName = comparacion.group(1);
            String columnasSeparadas = comparacion.group(2).trim();
            System.out.println(columnasSeparadas);

            if (columnasSeparadas != null) {
                createTable(tableName, columnasSeparadas);
                return;
            }
        }

        System.out.println("Comando no reconocido: " + sql);
    }


}
