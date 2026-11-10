# 📦 Inventory Project – Full Stack (Angular + Spring Boot)

## 📌 Descripción general

Este proyecto es una **aplicación de inventarios full stack**, desarrollada con **Angular (frontend)** y **Spring Boot (backend)**.

El punto de partida fue una **consigna de un curso de Udemy**, cuyo alcance inicial era muy básico:

* Solo existía la entidad **Producto**
* El home mostraba únicamente una lista simple de productos
* No había validaciones
* No existían relaciones entre entidades
* No había control real de inventario

A partir de esa base, el proyecto fue **extendido y rediseñado completamente**, incorporando nuevas entidades, validaciones, reglas de negocio y funcionalidades propias de un sistema de inventarios real.

---

## 🆕 Novedades de la nueva versión (v2)

Esta versión no agrega funcionalidades al sistema: **agrega confianza en lo que ya existe**.

* ✅ **316 tests automatizados** en el backend, organizados por entidad.
* 📝 **Tests documentados** con `@DisplayName` en español: cada uno describe, en lenguaje de negocio, qué regla protege.
* 📚 **JavaDoc** en las clases del backend (modelos, DTOs, servicios, controllers, repositorios y excepciones).
* 🤖 **`AGENTS.md`** en `backend/`: reglas de trabajo para asistentes de IA (alcance acotado, sin inventar reglas de negocio, sin tocar código de producción para "forzar" un test).
* 🧪 Los tests **verifican el comportamiento real del código** y funcionan como red de seguridad para las futuras mejoras del proyecto.

---

## 🚀 Funcionalidades implementadas

### 🧱 Entidades del sistema

El sistema cuenta actualmente con **4 entidades principales**:

* **Product** (Producto)
* **Category** (Categoría)
* **Supplier** (Proveedor)
* **InventoryMovement** (Movimiento de inventario)

---

### 📦 Productos

* **CRUD completo**.
* **Listado con:**
* Búsqueda parcial por nombre (ingresando caracteres mínimos).
* Un producto con movimientos de inventario no puede eliminarse, protegiendo la integridad de los datos.
* Si tiene movimientos, solo se puede editarse : nombre,descripción y precio.
* Sino tiene pueden editarse todos sus campos, menos el stock que en campos casos. Solo a través de los movimientos de inventario modifica su stock.
* Descarga de **PDF** con todos los productos cargados en la base de datos.


* **Validaciones:** Desde backend y reflejadas en frontend.

---

### 🗂️ Categorías

* **CRUD completo**.
* **Búsqueda parcial por nombre** (con mínimo de caracteres).
* **Validaciones adicionales en backend**.
* **Integridad referencial:** No permite eliminar una categoría si tiene productos asociados.
* **Manejo de errores:** Mediante excepción personalizada.

---

### 🚚 Proveedores

* **CRUD completo**.
* **Validaciones:** Desde backend y frontend.
* **Integridad referencial:** No permite eliminar un proveedor si tiene productos asociados.
* **Manejo de errores:** Excepción personalizada para reglas de negocio.
* **Búsqueda parcial por nombre**.

---

### 🔄 Movimientos de Inventario

* **Registro de movimientos:**
* **IN** (Entrada)
* **OUT** (Salida)


* **Impacto en Stock:** Cada movimiento impacta directamente en el stock del producto.
* **Regla de Salida:** No se permite salida sin stock suficiente.
* **Inmutabilidad:** * No se editan.
* No se eliminan.


* **Listado completo de movimientos**.
* **Filtros de búsqueda:**
* Por producto.
* Por rango de fechas.
* Por producto + rango de fechas.


* **Interfaz:** Clara con indicadores visuales para IN / OUT.

---

## 🧠 Reglas de negocio destacadas

* Los **movimientos de inventario son inmutables**.
* Cualquier corrección de stock se realiza mediante un **nuevo movimiento**.
* Las validaciones críticas se realizan en el **backend**.
* El frontend replica las validaciones para mejorar la experiencia de usuario.
* Uso de **excepciones personalizadas** para reglas de negocio.
* **Separación clara de responsabilidades:**
* Controller
* Service
* Repository



---


## 📖 Documentación de la API (Swagger)

Para facilitar la integración con el frontend y asegurar la calidad del software, la API cuenta con documentación técnica detallada.

