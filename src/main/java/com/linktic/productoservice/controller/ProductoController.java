package com.linktic.productoservice.controller;

import com.linktic.productoservice.dto.JsonApiResponse;
import com.linktic.productoservice.dto.ProductoRequest;
import com.linktic.productoservice.model.Producto;
import com.linktic.productoservice.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "API para gestión de productos")
public class ProductoController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                    content = @Content(schema = @Schema(implementation = JsonApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(schema = @Schema(implementation = JsonApiResponse.class))),
        @ApiResponse(responseCode = "409", description = "Producto ya existe",
                    content = @Content(schema = @Schema(implementation = JsonApiResponse.class)))
    })
    public ResponseEntity<JsonApiResponse<Producto>> crearProducto(
            @Parameter(description = "Datos del producto a crear", required = true)
            @Valid @RequestBody ProductoRequest request) {
        
        logger.info("Recibida solicitud para crear producto: {}", request.getNombre());
        
        try {
            Producto producto = productoService.crearProducto(request);
            JsonApiResponse<Producto> response = JsonApiResponse.success(producto);
            
            logger.info("Producto creado exitosamente con ID: {}", producto.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            logger.error("Error al crear producto: {}", e.getMessage());
            JsonApiResponse<Producto> errorResponse = JsonApiResponse.error(
                "Error al crear producto", e.getMessage(), "409");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Obtiene un producto específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(schema = @Schema(implementation = JsonApiResponse.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(schema = @Schema(implementation = JsonApiResponse.class)))
    })
    public ResponseEntity<JsonApiResponse<Producto>> obtenerProductoPorId(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Integer id) {
        
        logger.info("Recibida solicitud para obtener producto con ID: {}", id);
        
        return productoService.obtenerProductoPorId(id)
                .map(producto -> {
                    logger.info("Producto encontrado: {}", producto.getNombre());
                    return ResponseEntity.ok(JsonApiResponse.success(producto));
                })
                .orElseGet(() -> {
                    logger.warn("Producto con ID {} no encontrado", id);
                    JsonApiResponse<Producto> errorResponse = JsonApiResponse.error(
                        "Producto no encontrado", "No se encontró un producto con el ID: " + id, "404");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                });
    }

    @GetMapping
    @Operation(summary = "Listar todos los productos", description = "Obtiene la lista de todos los productos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = JsonApiResponse.class)))
    })
    public ResponseEntity<JsonApiResponse<List<Producto>>> listarTodosLosProductos() {
        logger.info("Recibida solicitud para listar todos los productos");
        
        List<Producto> productos = productoService.listarTodosLosProductos();
        JsonApiResponse<List<Producto>> response = JsonApiResponse.success(productos);
        
        logger.info("Se devolvieron {} productos", productos.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar/nombre")
    @Operation(summary = "Buscar productos por nombre", description = "Busca productos que contengan el nombre especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos encontrados",
                    content = @Content(schema = @Schema(implementation = JsonApiResponse.class)))
    })
    public ResponseEntity<JsonApiResponse<List<Producto>>> buscarProductosPorNombre(
            @Parameter(description = "Nombre a buscar", required = true)
            @RequestParam String nombre) {
        
        logger.info("Recibida solicitud para buscar productos por nombre: {}", nombre);
        
        List<Producto> productos = productoService.buscarProductosPorNombre(nombre);
        JsonApiResponse<List<Producto>> response = JsonApiResponse.success(productos);
        
        logger.info("Se encontraron {} productos que contienen '{}'", productos.size(), nombre);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar/precio")
    @Operation(summary = "Buscar productos por rango de precio", description = "Busca productos dentro de un rango de precios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos encontrados",
                    content = @Content(schema = @Schema(implementation = JsonApiResponse.class)))
    })
    public ResponseEntity<JsonApiResponse<List<Producto>>> buscarProductosPorPrecio(
            @Parameter(description = "Precio mínimo", required = true)
            @RequestParam BigDecimal minPrecio,
            @Parameter(description = "Precio máximo", required = true)
            @RequestParam BigDecimal maxPrecio) {
        
        logger.info("Recibida solicitud para buscar productos con precio entre {} y {}", minPrecio, maxPrecio);
        
        List<Producto> productos = productoService.buscarProductosPorPrecio(minPrecio, maxPrecio);
        JsonApiResponse<List<Producto>> response = JsonApiResponse.success(productos);
        
        logger.info("Se encontraron {} productos en el rango de precio", productos.size());
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonApiResponse<Object>> handleGenericException(Exception e) {
        logger.error("Error no manejado: {}", e.getMessage(), e);
        JsonApiResponse<Object> errorResponse = JsonApiResponse.error(
            "Error interno del servidor", "Ha ocurrido un error inesperado", "500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}