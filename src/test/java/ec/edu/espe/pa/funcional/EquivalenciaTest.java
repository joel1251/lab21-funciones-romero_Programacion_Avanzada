package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.funcional.ProcesadorFuncional;
import ec.edu.espe.pa.funcional.imperativo.ProcesadorImperativo;
import ec.edu.espe.pa.funcional.modelo.Incidencia;
import ec.edu.espe.pa.funcional.reglas.ReglasIncidencia;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Compara la solución imperativa de referencia con la versión funcional. */
class EquivalenciaTest {

    private final ProcesadorImperativo imperativo = new ProcesadorImperativo();
    private final ProcesadorFuncional funcional = new ProcesadorFuncional();

    static Stream<List<Incidencia>> escenarios() {
        return Stream.of(
                List.of(), // lista vacía
                List.of(new Incidencia("INC-001", "Caída del servidor", "ABIERTA", 5)),
                List.of(new Incidencia("INC-004", "Lentitud en reportes", "ABIERTA", 3),
                        new Incidencia("INC-005", "Error de acceso", "ABIERTA", 4),
                        new Incidencia("INC-003", "Fallo de respaldo", "CERRADA", 5)),
                List.of(new Incidencia("INC-006", "Consulta menor", "ABIERTA", 1),
                        new Incidencia("INC-007", "Actualizar certificado", "EN_PROCESO", 4)),
                Stream.generate(ReglasIncidencia.generadorSintetico()).limit(50).toList());
    }

    @ParameterizedTest
    @MethodSource("escenarios")
    void ambasSolucionesProducenElMismoResultado(List<Incidencia> datos) {
        List<Incidencia> copia = List.copyOf(datos);

        List<String> esperado = imperativo.titulosQueRequierenAtencion(datos);
        List<String> obtenido = funcional.titulosQueRequierenAtencion(datos);

        assertEquals(esperado, obtenido); // mismos elementos y en el mismo orden
        assertEquals(copia, datos);       // los datos originales no cambiaron
    }
}
