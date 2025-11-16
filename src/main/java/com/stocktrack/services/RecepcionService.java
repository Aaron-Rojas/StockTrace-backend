package com.stocktrack.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocktrack.model.*; // Importa todos los modelos
import com.stocktrack.repository.*;

import com.stocktrack.dto.LoteStockRecepcionDTO;
import com.stocktrack.dto.RecepcionRequestDTO;
import com.stocktrack.dto.RecepcionResponseDTO;
import com.stocktrack.model.LoteStock;
import com.stocktrack.model.OrdenDeCompra;
import com.stocktrack.model.Producto;
import com.stocktrack.model.RecepcionInventario;
import com.stocktrack.model.Usuario;
import com.stocktrack.repository.LoteStockRepository;
import com.stocktrack.repository.OrdenDeCompraRepository;
import com.stocktrack.repository.ProductoRepository;
import com.stocktrack.repository.RecepcionInventarioRepository;
import com.stocktrack.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class RecepcionService {
    
    @Autowired
    private RecepcionInventarioRepository recepcionRepository;

    @Autowired
    private OrdenDeCompraRepository ocRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private LoteStockRepository loteStockRepository;

    @Transactional
    public RecepcionResponseDTO registrarRecepcion(RecepcionRequestDTO request) {
        
        // 1. Buscar entidades principales
        OrdenDeCompra oc = ocRepository.findById(request.getOrdenDeCompraId())
                .orElseThrow(() -> new RuntimeException("Orden de Compra no encontrada"));

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Crear la entidad de Recepción
        RecepcionInventario recepcion = new RecepcionInventario();
        recepcion.setOrdenDeCompra(oc);
        recepcion.setUsuario(usuario);
        
        // 3. ¡KPI! Registrar el Tiempo de Llegada (TL)
        recepcion.setFechaHoraLlegadaTl(LocalDateTime.now());
        
        // 4. Lógica de Discrepancia (RQF 3.2)
        // Comparamos lo pedido (en la OC) vs lo recibido (en el DTO)
        Map<Long, Integer> pedido = new HashMap<>();
        oc.getDetalles().forEach(detalle -> 
            pedido.put(detalle.getProducto().getId(), detalle.getCantidadSolicitada())
        );

        Map<Long, Integer> recibido = new HashMap<>();
        request.getLotes().forEach(lote -> 
            recibido.merge(lote.getProductoId(), lote.getCantidadRecibida(), Integer::sum)
        );

        boolean hayDiscrepancia = !pedido.equals(recibido);
        recepcion.setDiscrepancia(hayDiscrepancia);
        if (hayDiscrepancia) {
            recepcion.setDetalleDiscrepancia("Discrepancia detectada. IDProducto -> {ID, n°solicitado }: " + pedido + "|||| Recibido del IDProducto -> {ID, n°recibido}: " + recibido);
        }

        // 5. Guardar la recepción
        RecepcionInventario recepcionGuardada = recepcionRepository.save(recepcion);

        // 6. ¡Crucial! Crear los LoteStock
        for (LoteStockRecepcionDTO loteDTO : request.getLotes()) {
            Producto producto = productoRepository.findById(loteDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            LoteStock lote = new LoteStock();
            lote.setProducto(producto);
            lote.setProveedor(oc.getProveedor()); // El proveedor viene de la OC
            lote.setRecepcion(recepcionGuardada); // La recepción que acabamos de crear
            
            lote.setFechaCaducidad(loteDTO.getFechaCaducidad());
            lote.setNumeroLote(loteDTO.getNumeroLote());
            lote.setCantidadInicial(loteDTO.getCantidadRecibida());
            lote.setCantidadActual(loteDTO.getCantidadRecibida());
            
            // IMPORTANTE: El ContenedorID (ubicación) es NULO al recibir.
            // Se asignará en la Fase 4 (MovimientoLote).
            lote.setContenedor(null); 
            
            loteStockRepository.save(lote);
        }

        // 7. Actualizar estado de la Orden de Compra
        oc.setEstatus("Recibida");
        ocRepository.save(oc);

        // 8. Devolver la respuesta
        return new RecepcionResponseDTO(
            recepcionGuardada.getId(),
            oc.getId(),
            recepcionGuardada.getFechaHoraLlegadaTl(),
            usuario.getId(),
            hayDiscrepancia,
            recepcionGuardada.getDetalleDiscrepancia(),
            request.getLotes().size()
        );
    }


}
