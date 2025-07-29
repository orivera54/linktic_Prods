# Producto Service

Microservicio REST para gestión de productos desarrollado con Spring Boot, siguiendo la especificación JSON API.

## Características

- ✅ **JSON API**: Implementación completa de la especificación JSON API
- ✅ **Docker**: Containerización optimizada con multi-stage build
- ✅ **MySQL**: Base de datos MySQL con configuración automática
- ✅ **Swagger**: Documentación automática de la API
- ✅ **Autenticación**: API Key para comunicación entre servicios
- ✅ **Circuit Breaker**: Patrón de resiliencia con Resilience4j
- ✅ **Logs Estructurados**: Sistema de logging avanzado
- ✅ **Health Checks**: Monitoreo de salud del servicio
- ✅ **Pruebas Unitarias**: Cobertura ≥ 80%
- ✅ **Arquitectura Limpia**: Separación clara de responsabilidades

## Arquitectura del Servicio

```
┌─────────────────────────────────────────────────────────────────────┐
│                        Cliente (API Consumer)                        │
└───────────────────────────────────┬─────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                            API Gateway                              │
│                     (API Key Authentication)                        │
└───────────────────────────────────┬─────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         Producto Service                            │
├─────────────────────────────────────────────────────────────────────┤
│ ┌─────────────┐    ┌─────────────┐    ┌─────────────┐             │
│ │  Controller │ ─► │   Service   │ ─► │ Repository  │             │
│ └─────────────┘    └─────────────┘    └─────────────┘             │
│         │                 │                   │                     │
│         ▼                 ▼                   ▼                     │
│ ┌─────────────┐    ┌─────────────┐    ┌─────────────┐             │
│ │    DTO     │    │   Domain    │    │    JPA      │             │
│ │   Models   │    │   Models    │    │  Entities   │             │
│ └─────────────┘    └─────────────┘    └─────────────┘             │
├─────────────────────────────────────────────────────────────────────┤
│                        Circuit Breaker                              │
│                        (Resilience4j)                              │
└───────────────────────────────────┬─────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         MySQL Database                              │
│                        (Productos Data)                            │
└─────────────────────────────────────────────────────────────────────┘
```

## Tecnologías

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security**
- **MySQL 8.0**
- **Docker & Docker Compose**
- **Resilience4j**
- **Swagger/OpenAPI 3**
- **JUnit 5 & Mockito**

## ¿Por qué MySQL?

La elección de MySQL como base de datos para este servicio se fundamenta en varios aspectos clave:

1. **Estructura de Datos Definida**: 
   - Los productos tienen un esquema fijo y bien definido
   - Las relaciones entre entidades son claras y predecibles
   - No se requiere flexibilidad en el esquema que justifique NoSQL

2. **Consistencia y ACID**:
   - Garantía de transacciones ACID críticas para operaciones financieras
   - Integridad referencial importante para mantener datos coherentes
   - Consistencia inmediata en todas las operaciones

3. **Rendimiento**:
   - Excelente rendimiento en operaciones CRUD simples
   - Optimización eficiente para consultas por rangos (ej: búsqueda por precio)
   - Índices B-tree efectivos para búsquedas por ID y nombre

4. **Madurez y Soporte**:
   - Amplia adopción en la industria
   - Integración robusta con Spring Boot
   - Gran cantidad de herramientas de administración disponibles

5. **Escalabilidad**:
   - Suficiente para la carga esperada del servicio
   - Opciones de replicación y sharding disponibles si se necesitan
   - Buena relación costo-beneficio en términos de recursos

Una base de datos NoSQL podría ser más apropiada si necesitáramos:
- Esquemas flexibles o dinámicos
- Escalabilidad horizontal masiva
- Manejo de datos no estructurados
- Consistencia eventual en lugar de inmediata

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/linktic/productoservice/
│   │   ├── config/          # Configuraciones
│   │   ├── controller/      # Controladores REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── model/          # Entidades JPA
│   │   ├── repository/     # Repositorios
│   │   └── service/        # Lógica de negocio
│   └── resources/
│       ├── application.yml # Configuración principal
│       └── application-test.yml # Configuración de pruebas
└── test/
    └── java/com/linktic/productoservice/
        ├── controller/     # Pruebas de controladores
        └── service/        # Pruebas de servicios
```

## Instalación y Ejecución

### Prerrequisitos

- Java 17 o superior
- Maven 3.6+
- Docker y Docker Compose
- MySQL 8.0 (opcional, se incluye en docker-compose)

### Opción 1: Ejecución Local

1. **Clonar el repositorio**
   ```bash
   git clone <repository-url>
   cd producto-service
   ```

2. **Configurar base de datos MySQL**
   ```sql
   CREATE DATABASE ltprods;
   USE ltprods;
   
   CREATE TABLE Producto (
       id INT AUTO_INCREMENT PRIMARY KEY,
       nombre VARCHAR(100) NOT NULL,
       precio DECIMAL(10,2) NOT NULL,
       descripcion TEXT,
       fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
       fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
   );
   ```

3. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```

### Opción 2: Ejecución con Docker

1. **Construir y ejecutar con Docker Compose**
   ```bash
   docker-compose up --build
   ```

2. **Verificar que los servicios estén funcionando**
   ```bash
   docker-compose ps
   ```

## Endpoints de la API

### Base URL
```
http://localhost:8080/api/v1
```

### Autenticación
Todas las peticiones requieren el header:
```
X-API-Key: producto-service-key-2024
```

### Endpoints Disponibles

#### 1. Crear Producto
```http
POST /productos
Content-Type: application/json
X-API-Key: producto-service-key-2024

{
  "nombre": "Laptop Dell",
  "precio": 1500.00,
  "descripcion": "Laptop Dell Inspiron 15"
}
```

