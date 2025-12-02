package com.stocktrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocktrack.dto.LoginRequestDTO;
import com.stocktrack.dto.UsuarioDTO;
import com.stocktrack.services.UsuarioService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        UsuarioDTO usuario = usuarioService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(usuario);
    }
}
