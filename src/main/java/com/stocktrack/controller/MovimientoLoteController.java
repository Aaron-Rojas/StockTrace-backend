package com.stocktrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocktrack.model.MovimientoRequestDTO;
import com.stocktrack.model.MovimientoResponseDTO;
import com.stocktrack.services.MovimientoLoteService;

@RestController
@RequestMapping("/movimientos")
@CrossOrigin(origins = "*")
public class MovimientoLoteController {
    @Autowired
    private MovimientoLoteService movimientoLoteService;

    // Usamos /ubicar para ser específicos sobre la acción
    @PostMapping("/ubicar")
    public ResponseEntity<MovimientoResponseDTO> ubicarLote(@RequestBody MovimientoRequestDTO request) {
        MovimientoResponseDTO response = movimientoLoteService.ubicarLote(request);
        return ResponseEntity.ok(response);
    }
}
