package com.stocktrack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocktrack.dto.ContenedorDTO;
import com.stocktrack.services.ContenedorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/contenedores")
@CrossOrigin(origins = "*")
public class ContenedorController {
    @Autowired
    private ContenedorService contenedorService;

    @PostMapping
    public ResponseEntity<ContenedorDTO> createContenedor(@RequestBody ContenedorDTO dto) {
        return new ResponseEntity <>(contenedorService.createContenedor(dto), HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<ContenedorDTO>> getAllContenedores() {
        return ResponseEntity.ok(contenedorService.getAllContenedores());
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<ContenedorDTO> getContenedorById(@PathVariable Long id) {
        return ResponseEntity.ok(contenedorService.getContenedorById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContenedor(@PathVariable Long id) {
        contenedorService.deleteContenedor(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContenedorDTO> updateContenedor (@PathVariable Long id, 
                                                        @RequestBody ContenedorDTO dto){
        return ResponseEntity.ok(contenedorService.updateContenedor(id, dto));
    }   
}
