package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.funcional.ProcesadorFuncional;
import ec.edu.espe.pa.funcional.imperativo.ProcesadorImperativo;
import ec.edu.espe.pa.funcional.modelo.Incidencia;

import java.util.List;

import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.CATEGORIA;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.ESTA_ABIERTA;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.NOTIFICAR;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.REQUIERE_ATENCION;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.RESUMEN;

/** Ejecuta la versión imperativa y la funcional con los mismos datos. */
public class App {
    public static void main(String[] args) {
        List<Incidencia> datos = List.of(
                new Incidencia("INC-001", "Caída del servidor", "ABIERTA", 5),
                new Incidencia("INC-002", "Error tipográfico en el menú", "ABIERTA", 2),
                new Incidencia("INC-003", "Fallo de respaldo nocturno", "CERRADA", 5),
                new Incidencia("INC-004", "Lentitud en reportes", "ABIERTA", 3),
                new Incidencia("INC-005", "Error de acceso al sistema", "ABIERTA", 4),
                new Incidencia("INC-006", "Actualizar certificado", "EN_PROCESO", 4));

        System.out.println("== Versión imperativa ==");
        new ProcesadorImperativo().mostrarQueRequierenAtencion(datos);

        ProcesadorFuncional procesador = new ProcesadorFuncional();
        System.out.println("== Versión funcional ==");
        procesador.titulosQueRequierenAtencion(datos).forEach(System.out::println);

        System.out.println("== Notificaciones ==");
        procesador.ejecutar(datos, REQUIERE_ATENCION, NOTIFICAR);

        System.out.println("== Incidencias abiertas con su categoría ==");
        procesador.seleccionar(datos, ESTA_ABIERTA,
                        i -> RESUMEN.apply(i) + " [" + CATEGORIA.apply(i) + "]")
                .forEach(System.out::println);
    }
}
