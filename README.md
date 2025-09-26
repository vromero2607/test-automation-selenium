# Proyecto Selenium con Java

Este proyecto es un ejemplo de automatización con Selenium + JUnit5, integrando reportes con Allure.

## Tecnologías usadas
- Java 17
- Maven
- Selenium 4.25.0
- WebDriverManager
- JUnit 5
- Allure Reports

## Ejercicio automatizado
El flujo cubre:
1. Entrar a [MercadoLibre](https://www.mercadolibre.com.mx/)
2. Seleccionar México como país
3. Buscar el término **"playstation 5"**
4. Filtrar por condición **"Nuevos"**
5. Filtrar por ubicación **"CDMX"**
6. Ordenar resultados por **"Mayor a menor precio"**
7. Obtener nombre y precio de los primeros 5 productos
8. Imprimir estos productos en la consola

## Cómo ejecutar
```bash
mvn clean test
mvn allure:serve
# Proyecto Selenium con Java
