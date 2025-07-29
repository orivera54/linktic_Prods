package com.linktic.productoservice.service;

import com.linktic.productoservice.dto.ProductoRequest;
import com.linktic.productoservice.model.Producto;
import com.linktic.productoservice.repository.ProductoRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @CircuitBreaker(name = "default")
    @Retry(name = "default")
    public Producto crearProducto(ProductoRequest request) {
        logger.info("Creando nuevo producto: {}", request.getNombre());
        
        if (productoRepository.existsByNombre(request.getNombre())) {
            logger.warn("Producto con nombre '{}' ya existe", request.getNombre());
            throw new RuntimeException("Ya existe un producto con el nombre: " + request.getNombre());
        }

        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setPrecio(request.getPrecio());
        producto.setDescripcion(request.getDescripcion());

        Producto savedProducto = productoRepository.save(producto);
        logger.info("Producto creado exitosamente con ID: {}", savedProducto.getId());
        
        return savedProducto;
    }

    @CircuitBreaker(name = "default")
    @Retry(name = "default")
    public Optional<Producto> obtenerProductoPorId(Integer id) {
        logger.info("Buscando producto con ID: {}", id);
        
        Optional<Producto> producto = productoRepository.findById(id);
        
        if (producto.isPresent()) {
            logger.info("Producto encontrado: {}", producto.get().getNombre());
        } else {
            logger.warn("Producto con ID {} no encontrado", id);
        }
        
        return producto;
    }

    @CircuitBreaker(name = "default")
    @Retry(name = "default")
    public List<Producto> listarTodosLosProductos() {
        logger.info("Obteniendo todos los productos");
        
        List<Producto> productos = productoRepository.findAll();
        logger.info("Se encontraron {} productos", productos.size());
        
        return productos;
    }

    @CircuitBreaker(name = "default")
    @Retry(name = "default")
    public Optional<Producto> obtenerProductoPorNombre(String nombre) {
        logger.info("Buscando producto por nombre: {}", nombre);
        
        Optional<Producto> producto = productoRepository.findByNombre(nombre);
        
        if (producto.isPresent()) {
            logger.info("Producto encontrado: {}", producto.get().getNombre());
        } else {
            logger.warn("Producto con nombre '{}' no encontrado", nombre);
        }
        
        return producto;
    }

    @CircuitBreaker(name = "default")
    @Retry(name = "default")
    public List<Producto> buscarProductosPorPrecio(java.math.BigDecimal minPrecio, java.math.BigDecimal maxPrecio) {
        logger.info("Buscando productos con precio entre {} y {}", minPrecio, maxPrecio);
        
        List<Producto> productos = productoRepository.findByPrecioBetween(minPrecio, maxPrecio);
        logger.info("Se encontraron {} productos en el rango de precio", productos.size());
        
        return productos;
    }

    @CircuitBreaker(name = "default")
    @Retry(name = "default")
    public List<Producto> buscarProductosPorNombre(String nombre) {
        logger.info("Buscando productos que contengan: {}", nombre);
        
        List<Producto> productos = productoRepository.findByNombreContaining(nombre);
        logger.info("Se encontraron {} productos que contienen '{}'", productos.size(), nombre);
        
        return productos;
    }
}