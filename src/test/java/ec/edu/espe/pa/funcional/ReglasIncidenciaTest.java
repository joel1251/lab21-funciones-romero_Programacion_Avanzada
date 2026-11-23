package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.modelo.Incidencia;
import ec.edu.espe.pa.funcional.reglas.ReglasIncidencia;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.*;
import static org.junit.jupiter.api.Assertions.*;

class ReglasIncidenciaTest {
    @ParameterizedTest
    @CsvSource({
            "ABIERTA, 5, true, true, true, CRITICA",
            "ABIERTA, 2, true, false, false, MEDIA",
            "CERRADA, 5, false, true, false, CRITICA",
            "ABIERTA, 3, true, false, false, MEDIA",
            "ABIERTA, 4, true, true, true, ALTA",
            "ABIERTA, 1, true, false, false, BAJA",
            "EN_PROCESO, 4, false, true, false, ALTA"
    })
    void funcionesRespetanLosCasosDelDominio(String estado, int prioridad,
                                            boolean abierta, boolean alta,
                                            boolean atencion, String categoria) {
        Incidencia caso = new Incidencia("INC-TEST", "Caso sintético", estado, prioridad);
        assertAll(
                () -> assertEquals(abierta, ESTA_ABIERTA.test(caso)),
                () -> assertEquals(alta, PRIORIDAD_ALTA.test(caso)),
                () -> assertEquals(atencion, REQUIERE_ATENCION.test(caso)),
                () -> assertEquals(categoria, CATEGORIA.apply(caso)));
    }

    @Test
    void resumenConservaIdentificadorYTitulo() {
        Incidencia caso = new Incidencia("INC-001", "Caída del servidor", "ABIERTA", 5);
        assertEquals("INC-001 - Caída del servidor", RESUMEN.apply(caso));
    }

    @Test
    void notificarEscribeElResumenEsperado() {
        Incidencia caso = new Incidencia("INC-001", "Error de acceso", "ABIERTA", 4);
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream anterior = System.out;
        try (PrintStream temporal = new PrintStream(salida, true, StandardCharsets.UTF_8)) {
            System.setOut(temporal);
            NOTIFICAR.accept(caso);
        } finally {
            System.setOut(anterior);
        }
        assertEquals("[ATENCION] INC-001 - Error de acceso" + System.lineSeparator(),
                salida.toString(StandardCharsets.UTF_8));
    }

    @Test
    void generadorProduceIdsDistintosYPrioridadesValidas() {
        Supplier<Incidencia> generador = ReglasIncidencia.generadorSintetico();
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < 15; i++) {
            Incidencia caso = generador.get();
            assertTrue(ids.add(caso.id()), "El generador no debe repetir identificadores");
            assertTrue(caso.prioridad() >= 1 && caso.prioridad() <= 5);
            assertTrue(Set.of("ABIERTA", "EN_PROCESO", "CERRADA").contains(caso.estado()));
        }
    }

    @Test
    void cadaGeneradorTieneSuPropioContador() {
        Supplier<Incidencia> primero = generadorSintetico();
        Supplier<Incidencia> segundo = generadorSintetico();
        assertEquals("INC-001", primero.get().id());
        assertEquals("INC-002", primero.get().id());
        assertEquals("INC-001", segundo.get().id());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 6})
    void recordRechazaPrioridadesFueraDeRango(int prioridad) {
        assertThrows(IllegalArgumentException.class,
                () -> new Incidencia("INC-ERROR", "Fuera del rango", "ABIERTA", prioridad));
    }
}
