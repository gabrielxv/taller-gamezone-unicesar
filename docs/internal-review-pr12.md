# Revisión técnica interna — PR #12

**Fecha:** 20 de octubre de 2026  
**Rama revisada:** `feature/leader-dev2-requirements-1-8`  
**Tipo:** revisión técnica previa del autor asistida por análisis estático y pruebas automatizadas.  
**Importante:** no constituye la revisión cruzada independiente de Diego Canales.

## Hallazgos revisados y cambios aplicados

| Área | Hallazgo | Acción |
|---|---|---|
| Garantías | La búsqueda de garantía básica podía depender del primer registro coincidente sin distinguir el subtipo. La garantía extendida podía duplicarse para una misma consola/venta. | Búsqueda por tipo concreto e idempotencia por producto/venta/tipo; prueba `WarrantyServiceTest`. |
| Persistencia de garantías | Un error de escritura podía dejar la lista en memoria modificada aunque el guardado fallara. | Asignación y cancelación restauran el estado en memoria si falla el guardado. |
| Devoluciones parciales | El validador rechazaba IDs repetidos, mientras las ventas consolidan artículos repetidos en una línea con cantidad. No era posible devolver la segunda unidad de un SKU vendido por dos unidades. | IDs repetidos representan unidades; se valida la cantidad acumulada contra la cantidad vendida y se prueba la devolución de dos unidades y el rechazo de una tercera. |
| Inventario de devoluciones | La restauración por artículo individual no reflejaba cantidades agrupadas. | La restauración se realiza por la cantidad solicitada para cada ID. |
| Documentación | No había una única guía que enlazara trazabilidad, diagramas y demostración. | README, matriz R1–R8, análisis de integración, diagramas de clases/capas/secuencia y guía de presentación actualizados. |
| Equipo | Había marcadores de identidad que no coincidían con los nombres confirmados. | Se documentaron Ángel Vargas y Diego Canales; los códigos estudiantiles permanecen pendientes porque no se suministraron. |

## Resultado de CI

La ejecución de push para el último commit documental `226061eec9a6d1213dbeb926f400b154b72a71e7` terminó correctamente:
https://github.com/gabrielxv/taller-gamezone-unicesar/actions/runs/39417623132

La ejecución de pull request para ese mismo commit debe confirmarse antes de fusionar:
https://github.com/gabrielxv/taller-gamezone-unicesar/actions/runs/39417627289

## Riesgos que siguen declarados

1. **Atomicidad de múltiples archivos:** una operación completa de venta/devolución involucra varios repositorios de archivos; no es una transacción ACID única. Si una escritura posterior falla, puede ser necesaria reconciliación.
2. **Prueba de aceptación local:** CI verifica compilación y pruebas unitarias, pero no reemplaza ejecutar la demostración completa en un equipo con una copia limpia de los datos.
3. **Trazabilidad literal:** cotejar la matriz con todos los PDF originales de los requerimientos. No se tuvo el texto íntegro de los documentos separados R2–R4 y R7–R8 en esta pasada.
4. **Datos de matrícula:** los códigos oficiales y cualquier tercer integrante exigido por el docente deben ser confirmados por el equipo.

## Criterio de aprobación

El autor puede documentar esta revisión interna, pero no debe aprobar su propio PR como si fuera el revisor. Diego Canales debe revisar el diff real, dejar observaciones si las hay y emitir su propia aprobación desde su cuenta.
