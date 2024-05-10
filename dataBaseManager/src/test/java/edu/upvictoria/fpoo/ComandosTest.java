package edu.upvictoria.fpoo;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ComandosTest {
    @Test
    public void testLineaComandos_ExceptionHandling() {
        Comandos comandos = new Comandos();
        String sql = "SELECT * FROM non_existing_table;";
        assertDoesNotThrow(() -> {
            comandos.lineaComandos(sql);
        });
    }
}