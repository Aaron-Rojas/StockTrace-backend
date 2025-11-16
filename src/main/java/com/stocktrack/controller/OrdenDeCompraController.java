package com.stocktrack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocktrack.dto.OrdenDeCompraRequestDTO;
import com.stocktrack.dto.OrdenDeCompraResponseDTO;
import com.stocktrack.services.OrdenDeCompraService;

@RestController
@RequestMapping("/ordenes-compra")
@CrossOrigin(origins = "*")
public class OrdenDeCompraController {
    
    @Autowired
    private OrdenDeCompraService ordenDeCompraService;

    @PostMapping
    public ResponseEntity<OrdenDeCompraResponseDTO> crearOrdenDeCompra(@RequestBody OrdenDeCompraRequestDTO requestDTO) {
        OrdenDeCompraResponseDTO response = ordenDeCompraService.crearOrdenDeCompra(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrdenDeCompraResponseDTO>> getAllOrdenesDeCompra() {
    List<OrdenDeCompraResponseDTO> ordenes = ordenDeCompraService.getAllOrdenesDeCompra();
    return ResponseEntity.ok(ordenes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrdenDeCompraResponseDTO> getOrdenDeCompraById(@PathVariable Long id) {
    OrdenDeCompraResponseDTO orden = ordenDeCompraService.getOrdenDeCompraById(id);
    return ResponseEntity.ok(orden);
    }
}
