# Interfaz web de GameZone Unicesar

## Objetivo
La interfaz web usa los mismos servicios de dominio que la aplicación de consola. No duplica reglas de venta, promociones, devoluciones ni garantías: actúa como una capa HTTP de presentación.

## Inicio
Requisitos: Java 17 y Maven 3.9+.

```bash
mvn --batch-mode clean verify
mvn exec:java -Dexec.mainClass="com.gamezone.Main" -Dexec.args="--web"
```

Abrir http://localhost:8080. Para otro puerto, definir la variable de entorno GAMEZONE_PORT antes de ejecutar. La aplicación sigue usando ficheros bajo data/; usar una copia desechable de esos datos para demostraciones. No exponer el servidor a una red pública sin autenticación, autorización, HTTPS y una política de respaldo.

## API disponible

| Método | Ruta | Función |
|---|---|---|
| GET | /api/health | Estado del servidor |
| GET | /api/dashboard | Indicadores de negocio, inventario, ventas y devoluciones del mes |
| GET / POST | /api/products | Catálogo / registrar videojuego o consola |
| GET / POST | /api/accessories | Catálogo / registrar control, cable o memoria |
| GET / POST | /api/clients | Clientes / registrar cliente |
| GET | /api/sellers | Vendedores |
| GET / POST | /api/promotions | Promociones / registrar descuento |
| GET / POST | /api/sales | Historial / registrar venta integrada |
| GET / POST | /api/returns | Historial / registrar devolución |
| GET | /api/warranties | Historial de garantías |

Los endpoints POST reciben JSON. Los errores de validación se devuelven con HTTP 400 y un objeto error; los fallos no esperados se devuelven con HTTP 500. Esta primera versión no incluye edición/eliminación ni autenticación.

## Parámetros clave
- Producto: type (VIDEOGAME o CONSOLE), id, name, price, stock; además platform/genre o manufacturer/model.
- Accesorio: type (CONTROLLER, CABLE, MEMORY), id, name, price, stock, compatibleConsoleIds; los campos específicos dependen del tipo.
- Cliente: id, name, email, phone y membershipLevel.
- Promoción: type, id, name, startDate, endDate, percentage; además category o minimumQuantity.
- Venta: customerId, sellerId, itemIds (lista de IDs; repetir un ID representa varias unidades) y opcionalmente extendedWarrantyIds.
- Devolución: saleId, itemIds y reason.

## Límites y seguridad
- Esta versión se diseñó para desarrollo local y demostración académica, no como publicación de producción.
- No se añade autenticación ni autorización; no debe exponerse a Internet.
- El servidor usa la persistencia de archivos existente. No hay una transacción ACID única entre ficheros.
- La página depende del backend Java en ejecución; abrir el HTML como archivo independiente no habilita operaciones.
- Ejecutar mvn clean verify y comprobar el escenario integral antes de afirmar que la versión final está verificada.
