# Espacio independiente — Desarrollador 2

## Propósito
Zona de implementación limpia: persistencia CSV, repositorios, servicios, validaciones, excepciones y refactorización. Debe consumir contratos acordados con el Desarrollador 1 y evitar duplicar reglas de negocio.

## Rama
`trabajo/desarrollador-2-codigo-limpio`

## Alcance asignado
- Repositorios y servicios para accesorios, promociones, devoluciones y garantías.
- Persistencia CSV y datos iniciales que exijan los requisitos.
- Validadores y excepciones tipadas: `GameZoneException`, `ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, `PersistenceException`.
- Refactorización de servicios existentes para usar validadores y excepciones consistentes.
- Pruebas de persistencia, reglas de negocio y fallos de datos.

## Reglas de trabajo
1. Trabajar y hacer commits únicamente en esta rama para las tareas asignadas.
2. No modificar directamente `develop`, `main` ni `integracion/entrega-final`.
3. Acordar firmas, identificadores y contratos con el Desarrollador 1 antes de depender de modelos inestables.
4. Mantener la dirección de dependencias por capas: `ui -> service -> persistence -> model`.
5. Evitar dependencias circulares entre servicios y repositorios.
6. Cuando esté revisable, abrir un Pull Request hacia `integracion/entrega-final`.
7. No considerar integrado el módulo hasta que pase revisión, compilación y pruebas.

## Criterios para el código limpio
- Métodos pequeños, nombres claros y responsabilidades separadas.
- Errores de persistencia envueltos con contexto sin ocultar la causa.
- Validaciones sin duplicación innecesaria.
- Pruebas para entradas inválidas, recursos inexistentes, reglas incumplidas y errores de CSV.

## Importante
Esta rama aísla el trabajo en Git, pero no crea una cuenta, editor ni entorno remoto independiente. Cada desarrollador debe abrir esta rama en su propio clon o Codespace/editor.
