package com.stocktrack.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocktrack.dto.UbicacionDTO;
import com.stocktrack.services.UbicacionService;


@RestController
@RequestMapping("/ubicaciones")
@CrossOrigin(origins = "*")
public class UbicacionController {
    @Autowired
    private UbicacionService ubicacionService;
    
    @PostMapping
    public ResponseEntity<UbicacionDTO> createUbicacion(@RequestBody UbicacionDTO dto) {
        return new ResponseEntity<>(ubicacionService.createUbicacion(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UbicacionDTO>> getAllUbicaciones() {
        return ResponseEntity.ok(ubicacionService.getAllUbicaciones());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDTO> getUbicacionById(@PathVariable Long id) {
        return ResponseEntity.ok(ubicacionService.getUbicacionById(id));
    } 

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUbicacion(@PathVariable Long id) {
        ubicacionService.deleteUbicacion(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UbicacionDTO> updateUbicacion(@PathVariable Long id
            , @RequestBody UbicacionDTO dto) {
        return ResponseEntity.ok(ubicacionService.updateUbicacion(id, dto));
    }
    
}
