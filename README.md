# challenge-foro-topicos-david-caicedo
API Forum Ultra - Sistema de Foro REST
📋 Descripción
API REST desarrollada con Spring Boot para gestionar un sistema de foro donde los usuarios pueden crear tópicos, hacer preguntas, sugerencias y responder como comunidad.
🚀 Tecnologías Utilizadas

Java 17
Spring Boot 3.2.0
Spring Security con JWT
Spring Data JPA
MySQL 8.0
Flyway Migration
Maven
Lombok

📁 Estructura del Proyecto
src/main/java/com/forum/api/
├── config/
│   ├── SecurityConfigurations.java
│   └── CorsConfig.java
├── controller/
│   ├── AutenticacionController.java
│   └── TopicoController.java
├── dto/
│   ├── DatosRegistroTopico.java
│   ├── DatosActualizarTopico.java
│   ├── DatosListadoTopico.java
│   ├── DatosDetalleTopico.java
│   ├── DatosRespuestaTopico.java
│   ├── DatosAutenticacion.java
│   └── DatosJWTToken.java
├── exception/
│   └── GlobalExceptionHandler.java
├── model/
│   ├── Usuario.java
│   ├── Topico.java
│   ├── Curso.java
│   ├── Respuesta.java
│   └── StatusTopico.java
├── repository/
│   ├── TopicoRepository.java
│   ├── UsuarioRepository.java
│   ├── CursoRepository.java
│   └── RespuestaRepository.java
├── security/
│   └── SecurityFilter.java
├── service/
│   ├── TokenService.java
│   └── AutenticacionService.java
└── ApiForumUltraApplication.java

src/main/resources/
├── application.properties
└── db/migration/
    ├── V1__create_tables.sql
    └── V2__insert_initial_data.sql
🛠️ Configuración Inicial
1. Base de Datos MySQL
Crear la base de datos en MySQL:
sqlCREATE DATABASE api_foro_ultra;
2. Configuración application.properties
El archivo ya está configurado con:

URL de conexión: jdbc:mysql://localhost/api_foro_ultra
Usuario: root
Contraseña: root

Si necesitas cambiar estas credenciales, modifica el archivo application.properties.
3. Instalación de Dependencias
bashmvn clean install
4. Ejecutar la Aplicación
bashmvn spring-boot:run
La aplicación estará disponible en: http://localhost:8080
🔐 Autenticación
Usuarios de Prueba
La migración inicial crea estos usuarios (contraseña para todos: 123456):

Obtener Token JWT
httpPOST http://localhost:8080/login
Content-Type: application/json

{
  "email": "admin@forum.com",
  "password": "123456"
}
Respuesta:
json{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
📡 Endpoints de la API
1. POST /login - Autenticación
Público - No requiere token
Request:
json{
  "email": "admin@forum.com",
  "password": "123456"
}
2. GET /topicos - Listar Tópicos
Público - No requiere token
Parámetros opcionales:

page: Número de página (default: 0)
size: Tamaño de página (default: 10)
curso: Filtrar por nombre del curso
year: Filtrar por año

Ejemplo: GET /topicos?page=0&size=10&curso=Spring Boot&year=2024
3. GET /topicos/{id} - Detalle del Tópico
Público - No requiere token
Ejemplo: GET /topicos/1
4. POST /topicos - Registrar Tópico
Requiere Token - Bearer Token en header Authorization
Headers:
Authorization: Bearer {token}
Content-Type: application/json
Request:
json{
  "titulo": "Problema con Spring Security",
  "mensaje": "No puedo configurar correctamente Spring Security",
  "autorId": 2,
  "cursoId": 1
}
5. PUT /topicos/{id} - Actualizar Tópico
Requiere Token - Bearer Token en header Authorization
Headers:
Authorization: Bearer {token}
Content-Type: application/json
Request:
json{
  "id": 1,
  "titulo": "Título actualizado",
  "mensaje": "Mensaje actualizado",
  "status": "SOLUCIONADO"
}
Status permitidos: ABIERTO, CERRADO, SOLUCIONADO, SIN_RESPUESTA
6. DELETE /topicos/{id} - Eliminar Tópico
Requiere Token - Bearer Token en header Authorization
Headers:
Authorization: Bearer {token}
Ejemplo: DELETE /topicos/1
🧪 Pruebas con Insomnia
Importar Colección

Abre Insomnia
Ve a Application → Preferences → Data → Import Data
Importa el archivo insomnia-collection.json incluido en el proyecto

Configurar Variable de Entorno

En Insomnia, crea una variable de entorno llamada token
Después de hacer login, copia el token recibido
Pega el token en la variable de entorno

Flujo de Prueba Recomendado

Login - Obtener token JWT
Listar Tópicos - Ver tópicos existentes
Crear Tópico - Usar el token para crear un nuevo tópico
Ver Detalle - Consultar el tópico creado
Actualizar - Modificar el tópico
Eliminar - Borrar el tópico

🚨 Manejo de Errores
La API maneja los siguientes tipos de errores:

400 Bad Request: Datos inválidos o tópico duplicado
401 Unauthorized: Token inválido o ausente
404 Not Found: Recurso no encontrado
500 Internal Server Error: Error del servidor

Ejemplo de respuesta de error:
json{
  "timestamp": "2024-01-01T12:00:00",
  "status": 400,
  "error": "Validation Failed",
  "messages": ["El título es obligatorio"]
}
📊 Reglas de Negocio

Tópicos Únicos: No se permiten tópicos con el mismo título y mensaje
Campos Obligatorios: Todos los campos son requeridos al crear un tópico
Autenticación: Solo usuarios autenticados pueden crear, actualizar y eliminar tópicos
Validación de IDs: Se valida que existan el autor y curso antes de crear un tópico

🔄 Estados del Tópico

ABIERTO: Tópico nuevo o sin resolver
CERRADO: Tópico cerrado para nuevas respuestas
SOLUCIONADO: Tópico con solución encontrada
SIN_RESPUESTA: Tópico sin respuestas

📝 Notas Adicionales
Seguridad

Los tokens JWT tienen una expiración de 2 horas (configurable en application.properties)
Las contraseñas se almacenan encriptadas con BCrypt
Se implementa validación de datos en todos los endpoints

Paginación

Por defecto se muestran 10 resultados por página
Los resultados se ordenan por fecha de creación ascendente
Se puede personalizar con parámetros page, size y sort

Filtros Disponibles

Filtrar por nombre del curso
Filtrar por año de creación
Combinar ambos filtros

🐛 Solución de Problemas Comunes
Error de Conexión a MySQL
Verificar que:

MySQL esté ejecutándose
La base de datos api_foro_ultra exista
Las credenciales en application.properties sean correctas

Token Inválido

Verificar que el token no haya expirado (2 horas de duración)
Asegurarse de incluir "Bearer " antes del token en el header
Verificar que el usuario esté activo

Tópico Duplicado

No se pueden crear tópicos con el mismo título Y mensaje
Se puede tener el mismo título con diferente mensaje o viceversa

🚀 Despliegue en Producción
Generar JAR
bashmvn clean package
Ejecutar JAR
bashjava -jar target/api-forum-ultra-0.0.1-SNAPSHOT.jar
Variables de Entorno para Producción
bashexport SPRING_DATASOURCE_URL=jdbc:mysql://servidor:3306/api_foro_ultra
export SPRING_DATASOURCE_USERNAME=usuario_produccion
export SPRING_DATASOURCE_PASSWORD=password_seguro
export JWT_SECRET=clave_secreta_muy_segura
export JWT_EXPIRATION=7200000
📚 Ejemplos de Uso Completos
Crear un Tópico Completo
bash# 1. Obtener token
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@forum.com","password":"123456"}'

# 2. Crear tópico con el token obtenido
curl -X POST http://localhost:8080/topicos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGc..." \
  -d '{
    "titulo":"Cómo implementar WebSockets",
    "mensaje":"Necesito ayuda para implementar WebSockets en Spring Boot",
    "autorId":1,
    "cursoId":1
  }'
