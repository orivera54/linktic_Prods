package com.linktic.productoservice.service;

import com.linktic.productoservice.dto.ProductoRequest;
import com.linktic.productoservice.model.Producto;
import com.linktic.productoservice.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private ProductoRequest productoRequest;
    private Producto producto;

    @BeforeEach
    void setUp() {
        productoRequest = new ProductoRequest();
        productoRequest.setNombre("Laptop Dell");
        productoRequest.setPrecio(new BigDecimal("1500.00"));
        productoRequest.setDescripcion("Laptop Dell Inspiron 15");

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop Dell");
        producto.setPrecio(new BigDecimal("1500.00"));
        producto.setDescripcion("Laptop Dell Inspiron 15");
    }

    @Test
    void crearProducto_Success() {
        // Given
        when(productoRepository.existsByNombre("Laptop Dell")).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // When
        Producto result = productoService.crearProducto(productoRequest);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop Dell", result.getNombre());
        assertEquals(new BigDecimal("1500.00"), result.getPrecio());
        assertEquals("Laptop Dell Inspiron 15", result.getDescripcion());

        verify(productoRepository).existsByNombre("Laptop Dell");
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void crearProducto_ProductoYaExiste_ThrowsException() {
        // Given
        when(productoRepository.existsByNombre("Laptop Dell")).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.crearProducto(productoRequest);
        });

        assertEquals("Ya existe un producto con el nombre: Laptop Dell", exception.getMessage());
        verify(productoRepository).existsByNombre("Laptop Dell");
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void obtenerProductoPorId_Success() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // When
        Optional<Producto> result = productoService.obtenerProductoPorId(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Laptop Dell", result.get().getNombre());
        verify(productoRepository).findById(1L);
    }

    @Test
    void obtenerProductoPorId_ProductoNoEncontrado_ReturnsEmpty() {
        // Given
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Producto> result = productoService.obtenerProductoPorId(999L);

        // Then
        assertFalse(result.isPresent());
        verify(productoRepository).findById(999L);
    }

    @Test
    void listarTodosLosProductos_Success() {
        // Given
        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Mouse Logitech");
        producto2.setPrecio(new BigDecimal("25.00"));
        producto2.setDescripcion("Mouse inalámbrico Logitech");

        List<Producto> productos = Arrays.asList(producto, producto2);
        when(productoRepository.findAll()).thenReturn(productos);

        // When
        List<Producto> result = productoService.listarTodosLosProductos();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Laptop Dell", result.get(0).getNombre());
        assertEquals("Mouse Logitech", result.get(1).getNombre());
        verify(productoRepository).findAll();
    }

    @Test
    void obtenerProductoPorNombre_Success() {
        // Given
        when(productoRepository.findByNombre("Laptop Dell")).thenReturn(Optional.of(producto));

        // When
        Optional<Producto> result = productoService.obtenerProductoPorNombre("Laptop Dell");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Laptop Dell", result.get().getNombre());
        verify(productoRepository).findByNombre("Laptop Dell");
    }

    @Test
    void obtenerProductoPorNombre_ProductoNoEncontrado_ReturnsEmpty() {
        // Given
        when(productoRepository.findByNombre("Producto Inexistente")).thenReturn(Optional.empty());

        // When
        Optional<Producto> result = productoService.obtenerProductoPorNombre("Producto Inexistente");

        // Then
        assertFalse(result.isPresent());
        verify(productoRepository).findByNombre("Producto Inexistente");
    }

    @Test
    void buscarProductosPorPrecio_Success() {
        // Given
        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Mouse Logitech");
        producto2.setPrecio(new BigDecimal("25.00"));

        List<Producto> productos = Arrays.asList(producto, producto2);
        when(productoRepository.findByPrecioBetween(any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(productos);

        // When
        List<Producto> result = productoService.buscarProductosPorPrecio(
                new BigDecimal("20.00"), new BigDecimal("2000.00"));

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productoRepository).findByPrecioBetween(
                new BigDecimal("20.00"), new BigDecimal("2000.00"));
    }

    @Test
    void buscarProductosPorNombre_Success() {
        // Given
        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Laptop HP");
        producto2.setPrecio(new BigDecimal("1200.00"));

        List<Producto> productos = Arrays.asList(producto, producto2);
        when(productoRepository.findByNombreContaining("Laptop")).thenReturn(productos);

        // When
        List<Producto> result = productoService.buscarProductosPorNombre("Laptop");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getNombre().contains("Laptop")));
        verify(productoRepository).findByNombreContaining("Laptop");
    }
}