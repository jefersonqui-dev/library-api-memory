# Biblioteca - API REST con Spring Boot

## Descripción
API REST completa para gestionar una biblioteca de libros, implementada siguiendo los principios de arquitectura por capas, principios SOLID, validación de datos y documentación automática. **Los datos se almacenan temporalmente en colecciones en memoria (Map) y se pierden al reiniciar la aplicación.**

## Arquitectura Implementada

### 1. **Arquitectura por Capas (Layered Architecture)**

```
┌─────────────────────────────────────┐
│           PRESENTATION LAYER        │
│         (Controllers/REST)          │
├─────────────────────────────────────┤
│            SERVICE LAYER            │
│         (Business Logic)            │
├─────────────────────────────────────┤
│          REPOSITORY LAYER           │
│         (Data Access - In Memory)   │
├─────────────────────────────────────┤
│            ENTITY LAYER             │
│         (Data Models)               │
└─────────────────────────────────────┘
```

#### **Capa de Presentación (Controllers)**
- `BookController`: Maneja todas las peticiones HTTP REST
- Implementa operaciones CRUD completas
- Incluye búsquedas por autor, título y disponibilidad
- Manejo de respuestas HTTP apropiadas

#### **Capa de Servicios (Services)**
- `BookService`: Contiene toda la lógica de negocio
- Conversión entre entidades y DTOs
- Validaciones de negocio

#### **Capa de Repositorio (Repositories)**
- `BookRepository`: Implementación en memoria usando `Map<Long, Book>`
- Métodos de búsqueda personalizados implementados con Streams
- Generación automática de IDs únicos
- **Datos se pierden al reiniciar la aplicación**

#### **Capa de Entidades (Entities)**
- `Book`: Modelo de datos principal
- Validaciones de datos con Bean Validation
- **Sin anotaciones JPA** (no hay persistencia real)

### 2. **Principios SOLID Implementados**

#### **S - Single Responsibility Principle**
- Cada clase tiene una sola responsabilidad
- `BookController`: Solo maneja peticiones HTTP
- `BookService`: Solo lógica de negocio
- `BookRepository`: Solo acceso a datos en memoria

#### **O - Open/Closed Principle**
- Abierto para extensión, cerrado para modificación
- Uso de interfaces para permitir implementaciones alternativas
- Fácil agregar nuevos tipos de búsqueda sin modificar código existente

#### **L - Liskov Substitution Principle**
- Las implementaciones pueden sustituir a las interfaces base
- `BookRepository` implementa todos los métodos necesarios

#### **I - Interface Segregation Principle**
- Interfaces específicas en lugar de una general
- `BookRepository` solo define métodos necesarios para libros

#### **D - Dependency Inversion Principle**
- Depender de abstracciones, no de implementaciones
- Inyección de dependencias con `@RequiredArgsConstructor`
- El servicio depende de la interfaz del repositorio

### 3. **Validación de Datos**

#### **Bean Validation**
- `@NotBlank`: Campos obligatorios no vacíos
- `@NotNull`: Campos obligatorios
- `@Size`: Validación de longitud de strings
- Mensajes de error personalizados en español

#### **Manejo de Errores**
- `GlobalExceptionHandler`: Manejo centralizado de excepciones
- Respuestas de error consistentes
- Validación automática con `@Valid`

### 4. **Documentación Automática**

#### **Swagger/OpenAPI 3**
- Documentación automática de todos los endpoints
- Ejemplos de uso y respuestas
- Interfaz web interactiva en `/swagger-ui.html`
- Metadatos de la API (versión, contacto, licencia)

## Tecnologías Utilizadas

- **Spring Boot 3.5.4**: Framework principal
- **Spring Web**: Para crear la API REST
- **Colecciones Java**: Map y List para almacenamiento en memoria
- **Lombok**: Reducción de código boilerplate
- **Bean Validation**: Validación de datos
- **Swagger/OpenAPI**: Documentación automática
- **Maven**: Gestión de dependencias

## Estructura del Proyecto

```
src/main/java/com/jquiguantar/library/library/
├── LibraryApplication.java          # Clase principal
├── config/
│   └── SwaggerConfig.java          # Configuración de Swagger
├── controller/
│   └── BookController.java         # Controlador REST
├── dto/
│   └── BookDto.java               # Objeto de transferencia
├── entity/
│   └── Book.java                  # Modelo de datos (sin JPA)
├── exception/
│   └── GlobalExceptionHandler.java # Manejador de excepciones
├── repository/
│   └── BookRepository.java        # Repositorio en memoria
└── service/
    └── BookService.java           # Lógica de negocio
```

## Endpoints de la API

### **Libros**
- `GET /api/books` - Obtener todos los libros
- `GET /api/books/{id}` - Obtener libro por ID
- `POST /api/books` - Crear nuevo libro
- `PUT /api/books/{id}` - Actualizar libro existente
- `DELETE /api/books/{id}` - Eliminar libro

### **Búsquedas**
- `GET /api/books/search/author?author={nombre}` - Buscar por autor
- `GET /api/books/search/title?title={titulo}` - Buscar por título
- `GET /api/books/buscar?q={texto}` - **Búsqueda general** (título o autor)
- `GET /api/books/available` - Obtener libros disponibles

### **Gestión de Préstamos**
- `POST /api/books/{id}/prestar` - **Prestar libro** (marcar como no disponible)
- `POST /api/books/{id}/devolver` - **Devolver libro** (marcar como disponible)

## Características de Almacenamiento en Memoria

### **Implementación**
- **Map<Long, Book>**: Almacenamiento principal usando HashMap
- **AtomicLong**: Generación automática de IDs únicos
- **Datos de ejemplo**: 5 libros clásicos pre-cargados al iniciar

### **Comportamiento**
- ✅ **Datos persistentes** durante la sesión de la aplicación
- ❌ **Datos se pierden** al reiniciar la aplicación
- ✅ **Operaciones rápidas** (sin acceso a disco)
- ✅ **Ideal para desarrollo y testing**

## Cómo Ejecutar

### **Requisitos**
- Java 21
- Maven 3.6+

### **Pasos**
1. Clonar el repositorio
2. Ejecutar: `mvn spring-boot:run`
3. La aplicación estará disponible en `http://localhost:8080`

### **Accesos**
- **API REST**: `http://localhost:8080/api/books`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`

## Características Destacadas

✅ **Arquitectura limpia y escalable**
✅ **Principios SOLID implementados**
✅ **Validación robusta de datos**
✅ **Documentación automática completa**
✅ **Manejo de errores centralizado**
✅ **Datos de ejemplo pre-cargados**
✅ **API RESTful completa**
✅ **Búsquedas avanzadas**
✅ **Almacenamiento en memoria eficiente**
✅ **Logging detallado**

## Ventajas del Almacenamiento en Memoria

- 🚀 **Rendimiento superior** (sin I/O de disco)
- 🔧 **Fácil desarrollo y testing**
- 📚 **Datos de ejemplo siempre disponibles**
- 🎯 **Ideal para prototipos y demos**
- 💾 **Sin configuración de base de datos**

## Limitaciones del Almacenamiento en Memoria

- ❌ **Datos se pierden al reiniciar**
- ❌ **No hay persistencia real**
- ❌ **No escalable para múltiples instancias**
- ❌ **No adecuado para producción**

## Próximos Pasos

- Implementar persistencia real con base de datos
- Agregar autenticación y autorización
- Implementar tests unitarios y de integración
- Agregar paginación para listas grandes
- Implementar cache con Redis
- Agregar auditoría de cambios
- Implementar métricas con Micrometer
