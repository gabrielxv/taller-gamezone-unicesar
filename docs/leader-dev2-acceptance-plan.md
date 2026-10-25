# Plan de aceptación — líder técnico y programador 2

## Meta
Dejar al programador 1 una integración estable y un contrato explícito para que pueda terminar y validar el módulo de accesorios sin duplicar reglas ni cambiar interfaces accidentalmente.

## Contratos que debe preservar el programador 1
- AccessoryService.listAllAccessories(), findById(), listAccessoriesByType() y findAccessoriesCompatibleWith().
- AccessoryService.updateStock(id, delta) rechaza stock final negativo.
- AccessoryService.restoreStock(id, quantity) exige cantidad positiva.
- SaleService.registerSale(customerId, sellerId, itemIds, extendedWarrantySelections) valida personas, artículos y stock, calcula descuentos, conserva líneas históricas, asigna garantías y actualiza inventario.
- ReturnService.registerReturn(saleId, itemIds, reason) valida pertenencia/cantidad/plazo, calcula reembolso y restaura inventario.
- ProductGateway y PeopleGateway son límites de integración; no acceder directamente a repositorios desde la UI.

## Escenario integral obligatorio
1. Iniciar con una copia desechable de data/.
2. Registrar consola, videojuego, control, cable y memoria compatibles.
3. Registrar cliente; comprobar vendedor inicial.
4. Crear promoción por categoría y por volumen con fechas vigentes.
5. Crear venta mixta con consola y accesorio; elegir garantía extendida.
6. Confirmar recibo: subtotal, descuento, costo de garantía y total.
7. Confirmar reducción del stock de cada artículo.
8. Devolver una unidad del accesorio y confirmar reembolso parcial y stock restaurado.
9. Devolver la consola dentro del plazo y confirmar cancelación de garantías y reembolso del costo extendido.
10. Consultar balance mensual: ventas menos devoluciones.
11. Reiniciar y confirmar persistencia.
12. Ejecutar mvn --batch-mode clean verify.

## Criterios por requerimiento
| Req. | Evidencia mínima |
|---|---|
| R1 | Validación, compatibilidad, persistencia, stock y venta de accesorios |
| R2 | Descuentos por porcentaje/categoría/volumen y vigencia |
| R3 | Devolución parcial, cantidades acumuladas, plazo, reembolso y stock |
| R4 | Cobertura básica/extendida, persistencia, cancelación y reembolso |
| R5 | Escenario mixto con recibo y datos persistidos |
| R6 | Casos positivos y negativos con errores claros |
| R7 | Diagrama GRASP coherente con las clases |
| R8 | Matriz SOLID con evidencia, riesgos y acciones |

## Registro de evidencias
No marcar pruebas como ejecutadas sin salida real de Maven/CI o captura verificable. Registrar SHA probado, resultado/fecha de CI, versiones de Java/Maven, IDs generados, recibos, stock antes/después y revisión humana de los ocho enunciados originales.

## Limitación técnica
Los repositorios de archivos separados no comparten una transacción ACID. Una excepción entre escrituras puede dejar estado parcial. Para producción, introducir persistencia transaccional o una estrategia de compensación probada.
