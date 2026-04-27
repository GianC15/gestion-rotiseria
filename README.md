# Sistema de Gestión de Rotisería

Sistema web desarrollado para gestionar la operativa diaria de una rotisería por encargo. Reemplaza el pizarrón físico y las planillas de Excel por una aplicación centralizada, accesible desde computadora y celular.

---

## El problema que resuelve

El negocio opera con clientes fijos que realizan pedidos día a día vía WhatsApp o en persona. La gestión se llevaba manualmente: un pizarrón con nombres y pedidos, y una planilla Excel para el seguimiento de cobros.

Este sistema digitaliza toda esa operativa:
- Registro de pedidos por cliente y por día
- Resumen de producción diaria (cuántas unidades de cada plato preparar)
- Carga anticipada del menú semanal completo
- Seguimiento de cobros semanales con soporte para distintas modalidades de pago
- Gestión de fiados y deudores

---

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| Backend | Java 21 + Spring Boot 4 |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | MySQL |
| Vistas | Thymeleaf |
| Seguridad | Spring Security |
| Build | Maven |
| Deploy | Railway |

---

## Arquitectura

El proyecto sigue una arquitectura en tres capas:

- **Controller** — recibe las peticiones HTTP y delega al servicio
- **Service** — contiene toda la lógica de negocio
- **Repository** — abstracción sobre la base de datos mediante JPA
- **Model** — entidades mapeadas con anotaciones JPA
- **DTO** — objetos de transferencia que separan la capa de datos de la presentación

---

## Modelo de datos

El sistema cuenta con 8 entidades principales:

- `Cliente` — clientes activos con baja lógica
- `Plato` — catálogo reutilizable de platos y guarniciones
- `Semana` — unidad de trabajo con precio de vianda configurable
- `MenuDia` — platos disponibles para un día específico
- `MenuDiaPlato` — relación muchos a muchos entre menú y platos
- `Pedido` — cabecera del pedido de un cliente para un día
- `PedidoItem` — ítem individual con principal, guarnición y cantidad
- `Cobro` — registro de cobros por cliente y semana

---

## Funcionalidades

- **Clientes** — alta, edición y baja lógica
- **Platos** — catálogo de principales y guarniciones reutilizable entre semanas
- **Semanas** — configuración de precio de vianda por semana
- **Menú semanal** — carga anticipada del menú para todos los días de la semana desde un solo lugar
- **Pedidos del día** — registro de pedidos con soporte para múltiples viandas por cliente
- **Pedidos semanales por cliente** — carga de todos los pedidos de la semana de un cliente de una sola vez
- **Resumen de producción** — totales automáticos por plato para preparar el día
- **Caja semanal** — seguimiento de cobros con tres modalidades: por día, al cierre o fiado
- **Totalizadores** — montos cobrados y adeudados actualizados automáticamente
- **Deudores** — vista consolidada de clientes con saldo pendiente
- **Recálculo automático** — el monto total se actualiza automáticamente al registrar pedidos
- **Autenticación** — acceso protegido con Spring Security

---

## Decisiones técnicas destacadas

**Baja lógica en clientes** — los clientes nunca se eliminan físicamente para preservar el historial de pedidos y cobros.

**Precio de vianda por semana** — el precio se guarda en la entidad `Semana` y no en cada pedido, permitiendo cambios de precio sin afectar datos históricos.

**Normalización de platos** — los platos son entidades reutilizables entre semanas, evitando duplicación de datos y facilitando la carga del menú.

**Separación Pedido / PedidoItem** — permite que un cliente tenga múltiples viandas distintas en un mismo día con cantidades independientes.

**orphanRemoval en PedidoItem** — garantiza que al quitar un ítem de la lista en memoria, JPA lo elimine automáticamente de la base de datos.

**Patrón DTO** — las entidades JPA nunca se exponen directamente a las vistas. Un `DtoMapper` centralizado convierte entidades a objetos de transferencia, separando la capa de datos de la presentación.

**Gestión de ambientes** — perfil `local` para desarrollo y variables de entorno en Railway para producción. Las credenciales nunca se exponen en el repositorio.

**Headers de seguridad** — Content Security Policy, frameOptions y HSTS configurados para proteger contra XSS y clickjacking.

**Validación de inputs** — los formularios críticos validan los datos del lado del servidor con Bean Validation antes de persistirlos.

---

## Seguridad

- Autenticación con Spring Security y contraseñas hasheadas con BCrypt
- Protección CSRF activa en todos los formularios
- Headers HTTP de seguridad: CSP, X-Frame-Options y HSTS
- Las credenciales se gestionan exclusivamente mediante variables de entorno
- Patrón DTO para evitar exposición de la estructura interna de la base de datos
- Errores de producción sin stack trace visible al usuario

---

## Cómo levantar el proyecto localmente

### Requisitos

- Java 21
- MySQL 8+
- Maven

### Pasos

**1. Clonar el repositorio**
```bash
git clone https://github.com/GianC15/gestion-rotiseria.git
cd gestion-rotiseria
```

**2. Crear la base de datos**

Ejecutar el script SQL en MySQL Workbench:

**3. Configurar credenciales locales**

Creá el archivo `src/main/resources/application-local.properties` — este archivo no se sube a GitHub:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tu_nombre_de_base_de_datos?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA_MYSQL

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.thymeleaf.cache=false

app.security.username=TU_USUARIO
app.security.password=TU_CONTRASEÑA_APP
```

**4. Ejecutar con perfil local**

Desde IntelliJ configurá la variable de entorno:

O desde terminal:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Acceder en `http://localhost:8080`

---

## Despliegue en producción

El sistema está desplegado en Railway con las siguientes variables de entorno configuradas en el panel:

MYSQLHOST
MYSQLPORT
MYSQL_DATABASE
MYSQLUSER
MYSQLPASSWORD
APP_USERNAME
APP_PASSWORD
SPRING_PROFILES_ACTIVE=prod

Railway redespliega automáticamente con cada push a la rama `main`.

---

## Flujo de trabajo Git

```bash
# Trabajar en develop
git checkout develop

# Cuando está listo para producción
git checkout main
git merge develop
git push
git checkout develop
```

---

## Autor

**Gian Castro Colicigno**
Estudiante de Licenciatura en Sistemas de Información — Universidad Nacional de Luján
[LinkedIn](https://www.linkedin.com/in/gian-colicigno-921ba21b0/) · [GitHub](https://github.com/GianC15)