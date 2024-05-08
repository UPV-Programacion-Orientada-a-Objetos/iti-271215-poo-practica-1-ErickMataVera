package edu.upvictoria.fpoo;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class SQLTest {
    //Test metodo use
    @Test
    void testUse_RutaExistente() {
        SQL sql = new SQL();
        String rutaExistente = "/home/erick/Escritorio/f";

        sql.use(rutaExistente);
    }
    //--
    @Test
    void testUse_RutaInexistente() {
        SQL sql = new SQL();
        String rutaInexistente = "/home/erick/Escritorio/ff";

        sql.use(rutaInexistente);
    }
    //FIn metodo use


    //Test show tables
    @Test
    void testShowTables_ExistenTablas() {
        SQL sql = new SQL();
        String rutaExistente = "/home/erick/Escritorio/f";
        sql.use(rutaExistente);

        File directorio = new File(rutaExistente);
        directorio.mkdir();
        File tabla1 = new File(rutaExistente, "tabla1.csv");
        File tabla2 = new File(rutaExistente, "tabla2.csv");
        try {
            tabla1.createNewFile();
            tabla2.createNewFile();
        } catch (Exception e) {
            fail("No se pudo crear los archivos CSV para el test.");
        }

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        sql.showTables();

        System.setOut(System.out);

        String expectedOutput = "tabla1.csv\ntabla2.csv\n";
    }
    //----
    @Test
    void testShowTables_NoExistenTablas() {
        SQL sql = new SQL();
        String rutaExistente = "/home/erick/Escritorio/f";
        sql.use(rutaExistente);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        sql.showTables();

        System.setOut(System.out);

    }
    //Fin show tables test


}
