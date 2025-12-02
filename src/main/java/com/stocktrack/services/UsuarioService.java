package com.stocktrack.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocktrack.dto.UsuarioDTO;
import com.stocktrack.model.Usuario;
import com.stocktrack.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    private UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        return dto;
    }

    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO getUsuarioById(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertToDTO(u);
    }

    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(usuarioDTO.getNombreCompleto());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setRol(usuarioDTO.getRol());

        // Encriptar contraseña
        String encryptedPassword = passwordEncoder.encode(usuarioDTO.getPassword());
        usuario.setPassword(encryptedPassword);

        System.out.println("VERIFICACION ENCRIPTACION: Password original: " + usuarioDTO.getPassword());
        System.out.println("VERIFICACION ENCRIPTACION: Password encriptado: " + encryptedPassword);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return convertToDTO(usuarioGuardado);
    }

    public UsuarioDTO login(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return convertToDTO(usuario);
    }

}
