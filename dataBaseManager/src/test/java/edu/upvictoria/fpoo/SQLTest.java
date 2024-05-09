package edu.upvictoria.fpoo;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

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
        // Arrange
        SQL sql = new SQL();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        sql.use(null);

      assertEquals("La ruta proporcionada es nula\n", outContent.toString());
    }


    //test de create table

    @Test
    public void testMostrarTablasConDirectorioValido() {
        // Configurar el entorno de prueba
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

}