* **Swagger UI:** Permite visualizar y probar todos los endpoints de forma interactiva.
* **Uso de DTOs:** La transferencia de datos se realiza mediante Data Transfer Objects para proteger las entidades del dominio.
* **Validaciones Robustas:** Se implementó *Bean Validation* para asegurar que los datos cumplan con los formatos y reglas requeridas antes de ser procesados.

📂 **[Ver Guía: ¿Cómo documenté mi API?](./docs/C%C3%B3mo%20document%C3%A9%20mi%20API%20as%C3%AD.pdf)**
*(En este documento detallo el proceso de configuración y cómo se reflejan las validaciones en la interfaz de Swagger).*
---

## 🖥️ Tecnologías utilizadas

### **Backend**

* **Java 21**
* **Spring Boot**
* **Spring Data JPA**
* **Hibernate**
* **MySQL**
* **Maven**
* **JUnit 5** y **Mockito** (testing)

### **Frontend**

* **Angular 21**
* **TypeScript**
* **Bootstrap**
* **Angular Standalone Components**
* **RxJS**

---

## 📂 Estructura general del proyecto

```text
inventoryProyectFullStack/
│
├── backend/
│   └── inventory-app (Spring Boot)
│
├── frontend/
│   └── inventory-app (Angular)
│
└── docs/
    └── Como-documente-mi-API-asi.pdf

```

---

## ⚙️ Cómo ejecutar el proyecto en local

### 🔧 Requisitos previos

* Java **21**
* Maven
* Node.js (recomendado LTS)
* Angular CLI
* MySQL

---

## 🗄️ Base de datos

### Crear la base de datos:

```sql
CREATE DATABASE inventory_db;

```

### Configurar las credenciales en el backend (`application.properties`):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD

```

---

## ☕ Backend

### Ejecutar:

```bash
cd backend/inventory-app
mvn clean install
mvn spring-boot:run

```

> **Backend disponible en:** `http://localhost:8080`

---

## 🅰️ Angular

### Ejecutar:

```bash
cd frontend/inventory-app
npm install
ng serve -o

```

> **Frontend disponible en:** `http://localhost:4200`

---

## 🧪 Testing

El backend cuenta con **316 tests propios** (JUnit 5 + Mockito), más el test original de Spring (`contextLoads`). Los tests unitarios **no usan base de datos**: corren en segundos y no dependen de MySQL.

### 🎯 Por qué importan

* **Protegen las reglas de negocio.** Reglas como "no se puede eliminar un producto con movimientos" o "no se permite una salida sin stock suficiente" quedan verificadas automáticamente, no solo probadas a mano.
* **Permiten refactorizar sin miedo.** Si un cambio rompe una regla, un test falla en segundos y señala dónde.
* **Sirven como documentación viva.** Cada test lleva un `@DisplayName` que describe el comportamiento esperado en lenguaje de negocio, por ejemplo: *"OUT con cantidad igual al stock deja el stock en 0"*.
* **Facilitan la evolución del proyecto.** Con las reglas actuales cubiertas, se pueden sumar funcionalidades nuevas (como login) con la seguridad de no romper lo que ya funciona.

### 🗺️ Qué se testea y con qué herramienta

| Capa | Herramienta | Qué verifica |
|------|-------------|--------------|
| **DTOs de request** | JUnit 5 + Bean Validation (`Validator` real) | Campos obligatorios, límites de longitud (mín, máx, mín-1, máx+1), patrones y valores numéricos |
| **DTOs de response** | JUnit 5 | Constructor, getters y setters |
| **Modelos** | JUnit 5 | Builder, constructor vacío y valores por defecto |
| **Services** | JUnit 5 + **Mockito** (`@Mock`, `@InjectMocks`, `ArgumentCaptor`, `InOrder`) | Reglas de negocio, caminos de error y orden de las operaciones |
| **Controllers** | `@WebMvcTest` + `MockMvc` + `@MockBean` | Códigos HTTP, cuerpo de la respuesta y validación del body |
| **Excepciones y `GlobalExceptionHandler`** | JUnit 5 | Mensaje, status HTTP y formato de cada error |
| **Configuración (`OpenApiConfig`)** | JUnit 5 | Título y versión de Swagger |
| **Reportes (PDF)** | JUnit 5 + Mockito + `@WebMvcTest` | Generación del PDF y respuesta del endpoint |

### 📊 Cobertura por lote

