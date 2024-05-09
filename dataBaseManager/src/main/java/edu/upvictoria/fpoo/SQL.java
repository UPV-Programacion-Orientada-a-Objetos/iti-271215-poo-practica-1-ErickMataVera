package edu.upvictoria.fpoo;


import java.io.*;
import java.sql.SQLOutput;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SQL {
    private String rutaTrabajo;

    public SQL() {
    }

    public SQL(String rutaTrabajo) {
        this.rutaTrabajo = rutaTrabajo;
    }

    public void use(String path) {
        if (path == null) {
            System.out.println("La ruta proporcionada es nula");
            return;
        }

        try {
            File directorio = new File(path);

            if (directorio.exists() || directorio.isDirectory()) {
                this.rutaTrabajo = path;
                System.out.println("Ruta establecida correctamente");
            } else {
                System.out.println("El directorio no existe o no es un directorio válido");
            }
        } catch (SecurityException e) {
            System.out.println("No se puede acceder al directorio debido a restricciones de seguridad");
        }
    }

    //COMANDO_PARA_MOSTRAR
    public void showTables() {
        try {
            if (this.rutaTrabajo == null) {
                System.out.println("La ruta de trabajo no está inicializada correctamente");
                return;
            }

            File[] files = new File(this.rutaTrabajo).listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.exists() && file.getName().endsWith(".csv")) {
                        System.out.println(file.getName());
                    }
                }
            } else {
                System.out.println("No se pudo acceder al directorio o no hay archivos presentes");
            }
        } catch (SecurityException e) {
            System.out.println("No se puede acceder al directorio debido a restricciones de seguridad");
        } catch (NullPointerException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("La ruta de trabajo no está inicializada correctamente");
        }
    }

    //COMANDO_CREAR_TABLAS
    public void createTable(String tableName, String columns) {
        File tableFile = new File(this.rutaTrabajo, tableName + ".csv");

        if (tableFile.exists()) {
            System.out.println("La tabla ya existe, no puede haber dos tablas con el mismo nombre");
            return;
        }

        //divido el string delimitida por ,
        String [] columnasSeparadas = columns.split(",");
        StringBuilder cabezeras= new StringBuilder(); //me permite constriur en el csv
        //StringBuilder tipoDatoBuilder = new StringBuilder();

        for (String columna : columnasSeparadas) {
            String [] parts = columna.trim().split("\\s+");
            String columName = parts[0];

            cabezeras.append(columName).append(",");
        }
        cabezeras.deleteCharAt(cabezeras.length() - 1);


        //nos ayuda a escribir una vez que tengo cabezeras delimitado por , lo pasa al csv
        try (FileWriter fw = new FileWriter(tableFile)) {
            fw.write(cabezeras.toString());
            System.out.println("tabla creada");
        } catch (IOException e) {
            System.out.println("error al crear la tabla:" + e.getMessage());
        }
    }


//COMANDO_BORRAR
    public void dropTable(String tableName) throws IOException {
        File table = new File(this.rutaTrabajo, tableName + ".csv");

        System.out.println("Estas seguro que deseas eliminar la tabla: S/N");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String dec = null;

        try {
            dec = br.readLine().toLowerCase();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            br.close();
        }

        if (dec.equals("s")) {
            table.delete();
            File tablaTipos = new File(rutaTrabajo, tableName + "_tipos.csv");
            tablaTipos.delete();
        } else {
            System.out.println("tabla no eliminada");
        }
    }

    //COMANDO SELECT
    public void select(String tableName) {
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



    // COMANDO_INSERTAR
    public void insertInto(String tableName, String columns, String values) {
        File tableFile = new File(this.rutaTrabajo, tableName + ".csv");

        if (!tableFile.exists()) {
            System.out.println("La tabla no existe");
            return;
        }

        String[] columnNames = columns.split(",");//esta las ingresa el usuario no se te olvide
        String[] columnValues = values.split(",");

        if (columnNames.length != columnValues.length) {
            System.out.println("La cantidad de columnas y valores no coinciden,verifiqu su eleccion");
            return;
        }

        try (FileReader fr = new FileReader(tableFile);
        BufferedReader br = new BufferedReader(fr)) {//recuerda que asi puede leer el nomb de la colum de un arch

            String cabLine = br.readLine();
            String[] cabezeras = cabLine.split(",");

            // si la cabecera coincide con los nombres de las columnas
            for (String columnName : columnNames) {
                boolean cabezaraBandera = false;    //tenemos dos for para iterarlos en la multp de ambos valores
                for (String cabezera : cabezeras) {
                    if (columnName.trim().equalsIgnoreCase(cabLine.trim())) {
                        cabezaraBandera = true;
                        break;
                    }
                }
                if (!cabezaraBandera) {
                    System.out.println("La columna '" + columnName.trim() + "' no coincide con ninguna cabecera existente");
                    return;
                }
            }

            try (FileWriter fw = new FileWriter(tableFile, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter pw = new PrintWriter(bw)) {

                StringBuilder newLine = new StringBuilder();
                for (String columnValue : columnValues) {
                    newLine.append(columnValue.trim()).append(",");
                }
                newLine.deleteCharAt(newLine.length() - 1);

                pw.println(newLine.toString());
                System.out.println("Datos insertados correctamente en la tabla");
            } catch (IOException e) {
                System.out.println("Error al insertar datos en la tabla: " + e.getMessage());
            }

        } catch (IOException e) {
            System.out.println("Error al leer la cabecera de la tabla: " + e.getMessage());
        }
    }



    public void deleteFromTable(String tableName, String condition) {
        File tableFile = new File(this.rutaTrabajo, tableName + ".csv");

        if(tableFile.exists()){
            System.out.println("El arch");
        }
    }



    public void dividir(String condition) {
        String[] condicion_dividida = condition.split("=");

        StringBuilder condicioncita = new StringBuilder();

        for (String conditions : condicion_dividida) {
            condicioncita.append(conditions.trim()).append(" = "); // Añadir un signo igual después de cada subcadena
        }

        if (condicioncita.length() > 0) {
            condicioncita.deleteCharAt(condicioncita.length() - 1);
        }


        System.out.println("Cadenas divididas: " + condicioncita.toString());
    }



    //funcion delete
    private static boolean dividirCondicion(String value, String condition) {
        String[] parts = condition.split("!=");
        if (parts.length == 2) {
            String conditionValue = parts[1].trim();
            return !value.equals(conditionValue);
        } else {
            parts = condition.split("=");
            if (parts.length == 2) {
                String conditionValue = parts[1].trim();
                return value.equals(conditionValue);
            } else {
                System.out.println("Condicion no valida");
                return false;
            }
        }
    }


}
