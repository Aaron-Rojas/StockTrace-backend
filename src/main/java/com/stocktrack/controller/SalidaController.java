package com.stocktrack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stocktrack.dto.SugerenciaLoteDTO;
import com.stocktrack.dto.TransaccionSalidaRequestDTO;
import com.stocktrack.services.SalidaService;

@RestController
@RequestMapping("/salidas")
@CrossOrigin(origins = "*")
public class SalidaController {
    @Autowired
    private SalidaService salidaService;

    @GetMapping("/sugerencia-fifo")
    public ResponseEntity<List<SugerenciaLoteDTO>> getSugerencia(
            @RequestParam Long productoId,
            @RequestParam Integer cantidad) {
        
        List<SugerenciaLoteDTO> sugerencia = salidaService.getSugerenciaFifo(productoId, cantidad);
        return ResponseEntity.ok(sugerencia);
    }

    @PostMapping("/confirmar")
    public ResponseEntity<Long> confirmarSalida(@RequestBody TransaccionSalidaRequestDTO request) {
        Long transaccionId = salidaService.confirmarSalida(request);
        return new ResponseEntity<>(transaccionId, HttpStatus.CREATED);
    }
}
