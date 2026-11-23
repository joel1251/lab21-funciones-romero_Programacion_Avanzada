package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.contrato.ReglaIncidencia;
import ec.edu.espe.pa.funcional.modelo.Incidencia;
import ec.edu.espe.pa.funcional.reglas.PrioridadAlta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ContratoIncidenciaTest {
    @ParameterizedTest
    @CsvSource({"3, false", "4, true", "5, true"})
    void lasTresImplementacionesConservanLaRegla(int prioridad, boolean esperado) {
        Incidencia caso = new Incidencia("INC-TEST", "Prueba", "ABIERTA", prioridad);
        ReglaIncidencia clase = new PrioridadAlta();
        ReglaIncidencia anonima = new ReglaIncidencia() {
            @Override
            public boolean evaluar(Incidencia incidencia) {
                return incidencia.prioridad() >= 4;
            }
        };
        ReglaIncidencia lambda = incidencia -> incidencia.prioridad() >= 4;
        assertEquals(esperado, clase.evaluar(caso));
        assertEquals(esperado, anonima.evaluar(caso));
        assertEquals(esperado, lambda.evaluar(caso));
    }

    @Test
    void composicionExigeAmbasCondiciones() {
        ReglaIncidencia abierta = incidencia -> "ABIERTA".equals(incidencia.estado());
        ReglaIncidencia atencion = abierta.y(new PrioridadAlta());
        assertTrue(atencion.evaluar(new Incidencia("A", "Prueba", "ABIERTA", 4)));
        assertFalse(atencion.evaluar(new Incidencia("B", "Prueba", "ABIERTA", 3)));
        assertFalse(atencion.evaluar(new Incidencia("C", "Prueba", "CERRADA", 5)));
    }

    @Test
    void composicionConservaElCortocircuito() {
        ReglaIncidencia falsa = incidencia -> false;
        ReglaIncidencia noDebeEjecutarse = incidencia -> {
            throw new AssertionError("La segunda regla no debe evaluarse");
        };
        assertFalse(falsa.y(noDebeEjecutarse)
                .evaluar(new Incidencia("A", "Prueba", "ABIERTA", 4)));
    }
}
