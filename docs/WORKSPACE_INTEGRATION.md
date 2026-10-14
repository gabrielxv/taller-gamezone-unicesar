# Espacio único de integración y entrega

## Rama común
`integracion/entrega-final`

Esta es la zona compartida donde se revisan e integran los aportes de ambos desarrolladores y se prepara la versión candidata a entrega. No debe usarse para hacer borradores individuales directamente.

## Mapa de espacios
| Zona | Rama | Uso | Destino |
|---|---|---|---|
| Desarrollador 1 | `trabajo/desarrollador-1-borrador` | Modelos, reglas iniciales y pruebas prácticas | Pull Request a `integracion/entrega-final` |
| Desarrollador 2 | `trabajo/desarrollador-2-codigo-limpio` | Repositorios, servicios, CSV, validaciones, excepciones y refactorización | Pull Request a `integracion/entrega-final` |
| Líder técnico / entrega | `integracion/entrega-final` | Revisar contratos, integrar, probar y organizar la entrega | Pull Request final a `main`, cuando esté aprobada |

## Flujo obligatorio
1. Cada desarrollador trabaja en su rama independiente y confirma cambios pequeños y comprensibles.
2. El desarrollador abre un Pull Request desde su rama hacia `integracion/entrega-final`.
3. El líder revisa el diff, requisitos, compatibilidad entre capas y evidencia de pruebas.
4. Si hay observaciones, el autor corrige en su rama y actualiza el Pull Request.
5. Solo se integra cuando las observaciones bloqueantes estén resueltas.
6. Después de integrar ambos módulos, se ejecuta la compilación y la batería de regresión.
7. El líder actualiza README, diagramas y lista de requisitos con el estado real.
8. Cuando todo esté verificado, se abre un Pull Request de `integracion/entrega-final` hacia `main` para la entrega final.

## Lista de verificación previa a entrega
- [ ] Los módulos de ambos desarrolladores están integrados y revisados.
- [ ] Los contratos entre modelo, persistencia y servicios son coherentes.
- [ ] Las ventas mixtas, descuentos, garantías y devoluciones funcionan de forma consistente.
- [ ] Los errores se muestran con mensajes claros y no exponen trazas internas.
- [ ] Se ejecutó `mvn clean package` con Java 17 y se registró el resultado real.
- [ ] Se probaron las operaciones originales y las nuevas.
- [ ] README y diagramas coinciden con el código implementado.
- [ ] Los requisitos incompletos se identifican como pendientes, sin marcarlos como terminados.

## Reglas de protección
- No hacer commits directos a `main` ni a `develop`.
- No copiar código de una rama a otra sin revisión.
- No declarar la entrega lista hasta que compilación, pruebas y revisión hayan pasado.
- La rama común es el lugar de integración; no reemplaza las ramas independientes.

## Nota sobre el entorno
Las ramas separan versiones de código dentro del repositorio. Para que cada desarrollador tenga un editor/entorno simultáneo realmente independiente, debe abrir su rama en su propio clon local o Codespace. La rama común seguirá siendo la referencia de integración.
