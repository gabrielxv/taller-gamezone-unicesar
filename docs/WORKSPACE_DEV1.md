# Espacio independiente — Desarrollador 1

## Propósito
Zona de borrador práctico para diseñar y construir la primera versión de los modelos y reglas de dominio. Este espacio es una rama independiente y no es la versión de entrega.

## Rama
`trabajo/desarrollador-1-borrador`

## Alcance asignado
- Modelos de accesorios: `Accessory`, `Controller`, `Cable`, `Memory`.
- Modelos de promociones: `Promotion`, `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount`.
- Modelos de devoluciones y comportamiento de devolución en ventas.
- Modelos de garantías: `Warranty`, `BasicWarranty`, `ExtendedWarranty`.
- Contratos de datos, invariantes del dominio y pruebas unitarias de los modelos.

## Reglas de trabajo
1. Trabajar y hacer commits únicamente en esta rama para los borradores asignados.
2. No modificar directamente `develop`, `main` ni `integracion/entrega-final`.
3. No incorporar lógica de consola ni acceso a CSV/repositorios dentro de los modelos.
4. Documentar métodos públicos, supuestos y contratos que necesite el Desarrollador 2.
5. Cuando el borrador esté revisable, abrir un Pull Request hacia `integracion/entrega-final`; no mezclar directamente.
6. El código solo se considera integrado después de revisión y pruebas en la rama común.

## Criterios para entregar el borrador
- Compila con Java 17.
- Mantiene encapsulación y responsabilidades de la capa `model`.
- Incluye pruebas o escenarios reproducibles.
- Explica decisiones pendientes y dependencias con otros módulos.

## Importante
Esta rama aísla el trabajo en Git, pero no crea una cuenta, editor ni entorno remoto independiente. Cada desarrollador debe abrir esta rama en su propio clon o Codespace/editor.