Buscar Tópicos del 2024 sobre Spring Boot
bashcurl -X GET "http://localhost:8080/topicos?curso=Spring Boot&year=2024&size=5"
🔍 Monitoreo y Logs
Configuración de Logs
En application.properties:
propertieslogging.level.com.forum.api=DEBUG
logging.level.org.springframework.security=DEBUG
logging.file.name=logs/forum-api.log
Health Check Endpoint
Puedes agregar Spring Actuator para monitoreo:
xml<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
👥 Contribuir

Fork el proyecto
Crea una rama para tu feature (git checkout -b feature/NuevaCaracteristica)
Commit tus cambios (git commit -m 'Agregar nueva característica')
Push a la rama (git push origin feature/NuevaCaracteristica)
Abre un Pull Request

📄 Licencia
Este proyecto es de uso educativo y está disponible bajo licencia MIT.
🆘 Soporte
Para reportar bugs o solicitar nuevas características, por favor abre un issue en el repositorio.
🎯 Próximas Mejoras

 Implementar sistema de respuestas a tópicos
 Agregar roles de usuario (Admin, Moderador, Usuario)
 Implementar sistema de notificaciones
 Agregar búsqueda por texto completo
 Implementar sistema de likes/votos
 Agregar upload de imágenes
 Implementar WebSockets para actualizaciones en tiempo real
 Agregar sistema de etiquetas/tags
 Implementar estadísticas y analytics
 Agregar exportación de datos (CSV/PDF)

📊 Estructura de Base de Datos
Tabla: usuarios
CampoTipoDescripciónidBIGINTPK, Auto incrementnombreVARCHAR(100)Nombre del usuarioemailVARCHAR(100)Email únicopasswordVARCHAR(300)Contraseña encriptadaactivoBOOLEANEstado del usuario
Tabla: topicos
CampoTipoDescripciónidBIGINTPK, Auto incrementtituloVARCHAR(200)Título del tópicomensajeTEXTContenido del tópicofecha_creacionDATETIMEFecha de creaciónstatusVARCHAR(50)Estado del tópicoautor_idBIGINTFK a usuarioscurso_idBIGINTFK a cursos
Tabla: cursos
CampoTipoDescripciónidBIGINTPK, Auto incrementnombreVARCHAR(100)Nombre del cursocategoriaVARCHAR(50)Categoría del curso
Tabla: respuestas
CampoTipoDescripciónidBIGINTPK, Auto incrementmensajeTEXTContenido de la respuestatopico_idBIGINTFK a topicosfecha_creacionDATETIMEFecha de creaciónautor_idBIGINTFK a usuariossolucionBOOLEANMarca como solución
