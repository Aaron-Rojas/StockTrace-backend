package com.stocktrack.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocktrack.dto.DetalleOCRequestDTO;
import com.stocktrack.dto.DetalleOCResponseDTO;
import com.stocktrack.dto.OrdenDeCompraRequestDTO;
import com.stocktrack.dto.OrdenDeCompraResponseDTO;


import com.stocktrack.model.DetalleOC;
import com.stocktrack.model.OrdenDeCompra;
import com.stocktrack.model.Producto;
import com.stocktrack.model.Proveedor;


import com.stocktrack.repository.DetalleOCRepository;
import com.stocktrack.repository.OrdenDeCompraRepository;
import com.stocktrack.repository.ProductoRepository;
import com.stocktrack.repository.ProveedorRepository;

import jakarta.transaction.Transactional;


@Service
public class OrdenDeCompraService {
    @Autowired
    private OrdenDeCompraRepository ordenDeCompraRepository;
    @Autowired
    private DetalleOCRepository detalleOCRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public OrdenDeCompraResponseDTO crearOrdenDeCompra(OrdenDeCompraRequestDTO requestDTO) {
        
        // 1. Buscar al Proveedor
        Proveedor proveedor = proveedorRepository.findById(requestDTO.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        // 2. Crear la Orden de Compra principal
        OrdenDeCompra oc = new OrdenDeCompra();
        oc.setProveedor(proveedor);
        oc.setEstatus("Solicitada");
        
        // 3. ¡KPI! Registrar el Tiempo de Solicitud (TS)
        oc.setFechaHoraSolicitudTs(LocalDateTime.now());
        
        OrdenDeCompra ocGuardada = ordenDeCompraRepository.save(oc);

        List<DetalleOCResponseDTO> detallesResponse = new ArrayList<>();

        // 4. Recorrer los detalles del request y crearlos
        for (DetalleOCRequestDTO detalleDTO : requestDTO.getDetalles()) {
            // Buscar el producto
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            // Crear la entidad DetalleOC
            DetalleOC detalle = new DetalleOC();
            detalle.setOrdenDeCompra(ocGuardada); // Vincular al padre
            detalle.setProducto(producto);
            detalle.setCantidadSolicitada(detalleDTO.getCantidadSolicitada());
            
            ocGuardada.getDetalles().add(detalle); // Agregar al conjunto de detalles

            detalleOCRepository.save(detalle); // Guardar el detalle

            // Preparar el DTO de respuesta para este detalle
            detallesResponse.add(convertToDetalleResponse(detalle));
        }

        // 5. Construir la respuesta final
        return convertToOrdenResponse(ocGuardada, detallesResponse);
    }

private OrdenDeCompraResponseDTO convertToOrdenResponse(OrdenDeCompra oc, List<DetalleOCResponseDTO> detalles) {
        OrdenDeCompraResponseDTO dto = new OrdenDeCompraResponseDTO();
        dto.setId(oc.getId());
        dto.setFechaHoraSolicitudTs(oc.getFechaHoraSolicitudTs());
        dto.setEstatus(oc.getEstatus());
        dto.setProveedorId(oc.getProveedor().getId());
        dto.setNombreProveedor(oc.getProveedor().getNombre());
        dto.setDetalles(detalles);
        return dto;
    }

    private DetalleOCResponseDTO convertToDetalleResponse(DetalleOC detalle) {
        DetalleOCResponseDTO dto = new DetalleOCResponseDTO();
        dto.setId(detalle.getId());
        dto.setProductoId(detalle.getProducto().getId());
        dto.setNombreProducto(detalle.getProducto().getNombre());
        dto.setCantidadSolicitada(detalle.getCantidadSolicitada());
        return dto;
    }

    public List<OrdenDeCompraResponseDTO> getAllOrdenesDeCompra() {
    // 1. Buscar todas las órdenes
    List<OrdenDeCompra> ordenes = ordenDeCompraRepository.findAll();

    // 2. Convertir cada una a su DTO de respuesta
    return ordenes.stream()
            .map(oc -> {
                // Por cada orden, convertimos sus detalles
                List<DetalleOCResponseDTO> detalles = new ArrayList<>(oc.getDetalles()).stream()
                        .map(this::convertToDetalleResponse)
                        .collect(Collectors.toList());
                // Construimos la respuesta completa para esta orden
                return convertToOrdenResponse(oc, detalles);
            })
            .collect(Collectors.toList());
    }   


    public OrdenDeCompraResponseDTO getOrdenDeCompraById(Long id) {
    // 1. Buscar la orden por ID
    OrdenDeCompra oc = ordenDeCompraRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Orden de Compra no encontrada"));

    // 2. Convertir sus detalles
    List<DetalleOCResponseDTO> detalles = new ArrayList<>(oc.getDetalles()).stream()
            .map(this::convertToDetalleResponse)
            .collect(Collectors.toList());

    // 3. Construir y devolver la respuesta
    return convertToOrdenResponse(oc, detalles);
    }
}
