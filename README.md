
# Proyecto Microservicios

Implementación de un microservicio de Gestión de Correctores para la asignatura de Sistemas de Información para Internet.
- **GestionCorrectores**: CRUD de los correctores de exámenes.
<!-- Proyecto futuro: implementar Gestion de Usuarios -->
<!-- - **GestionUsuarios**: CRUD de usuarios de la aplicación. Permitirá a cada usuario a cambiar sus datos de contacto y cambiar su contraseña en caso de que se le olvide. Permitirá hacer login y generará el token de autenticación para el acceso a los servicios Web. -->

## Despliegue con Docker
### Docker compose
Para poner en marcha el microservicio junto a la base de datos haremos uso de **[docker-compose](https://docs.docker.com/compose/gettingstarted/)**. Las imágenes se construirán automáticamente y se pondrán en ejecución **3 contenedores**, correspondientes al backend, frontend, y base de datos. En conjunto a ellos, se crean una red virtual **chupipandi-net** y un volumen **db-data** donde persistirán los datos de la BD.

```bash
# Arranca en background los contenedores
docker compose up -d
# Para y borra los contenedores (incluir el flag indicado para borrar tambien el volumen de datos)
docker compose down [-v, --volumes]
```

### Direcciones web
| Microservicio | URL |
| -------- | --- |
| Correctores backend | http://localhost:8081/ |
| Correctores frontend | http://localhost:4242/ |

> [!Tip]
> Consulta [GestionCorrectores API](GestionCorrectores/.docs/API-Reference.md) para más información sobre las rutas y métodos disponibles.

---

En caso de querer correr los contenedores individualmente, cada carpeta contiene su correspondiente `Dockerfile`, a excepción de el la base de datos, que se encuentra en la raiz del proyecto.

> [!Important]
> Tener en cuenta que el backend depende de la base de datos, por lo que será necesario tener dicho contenedor ya corriendo para poder iniciarse el backend correctamente. Además, para comunicar los contenedores entre sí, es necesario crear una nueva *network* y asignársela mediante el flag `--network <red>` a cada contenedor.

### Contenedor para H2-Database

Para levantar la base de datos h2 se puede usar el siguiente comando:
```bash
# Construye la imagen
docker build . -f Dockerfile.database -t database-h2:latest
# Pone en marcha un contenedor
docker run -itp 8082:8082 --name db-service --network <red> database-h2:latest
```
Para entrar por la web, acceder a través de http://localhost:8082.
| Field | Value |
| :-----: |:-----:|
| URL JDBC | *jdbc:h2:ms_database* |
| User & Passwd  | \<empty> |

### Contenedor para Microservicio Backend/Frontend
Se recomienda utilizar los siguientes tags propuestos al construir las imágenes.
```bash
# Construir imagen
docker build <carpeta con Dockerfile> -t <microservicio>/<back,front>
# Poner en marcha contenedor
docker run -itp <host port>:<cont.port> --name <name> --network <red> <image>
```
> [!Note]
> El nombre sugerido para el servicio de la BD es `db-service`, en caso de escoger otro, será necesario editar la variable de entorno `SPRING_DATASOURCE_URL` al crear el contenedor del microservicio

## Despliegue con maven y/o archivo jar

- Microservicio [Gestión de Correctores](GestionCorrectores/.docs/README.corr.md)

## Documentación

- [Spring Boot Maven Plugin](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/)
- [Docker - Get Started](https://docs.docker.com/get-started/)
- [Docker-compose file](https://docs.docker.com/compose/compose-file/03-compose-file/)
- [Dockerfile reference](https://docs.docker.com/engine/reference/builder)