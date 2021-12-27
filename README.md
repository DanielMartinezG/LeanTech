
## Dependencias:

Para ejecutar este sistema se requiere que usted tenga instalado en su maquina:

- Java 117
- Gradle 7
- Docker para correr en el contenerizado

## Run con Docker-compose
para poner a correr la aplicación se necesita lo siguiente:
- hacerle build desde el task de gradle desde cada uno de los proyectos
- pararse en la carpeta Docker, abrir una consola y ejecute el siguiente comando:

docker-compose up --build

## Performance
Se usa actuactor para la recoleccion de info y el health del recurso

health: http://localhost:8080/actuator/health

## LiquiBase
Este sistema cuenta con un manejo de versionamiento de queries llamado liquibase, este se encuentra
en el proyecto Hotel, si usted requiere ejecutar un nuevo query en la base de datos se debe hacer lo siguiente:

- ir a la ruta /resources/liquibase/changelog
- crear un file de tipo sql en la carpeta anterior teniendo en cuenta que se le debe poner la fecha como nombre
- dentro de este archivo se debe manejar una estructura sql para el query y como encabezado debe ponerse
  el nombre de la persona que lo realizó, la fecha y el ambiente. En el proyecto existe un archivo liquibase de 
  muestra para tenerlo en cuenta a la hora de requerirlo.
  
El sistema ejecutará una única vez el archivo cuando se inicialice 

##Nota
Es importante tener en cuenta que una vez el archivo haya sido ejecutado no se le deben realizar cambios al mismo.


##Base de Datos
Este sistema fue creado con una base de datos RDS de AWS, para conectarse a la misma se tienen las siguientes 
credenciales:

HostName: lean-tech-hotel.ct9njye4wmlr.us-east-1.rds.amazonaws.com 

UserName: admin

Password: a1234567

##Servicio de Colas
Para este sistema se implementó un servicio de colas SQS de Amazon.
Actualmente se cuenta con 2 queues, una para transportar el mensaje de la reserva que se está haciendo y otra encargada de manejar los mensajes
que rebotan o tienen algún problema (Dead queue letter)

En este momento la cola principal cuando percibe que hubo un error en el mensaje lo intenta enviar 2 veces más, en caso de que las 3 veces haya sido 
rechazado, el mensaje queda encolado en la Dead queue letter esperando a que se le dé gestión.

##Manejo de Excepciones
El sistema cuenta con una clase llamada RestExceptionHandler la cuál utiliza la anotación @ControllerAdvice la cuál nos permite capturar todas las unchecked exceptions
o las que dejamos propagar hasta la última capa. Esto nos permite darle un buen manejo, centralizar el comportamiento que queremos que tengan y evitar que se muestren
excepciones no deseadas al usuario.

En este caso el sistema se encuentra conectado con un gestor de errores llamado Sentry, cada vez que se lanza una excepción en el sistema esta misma se
está logueando en sentry para poder tener trazabilidad de cuántas veces se ha presentado, a qué usuarios y más información del error.

##Credenciales Sentry
user: danielmartinezg95@gmail.com
password: a1234567

Cuando se ingrese a sentry se puede ver un proyecto previamente creado de nombre lean-tech-hotel-producer y allí se pueden ver logueadas todas las excepciones

##Gestor de correos
El sistema se encuentra conectado con Amazon SES que nos sirve como servidor de correos. Es importante anotar que el sistema solo le puede enviar correos a cuentas que hayan
sido verificadas previamente

Se realizó una clase service para el manejo de correos, si usted como desarrollador necesita realizar el envío de un correo debe hacerlo por medio de la clase MailSenderService que se encuentra en el proyecto hotel-consumer, 
ya que este proyecto es quién tiene la responsabilidad de enviar los correos.

##Swagger
Cuando la aplicación ya está desplegada se puede ingresar a la siguiente URL para visualizar la información de los servicios Rest por medio de Swagger:
http://localhost:8081/swagger-ui/

##Mapping
El mapping entre entidades y DTO se está realizando por medio de una librería llamada mapstruct, de esta manera se tiene un código más limpio y se le delega la responsabilidad a esa librería de los mapeos
