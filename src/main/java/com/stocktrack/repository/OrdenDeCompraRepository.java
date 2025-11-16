package com.stocktrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stocktrack.model.OrdenDeCompra;

@Repository
public interface OrdenDeCompraRepository extends JpaRepository <OrdenDeCompra, Long>{
    
    // Le decimos a Spring: "Cuando llames a findAll,
    // también trae el proveedor Y los detalles,
    // Y el producto de cada detalle. ¡Todo de golpe!"
    @EntityGraph(attributePaths = {"proveedor", "detalles", "detalles.producto"})
    @Override
    List<OrdenDeCompra> findAll();

    // Hacemos lo mismo para findById
    @EntityGraph(attributePaths = {"proveedor", "detalles", "detalles.producto"})
    @Override
    java.util.Optional<OrdenDeCompra> findById(Long id);
}
