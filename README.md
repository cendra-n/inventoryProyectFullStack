# 📦 Inventory Project – Full Stack (Angular + Spring Boot)

## 📌 Descripción general

Este proyecto es una **aplicación de inventarios full stack**, desarrollada con **Angular (frontend)** y **Spring Boot (backend)**.

El punto de partida fue una **consigna de un curso de Udemy**, cuyo alcance inicial era muy básico:
- Solo existía la entidad **Producto**
- El home mostraba únicamente una lista simple de productos
- No había validaciones
- No existían relaciones entre entidades
- No había control real de inventario

A partir de esa base, el proyecto fue **extendido y rediseñado completamente**, incorporando nuevas entidades, validaciones, reglas de negocio y funcionalidades propias de un sistema de inventarios real.

---

## 🚀 Funcionalidades implementadas

### 🧱 Entidades del sistema

El sistema cuenta actualmente con **4 entidades principales**:

- **Product (Producto)**
- **Category (Categoría)**
- **Supplier (Proveedor)**
- **InventoryMovement (Movimiento de inventario)**

---

### 📦 Productos

- CRUD completo
- Listado con:
  - **Búsqueda parcial por nombre** (ingresando caracteres mínimos)
  - **Descarga de PDF** con todos los productos cargados en la base de datos
- Validaciones desde backend y reflejadas en frontend

---

### 🗂️ Categorías

- CRUD completo
- **Búsqueda parcial por nombre** (con mínimo de caracteres)
- Validaciones adicionales en backend
- **No permite eliminar una categoría si tiene productos asociados**
- Manejo mediante **excepción personalizada**

---

### 🚚 Proveedores

- CRUD completo
- Validaciones desde backend y frontend
- **No permite eliminar un proveedor si tiene productos asociados**
- Excepción personalizada para reglas de negocio
- **Búsqueda parcial por nombre**

---

### 🔄 Movimientos de Inventario

- Registro de movimientos:
  - **IN (Entrada)**
  - **OUT (Salida)**
- Cada movimiento impacta directamente en el stock del producto
- **No se permite salida sin stock suficiente**
- Los movimientos:
  - **No se editan**
  - **No se eliminan**
- Listado completo de movimientos
- Filtros de búsqueda:
  - Por producto
  - Por rango de fechas
  - Por producto + rango de fechas
- Interfaz clara con indicadores visuales para IN / OUT

---

## 🧠 Reglas de negocio destacadas

- Los **movimientos de inventario son inmutables**
- Cualquier corrección de stock se realiza mediante un **nuevo movimiento**
- Las validaciones críticas se realizan en el **backend**
- El frontend replica las validaciones para mejorar la experiencia de usuario
- Uso de excepciones personalizadas para reglas de negocio
- Separación clara de responsabilidades:
  - Controller
  - Service
  - Repository

---

## 🖥️ Tecnologías utilizadas

### Backend
- Java **21**
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven

### Frontend
- Angular **21**
- TypeScript
- Bootstrap
- Angular Standalone Components
- RxJS

---

## 📂 Estructura general del proyecto
inventoryProyectFullStack/
│
├── backend/
│   └── inventory-app (Spring Boot)
│
└── frontend/
    └── inventory-app (Angular)


---

## ⚙️ Cómo ejecutar el proyecto en local

### 🔧 Requisitos previos

- Java **21**
- Maven
- Node.js (recomendado LTS)
- Angular CLI
- MySQL

---

## 🗄️ Base de datos

###  Crear la base de datos:

```sql
CREATE DATABASE inventory_db;

Configurar las credenciales en el backend (application.properties):
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
---

## 🗄️ Backend
### Ejecutar:
cd backend
mvn clean install
mvn spring-boot:run
backend disponible en: http://localhost:8080
---

## 🗄️ Angular
### Ejecutar:
cd frontend
npm install
ng serve -o
frontend disponible en: http://localhost:4200
---

🧪 Testing
---------------------------------------

Actualmente el proyecto no incluye tests automatizados. Serán implementados en la próxima versión.



📚 Contexto de aprendizaje
--------------------------------------

Este proyecto surge como evolución de una consigna de curso y fue ampliado con:

Nuevas entidades

Reglas de negocio reales

Validaciones robustas

Integración completa frontend-backend

Buenas prácticas de diseño
----------------------------------------

👩‍💻 Autora
Nadia Cendra

Link a demo del proyecto: 

