# WChallenge

## Introducción

Adicional a la logica presentada en la prueba tecnica, se añadieron algunas pruebas unitarias, y una generacion de reporte de cobertura con jacoco y tambien implementacion de swagger para ver los endpoint con facilidad y se puede acceder esta de forma local en la ruta local http://localhost:8090/swagger-ui.html. Lamentablemente no dispuse del tiempo para lograr hacer la documentacion completa, pero con swagger se puede visualizar con mayor facilidad todas las rutas disponibles y sus respectivos metodos


## Instalación Local

La aplicacion guarda informacion en una base de datos, especificamente MongoDB, es necesario tener el servio de mongo corriendo y tener una base de datos llamada wchallenge, no hay necesidad de crear colecciones especificas o usuarios

Se uso gradle para las depencias y estructura del proyecto, solo basta con ejecutar un build para construir la aplicacion, usando el siguiente comando

- 'gradlew build'

Y luego

- 'gradlew bootRun'

para iniciar el servidor, tener en cuenta que en el application.properties esta configurado para subir en el puerto 8090.
