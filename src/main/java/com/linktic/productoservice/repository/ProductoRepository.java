package com.linktic.productoservice.repository;

import com.linktic.productoservice.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    Optional<Producto> findByNombre(String nombre);

    @Query("SELECT p FROM Producto p WHERE p.precio BETWEEN :minPrecio AND :maxPrecio")
    List<Producto> findByPrecioBetween(@Param("minPrecio") java.math.BigDecimal minPrecio, 
                                      @Param("maxPrecio") java.math.BigDecimal maxPrecio);

    @Query("SELECT p FROM Producto p WHERE p.nombre LIKE %:nombre%")
    List<Producto> findByNombreContaining(@Param("nombre") String nombre);

    boolean existsByNombre(String nombre);
}