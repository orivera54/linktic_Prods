package com.linktic.productoservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linktic.productoservice.dto.ProductoRequest;
import com.linktic.productoservice.model.Producto;
import com.linktic.productoservice.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
@ActiveProfiles("test")
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void crearProducto_Success() throws Exception {
        // Given
        when(productoService.crearProducto(any(ProductoRequest.class))).thenReturn(producto);

        // When & Then
        mockMvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nombre").value("Laptop Dell"))
                .andExpect(jsonPath("$.data.precio").value(1500.00))
                .andExpect(jsonPath("$.data.descripcion").value("Laptop Dell Inspiron 15"));

        verify(productoService).crearProducto(any(ProductoRequest.class));
    }

    @Test
    void crearProducto_ProductoYaExiste_ReturnsConflict() throws Exception {
        // Given
        when(productoService.crearProducto(any(ProductoRequest.class)))
                .thenThrow(new RuntimeException("Ya existe un producto con el nombre: Laptop Dell"));

        // When & Then
        mockMvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errors[0].title").value("Error al crear producto"))
                .andExpect(jsonPath("$.errors[0].detail").value("Ya existe un producto con el nombre: Laptop Dell"));

        verify(productoService).crearProducto(any(ProductoRequest.class));
    }

    @Test
    void crearProducto_InvalidRequest_ReturnsBadRequest() throws Exception {
        // Given
        ProductoRequest invalidRequest = new ProductoRequest();
        invalidRequest.setPrecio(new BigDecimal("-100.00")); // Invalid price

        // When & Then
        mockMvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(productoService, never()).crearProducto(any(ProductoRequest.class));
    }

    @Test
    void obtenerProductoPorId_Success() throws Exception {
        // Given
        when(productoService.obtenerProductoPorId(1L)).thenReturn(Optional.of(producto));

        // When & Then
        mockMvc.perform(get("/api/v1/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nombre").value("Laptop Dell"))
                .andExpect(jsonPath("$.data.precio").value(1500.00));

        verify(productoService).obtenerProductoPorId(1L);
    }

    @Test
    void obtenerProductoPorId_ProductoNoEncontrado_ReturnsNotFound() throws Exception {
        // Given
        when(productoService.obtenerProductoPorId(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/productos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0].title").value("Producto no encontrado"))
                .andExpect(jsonPath("$.errors[0].detail").value("No se encontró un producto con el ID: 999"));

        verify(productoService).obtenerProductoPorId(999L);
    }

    @Test
    void listarTodosLosProductos_Success() throws Exception {
        // Given
        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Mouse Logitech");
        producto2.setPrecio(new BigDecimal("25.00"));

        List<Producto> productos = Arrays.asList(producto, producto2);
        when(productoService.listarTodosLosProductos()).thenReturn(productos);

        // When & Then
        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].nombre").value("Laptop Dell"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].nombre").value("Mouse Logitech"));

        verify(productoService).listarTodosLosProductos();
    }

    @Test
    void buscarProductosPorNombre_Success() throws Exception {
        // Given
        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Laptop HP");
        producto2.setPrecio(new BigDecimal("1200.00"));

        List<Producto> productos = Arrays.asList(producto, producto2);
        when(productoService.buscarProductosPorNombre("Laptop")).thenReturn(productos);

        // When & Then
        mockMvc.perform(get("/api/v1/productos/buscar/nombre")
                        .param("nombre", "Laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].nombre").value("Laptop Dell"))
                .andExpect(jsonPath("$.data[1].nombre").value("Laptop HP"));

        verify(productoService).buscarProductosPorNombre("Laptop");
    }

    @Test
    void buscarProductosPorPrecio_Success() throws Exception {
        // Given
        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Mouse Logitech");
        producto2.setPrecio(new BigDecimal("25.00"));

        List<Producto> productos = Arrays.asList(producto, producto2);
        when(productoService.buscarProductosPorPrecio(any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(productos);

        // When & Then
        mockMvc.perform(get("/api/v1/productos/buscar/precio")
                        .param("minPrecio", "20.00")
                        .param("maxPrecio", "2000.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));

        verify(productoService).buscarProductosPorPrecio(
                new BigDecimal("20.00"), new BigDecimal("2000.00"));
    }
}