| Lote | Alcance | Tests |
|------|---------|-------|
| 1 | Category | 58 |
| 2 | Supplier | 71 |
| 3 | Product | 74 |
| 4 | InventoryMovement | 65 |
| 5 | Excepciones, `GlobalExceptionHandler` y `OpenApiConfig` | 42 |
| 6 | Reportes (`PdfReportService` y `ReportController`) | 6 |
| | **Total propio** | **316** |

### 🔍 Ejemplos de lo que se verifica

**1. Una salida que deja el stock exactamente en 0 está permitida.**
Con stock 10 y un movimiento OUT de 10, el stock final es 0 y el movimiento se guarda con tipo, cantidad y producto correctos. Este caso límite evita bloquear salidas legítimas.

**2. Los errores no dejan rastro.**
Si un movimiento es inválido (cantidad 0, tipo incorrecto, producto inexistente o stock insuficiente), se verifica con Mockito que **no se guardó nada** y que no hubo llamadas inesperadas a los repositorios.

**3. El orden de las operaciones importa.**
Con `InOrder` se comprueba que al registrar un movimiento se guarda primero el producto con su stock actualizado y después el movimiento.

**4. Un producto sin movimientos sí se puede eliminar, aunque tenga stock inicial.**
La regla es "no eliminar si tiene movimientos". Es una decisión de diseño deliberada: permite corregir errores de carga (por ejemplo, un producto dado de alta por equivocación) sin dejar registros innecesarios en el sistema. En cambio, una vez que el producto registra un ingreso o una salida, pasa a formar parte del historial de auditoría y queda protegido contra la eliminación.

**5. El update de un producto nunca toca el stock.**
Con `ArgumentCaptor` se comprueba que el stock persistido no cambia al editar. Además, categoría y proveedor solo se reemplazan cuando el producto no tiene movimientos.

**6. Cada error devuelve el formato que corresponde.**
`GlobalExceptionHandler` se prueba handler por handler: 400 para validaciones y duplicados, 404 para recursos inexistentes, 409 para conflictos de integridad y 500 para errores inesperados.

**7. Las validaciones de los DTOs se prueban con límites exactos.**
Por ejemplo, el nombre de una categoría acepta 2 y 40 caracteres, y rechaza 1 y 41.

### ▶️ Cómo ejecutar los tests

Desde la carpeta `backend/`:

```bash
# Todos los tests unitarios (sin necesidad de MySQL)
mvn test -Dtest='!InventoryprojectApplicationTests'

# Todos los tests, incluido el de contexto de Spring
mvn test

# Solo una entidad, por ejemplo Product
mvn test -Dtest="Product*Test"
```

> ⚠️ `InventoryprojectApplicationTests` (`contextLoads`) levanta la aplicación completa y **necesita MySQL en ejecución**. Sin la base de datos, ese único test falla; los demás no se ven afectados.

### 📝 Convenciones de los tests

* Un test por comportamiento, con estructura **Arrange / Act / Assert**.
* Nombres `testXxx` y `@DisplayName` en español describiendo la regla de negocio.
* JavaDoc en los tests que documentan un comportamiento no obvio.
* Los tests de unidad no usan base de datos real.

---

## 🔮 Próximos pasos

El proyecto está pensado para seguir creciendo. Las próximas versiones contemplan:

* 🧪 **Seguir ampliando la cobertura de tests** a medida que se sumen funcionalidades.
* 🔐 **Login y autenticación de usuarios**, con acceso diferenciado a la información.
* 🤖 **Chatbot de consulta de stock**: luego del login, cada persona podrá consultar el stock de forma conversacional.
* 🚀 **Nuevas funcionalidades** sobre la base ya probada y documentada.

---

## 📚 Contexto de aprendizaje

Este proyecto surge como evolución de una consigna de curso y fue ampliado con:

* Nuevas entidades
* Reglas de negocio reales
* Validaciones robustas
* Integración completa frontend-backend
* Buenas prácticas de diseño
* Tests automatizados y documentación técnica

---

## 👩‍💻 Autora

**Nadia Cendra**

**Link a demo del proyecto:** [https://drive.google.com/file/d/1qEPnR7cspDyisZWhy6leafh1jSbTJdXk/view?usp=sharing]

> ℹ️ Esta demo corresponde a la **versión 1** del proyecto, que todavía no incluía los tests ni la documentación técnica. La **versión actual (v2)** incorpora la suite de tests automatizados y la documentación del código. La demo se irá actualizando a medida que se incorporen las nuevas funcionalidades.

---
