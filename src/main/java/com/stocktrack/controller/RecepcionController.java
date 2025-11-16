package com.stocktrack.controller;


import com.stocktrack.dto.RecepcionRequestDTO;
import com.stocktrack.dto.RecepcionResponseDTO;
import com.stocktrack.services.RecepcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recepciones")
@CrossOrigin(origins = "*")
public class RecepcionController {
    
    @Autowired
    private RecepcionService recepcionService;

    @PostMapping
    public ResponseEntity<RecepcionResponseDTO> registrarRecepcion(@RequestBody RecepcionRequestDTO request) {
        RecepcionResponseDTO response = recepcionService.registrarRecepcion(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
