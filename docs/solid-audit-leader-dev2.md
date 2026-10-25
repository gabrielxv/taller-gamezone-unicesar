# Auditoría SOLID — líder técnico y programador 2

## Alcance
Esta auditoría describe principios observables en la implementación integrada. “Presente” indica alineación estructural, no ausencia total de mejoras ni sustitución de pruebas.

## S — Single Responsibility Principle
**Evidencia:** ConsoleUI gestiona interacción de consola; WebServer traduce HTTP a operaciones de servicio; SaleService, ReturnService, PromotionService y WarrantyService coordinan sus casos de uso; los repositorios aíslan persistencia.
**Riesgo:** ReturnService.registerReturn coordina varios efectos persistentes con compensación limitada. WebServer también contiene mapeo de parámetros y serialización HTTP.
**Recomendación:** extraer DTOs/mappers y un coordinador de transacción/compensación si crece el proyecto, sin trasladar reglas a la interfaz.

## O — Open/Closed Principle
**Evidencia:** Promotion define contrato de estrategia y sus subclases calculan descuentos; Warranty abstrae cobertura y sus subclases aportan duración/costo.
**Riesgo:** UI/API seleccionan explícitamente tipos concretos. Añadir tipos nuevos exige ampliar puntos de entrada.
**Recomendación:** si se agregan muchas estrategias, usar registro/fábrica y pruebas por tipo.

## L — Liskov Substitution Principle
**Evidencia:** implementaciones de Promotion deben satisfacer el contrato de cálculo; BasicWarranty y ExtendedWarranty se usan mediante Warranty.
**Verificación requerida:** ejecutar pruebas con cada subtipo y confirmar precondiciones, resultados y serialización. El código fuente por sí solo no demuestra todos los casos límite.

## I — Interface Segregation Principle
**Evidencia:** ProductGateway expone operaciones requeridas por servicios que coordinan ventas; PeopleGateway consulta existencia de cliente/vendedor.
**Recomendación:** no convertir gateways en interfaces demasiado amplias; agregar operaciones solo cuando un consumidor las necesite.

## D — Dependency Inversion Principle
**Evidencia:** SaleService recibe ProductGateway, PeopleGateway y servicios coordinados por constructor; los servicios reciben repositorios por constructor.
**Límite:** algunos servicios conocen implementaciones concretas y los repositorios escriben directamente en ficheros. Es una decisión sencilla para un proyecto local; una evolución a varios almacenes puede introducir interfaces de persistencia.

## Matriz de seguimiento
| ID | Principio | Área | Estado | Evidencia / acción |
|---|---|---|---|---|
| S-01 | SRP | Presentación/casos de uso | Presente | ConsoleUI y WebServer delegan en servicios |
| S-02 | SRP | Persistencia | Presente | Repositorios separados por agregado |
| S-03 | OCP | Promociones | Presente | Estrategias derivadas de Promotion |
| S-04 | OCP/LSP | Garantías | Presente; pruebas completas requeridas | BasicWarranty y ExtendedWarranty |
| S-05 | ISP | Integración | Presente | ProductGateway y PeopleGateway |
| S-06 | DIP | Ventas | Parcialmente presente | Inyección por constructor; algunos módulos concretos |
| S-07 | SRP/consistencia | Devoluciones | Riesgo conocido | Escrituras en varios ficheros sin transacción global |
| S-08 | Validación | API web | Añadido en esta rama | Validación de campos y errores HTTP; añadir pruebas HTTP |
| S-09 | Pruebas | R1–R8 | Pendiente | Ejecutar clean verify y escenario integral |

## Criterio de aceptación
- [ ] Ejecutar mvn --batch-mode clean verify en esta rama.
- [ ] Confirmar CI para el commit final.
- [ ] Probar registro válido e inválido de cada tipo de producto/accesorio/promoción.
- [ ] Probar venta mixta con promoción y garantía extendida.
- [ ] Probar devolución parcial, plazo de 30 días y restauración de stock.
- [ ] Confirmar persistencia tras reinicio.
- [ ] Revisar hallazgos después de integrar al programador 1.
