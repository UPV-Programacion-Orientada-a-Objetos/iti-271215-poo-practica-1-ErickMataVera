package edu.upvictoria.fpoo;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SQL {
    public String rutaTrabajo;

    public SQL() {
    }

    public SQL(String rutaTrabajo) {
        this.rutaTrabajo = rutaTrabajo;
    }

    //COMANDO USE
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

    //COMANDO CREATE
    public void createTable(String tableName, String columns) {
        File tableFile = new File(this.rutaTrabajo, tableName + ".csv");

        try {
            if (tableFile.exists()) {
                System.out.println("Ya existe una tabla con ese nombre, no se pueden tener dos tablas con el mismo nombre");
                return;
            }

            String[] columnasSeparadas = columns.split(",");//AQUI LO SEPARAS REY ACUERDATE
            StringBuilder cabeceras = new StringBuilder();

            for (String columna : columnasSeparadas) {
                String[] parts = columna.trim().split("\\s+");
                String columName = parts[0];

                cabeceras.append(columName).append(",");
            }
            cabeceras.deleteCharAt(cabeceras.length() - 1);//ACUERDATE QUE EL BUENO ES STRING BUILDER

            try (FileWriter fw = new FileWriter(tableFile)) {
                fw.write(cabeceras.toString());
                System.out.println("Tabla creada exitosamente");
            }
        } catch (IOException e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    //COMANDO DROP
    public void dropTable(String tableName) {
        File table = new File(this.rutaTrabajo, tableName + ".csv");

        System.out.println("¿Estás seguro de que deseas eliminar la tabla? S/n)");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String dec = br.readLine().toLowerCase();

            if (dec.equals("s")) {
                if (table.delete()) {
                    System.out.println("Tabla eliminada correctamente.");
                    File tablaTipos = new File(rutaTrabajo, tableName + "_tipos.csv");
                    tablaTipos.delete();
                } else {
                    System.out.println("Error al eliminar la tabla.");
                }
            } else {
                System.out.println("Tabla no eliminada.");
            }
        } catch (IOException e) {
            System.out.println("Error de entrada/salida: " + e.getMessage());
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
            System.out.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }


    // COMANDO_INSERTAR
    public void insertInto(String tableName, String columns, String values) {
        File tableFile = new File(this.rutaTrabajo, tableName + ".csv");

        if (!tableFile.exists()) {
            System.out.println("La tabla no existe");
            return;
        }

        String[] columnNames = columns.split(",");
        String[] columnValues = values.split(",");

        if (columnNames.length != columnValues.length) {
            System.out.println("La cantidad de columnas y valores no coinciden, verifique su seleccion");
            return;
        }

        try (FileWriter fw = new FileWriter(tableFile, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {

            try (FileReader fr = new FileReader(tableFile);
                 BufferedReader br = new BufferedReader(fr)) {

                String cabLine = br.readLine();
                String[] cabezeras = cabLine.split(",");

                // Verificar si los nombres de las columnas coinciden con las cabeceras
                for (String columnName : columnNames) {
                    boolean cabezaraBandera = false;
                    for (String cabecera : cabezeras) {
                        if (columnName.trim().equalsIgnoreCase(cabecera.trim())) {
                            cabezaraBandera = true;
                            break;
                        }
                    }
                    if (!cabezaraBandera) {
                        System.out.println("La columna '" + columnName.trim() + "' no coincide con ninguna cabecera existente");
                        return;
                    }
                }
            }


            //AQUI CONSTRUYES LA LINEA DE VALORES A INSERTAR
            StringBuilder newLine = new StringBuilder();
            for (String columnValue : columnValues) {
                newLine.append(columnValue.trim()).append(",");
            }
            newLine.deleteCharAt(newLine.length() - 1);

            // Escribir la línea de valores en el archivo
            pw.println(newLine.toString());
            System.out.println("Datos insertados correctamente en la tabla");

        } catch (IOException e) {
            System.out.println("Error al insertar datos en la tabla: " + e.getMessage());
        }
    }

    //SOLO FUNCIONA PARA CONDICIONALES SIMPLES
    private boolean evaluarCondicion(String[] rowValues, String condition) {
        String[] parts = condition.split("=");
        if (parts.length == 2) {
            String columnName = parts[0].trim();
            String conditionValue = parts[1].trim();

            int columnIndex = -1;
            for (int i = 0; i < rowValues.length; i++) {
                if (rowValues[i].trim().equals(columnName)) {
                    columnIndex = i;
                    break;
                }
            }

            if (columnIndex != -1) {
                return rowValues[columnIndex].trim().equals(conditionValue);
            } else {
                System.out.println("La columna '" + columnName + "' no existe en el archivo");
                return false;
            }
        } else {
            System.out.println("La condicin no tiene el formato correcto");
            return false;
        }
    }

    //COMANDO DELETE
    public void deleteFromTable(String tableName, String condition) {
        File tableFile = new File(this.rutaTrabajo, tableName + ".csv");
        File tempFile = new File(this.rutaTrabajo, "temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(tableFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String headerLine = reader.readLine();
            if (headerLine != null) {
                writer.write(headerLine);
                writer.newLine();

                String[] columnNames = headerLine.split(",");

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] rowValues = line.split(",");

                    if (!evaluarCondicion(rowValues, condition)) {
                        writer.write(line);
                        writer.newLine();
                    }
                }

                if (tableFile.delete() && tempFile.renameTo(tableFile)) {
                    System.out.println("Filas eliminadas de la tabla '" + tableName + "' según la condición");
                } else {
                    System.out.println("Error al eliminar las filas de la tabla '" + tableName + "'.");
                }
            } else {
                System.out.println("El archivo está vacío o no contiene una línea de encabezado");
            }
        } catch (IOException e) {
            System.out.println("Error al procesar el archivo: " + e.getMessage());
        }
    }
}