# Laboratorio 2.1 Funciones y contratos funcionales

Avance implementado hasta la fase 4. Paquete base: `ec.edu.espe.pa.funcional`.

Se utiliza JDK 23 para ejecutar Maven y el programa. El `pom.xml` conserva
`maven.compiler.release=21`, por lo que la compilación genera clases compatibles
con Java 21. Esto no equivale a afirmar que se ejecutó bajo JDK 21.

## Ejecución en PowerShell

Desde esta carpeta, con Java y Maven disponibles:

```powershell
mvn test
java -cp target/classes ec.edu.espe.pa.funcional.Demostracion 2
java -cp target/classes ec.edu.espe.pa.funcional.Demostracion 3
java -cp target/classes ec.edu.espe.pa.funcional.Demostracion 4
```

Sin argumento, `Demostracion` ejecuta las fases 2, 3 y 4 en ese orden.

Si la terminal muestra mal las tildes al redirigir la salida, usar:

```powershell
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new($false)
java "-Dstdout.encoding=UTF-8" "-Dstderr.encoding=UTF-8" -cp target/classes ec.edu.espe.pa.funcional.Demostracion 4
```

En esta computadora Maven también está en
`C:\Tools\apache-maven-3.10.2\bin\mvn.cmd` y JDK 23 en
`C:\Program Files\Java\jdk-23`. Si hace falta seleccionarlos en la sesión:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-23'
& 'C:\Tools\apache-maven-3.10.2\bin\mvn.cmd' test
& 'C:\Program Files\Java\jdk-23\bin\java.exe' -cp target/classes ec.edu.espe.pa.funcional.Demostracion 4
```

## Archivos que deben mostrarse en las capturas

| Fase | Archivo o salida | Evidencia |
| --- | --- | --- |
| 2 | `Demostracion.java`, método `demostrarInterfaces` | Las cuatro interfaces, sintaxis y captura de un umbral efectivamente final. |
| 3 | `contrato/ReglaIncidencia.java` | Un método abstracto y el método `default y`. |
| 3 | `reglas/PrioridadAlta.java` | Implementación mediante clase con nombre. |
| 3 | `Demostracion.java`, método `demostrarContrato` | La misma regla implementada por clase, clase anónima y lambda. |
| 3 | `Demostracion 3` | Las tres implementaciones coinciden en prioridades 3, 4 y 5. |
| 3 | `evidencias/fase3-error-contrato.txt` | Diagnóstico real al compilar una copia temporal con dos métodos abstractos; la fuente original no fue modificada. |
| 4 | `reglas/ReglasIncidencia.java` | Justificación por entrada y salida sobre cada función. |
| 4 | `Demostracion 4` | Reglas, categorías, notificación y dos llamadas al mismo Supplier. |
| 4 | `mvn test` | 19 pruebas aprobadas, incluyendo la prueba inicial del arquetipo. |
| Historial | `git log --oneline` | Registro del avance previo y commits de fases 3 y 4. |

Los registros de ejecución están en `evidencias/`. Las capturas del estudiante
y la actualización del informe quedan pendientes. Las fases 5 a 8 no se han
completado; `App.java` y `ProcesadorImperativo.java` conservan el estado anterior.
La prueba de mutación del umbral corresponde a una fase posterior.

## Verificaciones realizadas

- Tres implementaciones del contrato: mismo resultado y cortocircuito de `y`.
- Matriz del dominio: abiertas, cerradas, en proceso y prioridades de 1 a 5.
- Categorías: BAJA, MEDIA, ALTA y CRITICA.
- Identificador y título del resumen; contenido de la notificación.
- Supplier con identificadores distintos y prioridades válidas, con contador
  independiente por instancia de generador.
- Rechazo de prioridades 0 y 6 por el record existente.
- El Supplier conserva estado: sus resultados cambian con cada llamada y un
  generador nuevo reinicia la secuencia. No se afirma unicidad entre generadores.

El repositorio Git se creó localmente para conservar las fases. No se ha
configurado ni publicado un repositorio remoto para esta práctica.
