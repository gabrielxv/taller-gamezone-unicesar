# Auditoría GRASP — líder técnico y programador 2

## Alcance y método
La auditoría se basa en las clases presentes en main al iniciar esta rama. Se documentan responsabilidades observables; las recomendaciones no se confunden con pruebas de ejecución. GRASP orienta la asignación de responsabilidades y complementa pruebas y principios SOLID.

## Trazabilidad funcional de R1–R8
| Req. | Responsabilidad | Clases / evidencia | Revisión |
|---|---|---|---|
| R1 Accesorios | Modelar control, cable y memoria; compatibilidad y stock | Accessory, Controller, Cable, Memory, AccessoryService, AccessoryRepository | Preservar el contrato del servicio para el programador 1 |
| R2 Promociones | Aplicar estrategias vigentes y elegir mayor descuento | Promotion, PercentageDiscount, CategoryDiscount, BulkPurchaseDiscount, PromotionService | Añadir pruebas de vigencia, categoría y empate |
| R3 Devoluciones | Validar venta, plazo, pertenencia y cantidad; reembolsar y reponer stock | Return, ReturnService, ReturnRepository, ReturnValidator | Probar fallos entre escrituras; no prometer atomicidad global |
| R4 Garantías | Asignar coberturas, persistir y cancelar | Warranty, BasicWarranty, ExtendedWarranty, WarrantyService | Confirmar reembolso de costo extendido |
| R5 Integración | Coordinar venta con personas, catálogo, descuentos, stock y garantías | SaleService, ProductGateway, PeopleGateway, Main | Requiere prueba manual integral |
| R6 Validaciones | Rechazar datos inválidos y reglas de negocio violadas | validation/*, exception/*, validaciones de modelo | Añadir pruebas negativas por endpoint web |
| R7 GRASP | Asignar controlador, experto en información, fabricación pura y bajo acoplamiento | Servicios, repositorios, gateways, UI | Sincronizar diagramas con código |
| R8 SOLID | Separar responsabilidades y extender por polimorfismo | Servicios, repositorios, estrategias, interfaces gateway | Revisar cambios nuevos para no llevar reglas a UI |

## Principios GRASP observados

### Controller — controlador
- SaleService.registerSale coordina el caso de uso de venta; la UI recibe datos y delega.
- ReturnService.registerReturn coordina validación, reembolso, cancelación de garantías, reposición de stock y persistencia.
- Riesgo: ReturnService coordina varios efectos persistentes; una excepción a mitad del proceso puede dejar estados parciales. Una evolución robusta requiere transacción o compensación explícita.

### Information Expert — experto en información
- ProductService conoce el catálogo y stock de productos.
- AccessoryService conoce accesorios, compatibilidad y stock.
- Sale y SaleLine conservan los datos históricos para recibos y reembolsos.
- Promotion delega el cálculo específico a cada estrategia concreta.

### Pure Fabrication — fabricación pura
- Los repositorios encapsulan lectura y escritura de ficheros.
- ConsoleUI y la capa HTTP presentan datos y delegan en los mismos servicios.

### Creator — creador
- Main construye el grafo de dependencias e inyecta repositorios y servicios.
- Los servicios crean entidades cuando conocen los datos y la operación de negocio correspondiente.

### Low Coupling / High Cohesion
- ProductGateway y PeopleGateway acotan dependencias de ventas.
- Los módulos mantienen servicios y repositorios separados.
- Los flujos de venta/devolución tocan varios repositorios: considerar coordinador transaccional o compensación si la persistencia evoluciona.

## Matriz de hallazgos
| ID | Hallazgo | Estado | Acción |
|---|---|---|---|
| G-01 | La UI delega en servicios y no escribe ficheros directamente | Presente | Preservar en nuevas pantallas |
| G-02 | Gateways limitan dependencias de ventas | Presente | Añadir tests de integración |
| G-03 | Reembolso y reposición afectan más de un fichero | Riesgo conocido | Diseñar compensación/transacción antes de uso productivo |
| G-04 | Consola y web son dos capas de presentación | Añadido en esta rama | Confirmar que compilan y comparten servicios |
| G-05 | Diagramas deben corresponder a clases reales | Revisión humana pendiente | Revisar tras integrar al programador 1 |

## Conclusión
El diseño separa interfaz, reglas de aplicación, persistencia y modelo. La documentación no declara cumplimiento perfecto: el equipo debe ejecutar pruebas, comparar con los ocho enunciados originales y registrar evidencia real.
