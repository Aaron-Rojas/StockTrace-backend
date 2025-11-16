package com.stocktrack.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocktrack.model.Contenedor;
import com.stocktrack.model.LoteStock;
import com.stocktrack.model.MovimientoLote;
import com.stocktrack.model.MovimientoRequestDTO;
import com.stocktrack.model.MovimientoResponseDTO;
import com.stocktrack.model.Usuario;
import com.stocktrack.repository.ContenedorRepository;
import com.stocktrack.repository.LoteStockRepository;
import com.stocktrack.repository.MovimientoLoteRepository;
import com.stocktrack.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class MovimientoLoteService {
    @Autowired
    private LoteStockRepository loteStockRepository;
    @Autowired
    private ContenedorRepository contenedorRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private MovimientoLoteRepository movimientoLoteRepository;


    @Transactional
    public MovimientoResponseDTO ubicarLote(MovimientoRequestDTO request) {    
        // 1. Buscar las entidades
        LoteStock lote = loteStockRepository.findById(request.getLoteId())
                .orElseThrow(() -> new RuntimeException("LoteStock no encontrado"));
        
        Contenedor contenedor = contenedorRepository.findById(request.getContenedorId())
                .orElseThrow(() -> new RuntimeException("Contenedor no encontrado"));
        
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Actualizar el LoteStock (asignarle su ubicación)
        lote.setContenedor(contenedor);
        loteStockRepository.save(lote);

        // 3. Crear el registro del Movimiento (para el KPI TO)
        MovimientoLote movimiento = new MovimientoLote();
        movimiento.setLoteStock(lote);
        movimiento.setUsuario(usuario);
        
        // 4. ¡KPI! Registrar el Tiempo de Organización (TO)
        movimiento.setFechaHoraOrganizacionTo(LocalDateTime.now());
        
        MovimientoLote movimientoGuardado = movimientoLoteRepository.save(movimiento);

        // 5. Devolver la respuesta
        return new MovimientoResponseDTO(
            movimientoGuardado.getId(),
            lote.getId(),
            contenedor.getId(),
            contenedor.getNombre(),
            usuario.getId(),
            movimientoGuardado.getFechaHoraOrganizacionTo()
        );
    }
}
