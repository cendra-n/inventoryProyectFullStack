# Reglas de trabajo – Inventory Project Full Stack

## 1. Alcance de los cambios
- Trabajá únicamente sobre los archivos necesarios para la funcionalidad solicitada.
- No realices refactorizaciones, mejoras adicionales ni cambios fuera del alcance de la tarea.
- No modifiques código existente que no esté directamente relacionado con el pedido.
- Antes de realizar cambios importantes, analizá el código existente y respetá su comportamiento.

## 2. Arquitectura y convenciones
- Seguí la arquitectura, estructura de paquetes y convenciones existentes en el proyecto.
- Respetá la separación entre Controller, Service y Repository.
- Mantené las convenciones de nombres y el idioma utilizados en el código.
- No agregues patrones, capas o abstracciones innecesarias.

## 3. JavaDoc y documentación
- Agregá JavaDoc claro y conciso a las clases y métodos públicos nuevos o modificados que lo necesiten.
- Documentá parámetros, valores de retorno y excepciones cuando corresponda.
- Documentá únicamente comportamientos comprobables en el código.
- No inventes reglas de negocio ni comportamientos.
- No modifiques lógica, firmas, anotaciones ni dependencias solamente para agregar documentación.

## 4. Reglas de negocio
- No inventes, supongas ni modifiques reglas de negocio.
- Antes de implementar un comportamiento que no esté claramente definido, consultá.
- Si encontrás contradicciones entre la documentación y el código, informalas antes de modificar.
- No cambies el comportamiento actual sin autorización.

## 5. Tests
- Respetá las herramientas y convenciones de testing existentes.
- No elimines ni modifiques tests existentes para conseguir que una ejecución finalice correctamente.
- No utilices bases de datos reales en tests unitarios.
- Si un test falla, analizá e informá la causa sin modificar automáticamente el código de producción.

## 6. Dependencias y configuración
- No agregues, elimines ni actualices dependencias sin justificar previamente su necesidad.
- No modifiques archivos de configuración, versiones de Java, Spring Boot o Maven sin autorización.

## 7. Revisión obligatoria
Al terminar cada tarea:
- Mostrame un resumen de los archivos creados, modificados y eliminados.
- Revisá el diff para confirmar que los cambios sean coherentes con la tarea.
- Informá cualquier modificación fuera del alcance solicitado.
- Ejecutá la compilación o los tests pertinentes, cuando corresponda.
- Informá el resultado real de la ejecución. No afirmes que algo funciona si no fue verificado.

## 8. Git
- No hagas commits ni pushes sin mi autorización explícita.
- No descartes, sobrescribas ni elimines cambios existentes sin consultarme.
- No agregues archivos al staging automáticamente.
- No modifiques archivos ajenos a la tarea.

## 9. Forma de trabajo
- Soy desarrolladora junior y quiero comprender lo que se implementa.
- Explicá brevemente las decisiones técnicas relevantes.
- Utilizá soluciones simples, legibles y acordes al nivel del proyecto.
- No avances con tareas adicionales sin mi aprobación.
- Si tenés dudas sobre un requisito, preguntá antes de implementarlo.