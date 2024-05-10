package edu.upvictoria.fpoo;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class SQLTest {
    //Test metodo use
    @Test
    public void testUsarDirectorioExistente() {
        SQL sql = new SQL();
        String path = "/home";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        sql.use(path);

        assertEquals("Ruta establecida correctamente\n", outContent.toString());
    }

    @Test
    public void testUsarDirectorioNoExistente() {
        SQL sql = new SQL();
        String path = "/path/to/non/existing/directory";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        sql.use(path);

        assertEquals("El directorio no existe o no es un directorio válido\n", outContent.toString());
    }

    @Test
    public void testUsarRutaNula() {
        SQL sql = new SQL();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        sql.use(null);

      assertEquals("La ruta proporcionada es nula\n", outContent.toString());
    }



    @Test
    public void testMostrarTablasConDirectorioValido() {
        SQL sql = new SQL();
        String existingDirectory = "/home";
        sql.use(existingDirectory);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        sql.showTables();

        String expectedOutput = "tabla1.csv\ntabla2.csv\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testMostrarTablasConDirectorioInvalido() {
        SQL sql = new SQL();
        String nonExistingDirectory = "/path/to/non/existing/directory";
        sql.use(nonExistingDirectory);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        sql.showTables();

        String expectedOutput = "La ruta de trabajo no está inicializada correctamente\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testCreateTable() {
        SQL sql = new SQL();
        String tableName = "hola";
        String columns = "id INT, name VARCHAR(50), age INT";

        sql.createTable(tableName, columns);
        File tableFile = new File("/home", tableName + ".csv");

        assertTrue(!tableFile.exists());
    }

    @Test
    public void testCreateTable_TablaExistente() {
        SQL sql = new SQL();
        String tableName = "hola";
        String columns = "id INT, name VARCHAR(50), age INT";

        try {
            String existingTableFilePath = "/home/erick/Escritorio" + File.separator + tableName + ".csv";
            File existingTableFile = new File(existingTableFilePath);

            FileWriter fileWriter = new FileWriter(existingTableFile);
            fileWriter.write("existing_table_id, existing_table_name, existing_table_age\n");
            fileWriter.close();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));

            sql.createTable(tableName, columns);

            System.setOut(System.out);

            assertEquals("Ya existe una tabla con ese nombre, no se pueden tener dos tablas con el mismo nombre\n", outputStream.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    void dropTable_ConfirmDelete() throws IOException {
        SQL sql = new SQL();
        sql.rutaTrabajo = "/home/erick/Escritorio";
        String tableName = "test_table";
        File tableFile = new File(sql.rutaTrabajo, tableName + ".csv");
        tableFile.createNewFile();

        ByteArrayInputStream in = new ByteArrayInputStream("s\n".getBytes());
        System.setIn(in);

        sql.dropTable(tableName);

        assertFalse(tableFile.exists());
    }

    @Test
    void dropTable_CancelarDelete() throws IOException {
        SQL sql = new SQL();
        sql.rutaTrabajo = "/home/erick/Escritorio";
        String tableName = "test_table";
        File tableFile = new File(sql.rutaTrabajo, tableName + ".csv");
        tableFile.createNewFile();

        ByteArrayInputStream in = new ByteArrayInputStream("n\n".getBytes());
        System.setIn(in);

        sql.dropTable(tableName);

        assertTrue(tableFile.exists());
    }

    @Test
    void dropTable_IOError() {
        SQL sql = new SQL();
        sql.rutaTrabajo = "/home/erick/Escritorio";
        ByteArrayInputStream in = new ByteArrayInputStream("s\n".getBytes());
        System.setIn(in);

        assertDoesNotThrow(() -> sql.dropTable("test_table"));
    }

    @Test
    public void testSelect_LecturaErronea() {
        final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        SQL sql = new SQL();

        sql.select("non_existing_table");

        assertEquals("Error al leer el archivo CSV: null/non_existing_table.csv (No existe el archivo o el directorio)\n", outputStreamCaptor.toString());
    }

    @Test
    public void testInsertInto_NoInsertar() {

        final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        SQL sql = new SQL();

        sql.insertInto("non_existing_table", "column1,column2", "value1,value2");

        assertTrue(outputStreamCaptor.toString().contains("Error al insertar datos en la tabla"));
    }

    @Test
    public void testDeleteFromTable_ArchivoNoprocesado() {
        final ByteArrayOutputStream errorStreamCaptor = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errorStreamCaptor));
        SQL sql = new SQL();

        sql.deleteFromTable("non_existing_table", "condition");

        assertTrue(errorStreamCaptor.toString().contains("Error al procesar el archivo: non_existing_table.csv (No existe el archivo o el directorio)\n"));
    }







}


