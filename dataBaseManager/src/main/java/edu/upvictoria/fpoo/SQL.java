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
            System.out.println("La tabla ya existe");
            return;
        }

        String[] columnDefinitions = columns.split(",");
        StringBuilder headerBuilder = new StringBuilder();
        for (String columnDefinition : columnDefinitions) {
            String[] parts = columnDefinition.trim().split("\\s+");
            String columnName = parts[0];
            String dataType = parts[1];

            // Aplicar restricciones
            if (columnDefinition.contains("NOT NULL")) {
                // Aplicar restricción NOT NULL
            }

            Class<?> javaType = createTipoDato.get(dataType);
            if (javaType == null) {
                System.out.println("Tipo de datos no compatible: " + dataType);
                return;
            }

            headerBuilder.append(columnName).append(",");
        }
        headerBuilder.deleteCharAt(headerBuilder.length() - 1);

        try (FileWriter fw = new FileWriter(tableFile)) {
            fw.write(headerBuilder.toString());
            System.out.println("Tabla creada");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //Mapeos de datos------------------------------------------------
    private final Map<String,Class<?>> createTipoDato= new HashMap<>();
    {
        createTipoDato.put("INT",Integer.class);
    }

    private final Map<String,Class<?>>createTipoKey =  new HashMap<>();
    {
        createTipoKey.put("NOT NULL", Boolean.class);
        createTipoKey.put("PRIMARY KEY",Boolean.class);
    }
    //Fin de mapeos de datos-------------------------------------------



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


    //SOLO_USARSE_PARA_PRUEBAS_NO_ES_PARTE_DE_SQL
    public   void showTableData(String tableName) {
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


    public void executeSQL(String sql) throws IOException {
        Matcher comparacion;

        if ((comparacion = Pattern.compile("^USE (.+);$",Pattern.CASE_INSENSITIVE).matcher(sql)).find()) {
            String path = comparacion.group(1);
            use(path);
            return;
        }


        if ((comparacion = Pattern.compile("^DROP TABLE (\\w+);$",Pattern.CASE_INSENSITIVE).matcher(sql)).find()) {
            String tableName = comparacion.group(1);
            dropTable(comparacion.group(1));
            return;
        }

        if ((comparacion = Pattern.compile("^SHOW TABLES;$",Pattern.CASE_INSENSITIVE).matcher(sql)).find()) {
            showTables();
            return;
        }

        //acuerdate que asi no se muestran las tablas, esto namas es porque aun no sabes como hacer lo del select xd
        if((comparacion = Pattern.compile("^SELECT (.+) FROM (.+);$",Pattern.CASE_INSENSITIVE).matcher(sql)).find()){
            String tableName = comparacion.group(1);
            showTableData(tableName);
            return;
        }

        System.out.println("Comando no reconocido: " + sql);
    }


}
