package com.stocktrack.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocktrack.dto.ContenedorDTO;
import com.stocktrack.model.Contenedor;
import com.stocktrack.repository.ContenedorRepository;

import com.stocktrack.model.Ubicacion;
import com.stocktrack.repository.UbicacionRepository;

@Service
public class ContenedorService {
    @Autowired
    private ContenedorRepository contenedorRepository;
    
    @Autowired
    private UbicacionRepository ubicacionRepository;

    private ContenedorDTO convertToDTO(Contenedor contenedor) {
        ContenedorDTO dto = new ContenedorDTO();
        dto.setId(contenedor.getId());
        dto.setCodigoQrId(contenedor.getCodigoQrId());
        dto.setNombre(contenedor.getNombre());
        dto.setEstatus(contenedor.getEstatus());
        // Nos aseguramos que la Ubicacion no sea nula antes de llamar a getId()
        if (contenedor.getUbicacion() != null) {
            dto.setUbicacionId(contenedor.getUbicacion().getId());
        }
        return dto;
    }

    private Contenedor convertToEntity(ContenedorDTO dto) {
        Contenedor contenedor = new Contenedor();

        contenedor.setCodigoQrId(dto.getCodigoQrId());
        contenedor.setNombre(dto.getNombre());  
        contenedor.setEstatus(dto.getEstatus());
        // La asignación de la ubicación se manejará en otro servicio o capa
        return contenedor;

    }

    public ContenedorDTO createContenedor(ContenedorDTO dto) {
        // Buscamos la entidad Ubicacion
        Ubicacion ubicacion = ubicacionRepository.findById(dto.getUbicacionId())
                .orElseThrow(() -> new RuntimeException("Ubicacion no encontrada"));

        Contenedor c = convertToEntity(dto);
        
        // Adjuntamos la entidad Ubicacion completa
        c.setUbicacion(ubicacion); 
        
        c = contenedorRepository.save(c);
        return convertToDTO(c);
    }

    public ContenedorDTO getContenedorById(Long id) {
        Contenedor c = contenedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contenedor no encontrado"));
        return convertToDTO(c);
    }
    public List<ContenedorDTO> getAllContenedores() {
        return contenedorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public ContenedorDTO updateContenedor(Long id, ContenedorDTO dto) {
        Contenedor c = contenedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contenedor no encontrado"));
        
        c.setCodigoQrId(dto.getCodigoQrId());
        c.setNombre(dto.getNombre());
        c.setEstatus(dto.getEstatus());

        // Verificamos si la ubicación cambió
        if (dto.getUbicacionId() != null && !dto.getUbicacionId().equals(c.getUbicacion().getId())) {
            Ubicacion nuevaUbicacion = ubicacionRepository.findById(dto.getUbicacionId())
                    .orElseThrow(() -> new RuntimeException("Ubicacion no encontrada"));
            c.setUbicacion(nuevaUbicacion);
        }
        
        c = contenedorRepository.save(c);
        return convertToDTO(c);
    }

    public void deleteContenedor(Long id) {
        if(!contenedorRepository.existsById(id)) {
            throw new RuntimeException("Contenedor no encontrado");
        }
        contenedorRepository.deleteById(id);
    }
}
