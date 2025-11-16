package com.stocktrack.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocktrack.dto.DetalleSalidaRequestDTO;
import com.stocktrack.dto.SugerenciaLoteDTO;
import com.stocktrack.dto.TransaccionSalidaRequestDTO;
import com.stocktrack.model.DetalleSalida;
import com.stocktrack.model.LoteStock;
import com.stocktrack.model.TransaccionSalida;
import com.stocktrack.repository.DetalleSalidaRepository;
import com.stocktrack.repository.LoteStockRepository;
import com.stocktrack.repository.TransaccionSalidaRepository;

import jakarta.transaction.Transactional;


@Service
public class SalidaService {
   @Autowired
    private LoteStockRepository loteStockRepository;

    @Autowired
    private TransaccionSalidaRepository transaccionSalidaRepository;

    @Autowired
    private DetalleSalidaRepository detalleSalidaRepository;

    /**
     * Implementa la lógica FIFO (Primero en Caducar, Primero en Salir).
     * Devuelve la lista exacta de lotes y cantidades a retirar.
     */
    public List<SugerenciaLoteDTO> getSugerenciaFifo(Long productoId, Integer cantidadRequerida) {
        
        // 1. (RQF 4.1) Buscar todos los lotes de ese producto con stock,
        //    ordenados por la fecha de caducidad más próxima.
        //    (Este método ya lo definimos en LoteStockRepository)
        List<LoteStock> lotesDisponibles = loteStockRepository
                .findByProductoIdAndCantidadActualGreaterThanOrderByFechaCaducidadAsc(productoId, 0);

        List<SugerenciaLoteDTO> sugerencia = new ArrayList<>();
        Integer cantidadFaltante = cantidadRequerida;

        // 2. Lógica de "Cascada"
        for (LoteStock lote : lotesDisponibles) {
            if (cantidadFaltante <= 0) {
                break; // Ya completamos el pedido
            }

            // ¿Cuánto podemos tomar de este lote?
            // Tomamos el mínimo entre lo que falta y lo que el lote tiene.
            Integer cantidadARetirarDeEsteLote = Math.min(lote.getCantidadActual(), cantidadFaltante);

            // 3. Añadir a la sugerencia
            sugerencia.add(new SugerenciaLoteDTO(
                    lote.getId(),
                    lote.getNumeroLote(),
                    lote.getFechaCaducidad(),
                    lote.getCantidadActual(),
                    cantidadARetirarDeEsteLote
            ));

            // 4. Actualizar lo que nos falta
            cantidadFaltante -= cantidadARetirarDeEsteLote;
        }

        // 5. (Opcional) Advertencia si el stock no fue suficiente
        if (cantidadFaltante > 0) {
            // No podemos lanzar una excepción, porque el operario igual puede
            // querer llevarse lo que sí hay. El frontend manejará esta info.
            // Por ahora, solo devolvemos lo que pudimos cubrir.
        }

        return sugerencia;
    } 

    
    @Transactional
    public Long confirmarSalida(TransaccionSalidaRequestDTO request) {
        
        // 1. Crear la transacción "padre" (la factura)
        TransaccionSalida transaccion = new TransaccionSalida();
        transaccion.setMotivo(request.getMotivo());
        transaccion.setFechaHoraSalida(LocalDateTime.now());
        transaccion.setPrecioDeVenta(request.getPrecioDeVenta());
        // (Omitimos el usuario por simpleza, pero se podría añadir)

        TransaccionSalida transaccionGuardada = transaccionSalidaRepository.save(transaccion);

        // 2. Recorrer los detalles (los lotes a descontar)
        for (DetalleSalidaRequestDTO detalleDTO : request.getDetalles()) {
            
            // 3. Buscar el LoteStock
            LoteStock lote = loteStockRepository.findById(detalleDTO.getLoteId())
                    .orElseThrow(() -> new RuntimeException("LoteStock no encontrado: " + detalleDTO.getLoteId()));

            // 4. Validar que tengamos stock suficiente
            if (lote.getCantidadActual() < detalleDTO.getCantidadRetirada()) {
                // ¡El stock cambió! Alguien más lo tomó.
                // Anulamos toda la transacción.
                throw new RuntimeException("Stock insuficiente en el lote: " + lote.getId());
            }

            // 5. ¡Crucial! Descontar el stock
            int nuevaCantidad = lote.getCantidadActual() - detalleDTO.getCantidadRetirada();
            lote.setCantidadActual(nuevaCantidad);
            loteStockRepository.save(lote);

            // 6. Crear el registro "hijo" (DetalleSalida)
            DetalleSalida detalleSalida = new DetalleSalida();
            detalleSalida.setTransaccionSalida(transaccionGuardada);
            detalleSalida.setLoteStock(lote);
            detalleSalida.setCantidadRetirada(detalleDTO.getCantidadRetirada());
            detalleSalidaRepository.save(detalleSalida);
        }

        return transaccionGuardada.getId(); // Devolvemos el ID de la transacción
    }
}