```bash
curl -X POST "http://localhost:8080/api/v1/productos" \
     -H "Content-Type: application/json" \
     -H "X-API-Key: producto-service-key-2024" \
     -d '{
           "nombre": "Laptop Dell",
           "precio": 1500.00,
           "descripcion": "Laptop Dell Inspiron 15"
         }'
```

#### 2. Obtener Producto por ID
```http
GET /productos/{id}
X-API-Key: producto-service-key-2024
```

```bash
curl -X GET "http://localhost:8080/api/v1/productos/1" \
     -H "X-API-Key: producto-service-key-2024"
```

#### 3. Listar Todos los Productos
```http
GET /productos
X-API-Key: producto-service-key-2024
```

```bash
curl -X GET "http://localhost:8080/api/v1/productos" \
     -H "X-API-Key: producto-service-key-2024"
```

#### 4. Buscar Productos por Nombre
```http
GET /productos/buscar/nombre?nombre=Laptop
X-API-Key: producto-service-key-2024
```

```bash
curl -X GET "http://localhost:8080/api/v1/productos/buscar/nombre?nombre=Laptop" \
     -H "X-API-Key: producto-service-key-2024"
```

#### 5. Buscar Productos por Rango de Precio
```http
GET /productos/buscar/precio?minPrecio=100&maxPrecio=1000
X-API-Key: producto-service-key-2024
```

```bash
curl -X GET "http://localhost:8080/api/v1/productos/buscar/precio?minPrecio=100&maxPrecio=1000" \
     -H "X-API-Key: producto-service-key-2024"
```

### Respuestas JSON API

Todas las respuestas siguen la especificación JSON API:

#### Respuesta Exitosa
```json
{
  "data": {
    "id": 1,
    "nombre": "Laptop Dell",
    "precio": 1500.00,
    "descripcion": "Laptop Dell Inspiron 15",
    "fechaCreacion": "2024-01-15T10:30:00",
    "fechaActualizacion": "2024-01-15T10:30:00"
  },
  "meta": {
    "timestamp": "2024-01-15T10:30:00",
    "version": "1.0.0"
  }
}
```

#### Respuesta de Error
```json
{
  "errors": [
    {
      "title": "Producto no encontrado",
      "detail": "No se encontró un producto con el ID: 999",
      "status": "404",
      "timestamp": "2024-01-15T10:30:00"
    }
  ]
}
```

## Documentación Swagger

La documentación interactiva está disponible en:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## Monitoreo y Health Checks

### Endpoints de Actuator
- **Health Check**: http://localhost:8080/actuator/health
- **Info**: http://localhost:8080/actuator/info
- **Metrics**: http://localhost:8080/actuator/metrics

### Health Check Response
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "MySQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 499963174912,
        "free": 419430400000,
        "threshold": 10485760
      }
    }
  }
}
```

## Pruebas

### Ejecutar Pruebas Unitarias
```bash
mvn test
```

### Ejecutar Pruebas con Cobertura
```bash
mvn clean test jacoco:report
```

### Verificar Cobertura
El reporte de cobertura se genera en: `target/site/jacoco/index.html`

## Configuración

### Variables de Entorno

| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `SPRING_DATASOURCE_URL` | URL de conexión a MySQL | `jdbc:mysql://localhost:3306/ltprods` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de MySQL | `root` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de MySQL | `Valery2005*` |
| `APP_SECURITY_API_KEY` | API Key para autenticación | `producto-service-key-2024` |
| `SERVER_PORT` | Puerto del servidor | `8080` |

### Configuración de Logs

Los logs se escriben en:
- **Consola**: Formato estructurado
- **Archivo**: `logs/producto-service.log`

### Configuración de Circuit Breaker

- **Sliding Window Size**: 10
- **Failure Rate Threshold**: 50%
- **Wait Duration**: 10 segundos
- **Max Attempts**: 3

## Integración con Otros Servicios

### Ejemplo de Llamada desde el Servicio de Inventario

```java
@Service
public class InventarioService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    public Producto obtenerProducto(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", "producto-service-key-2024");
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<JsonApiResponse> response = restTemplate.exchange(
            "http://producto-service:8080/api/v1/productos/" + id,
            HttpMethod.GET,
            entity,
            JsonApiResponse.class
        );
        
        return response.getBody().getData();
    }
}
```

## Despliegue

### Docker Hub
```bash
# Construir imagen
docker build -t linktic/producto-service:latest .

# Subir a Docker Hub
docker push linktic/producto-service:latest
```

### Kubernetes
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: producto-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: producto-service
  template:
    metadata:
      labels:
        app: producto-service
    spec:
      containers:
      - name: producto-service
        image: linktic/producto-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_DATASOURCE_URL
          value: "jdbc:mysql://mysql-service:3306/ltprods"
        - name: APP_SECURITY_API_KEY
          valueFrom:
            secretKeyRef:
              name: producto-service-secret
              key: api-key
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
```

## Troubleshooting

### Problemas Comunes

1. **Error de conexión a MySQL**
   - Verificar que MySQL esté ejecutándose
   - Verificar credenciales en `application.yml`
   - Verificar que la base de datos `ltprods` exista

2. **Error de autenticación**
   - Verificar que el header `X-API-Key` esté presente
   - Verificar que el valor del API Key sea correcto

3. **Error de validación**
   - Verificar que los datos enviados cumplan con las validaciones
   - Revisar los logs para detalles específicos

### Logs de Debug

Para habilitar logs de debug, agregar en `application.yml`:
```yaml
logging:
  level:
    com.linktic: DEBUG
    org.springframework.security: DEBUG
```



## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## Contacto

- **Correo**: orivera54@gmail.com
