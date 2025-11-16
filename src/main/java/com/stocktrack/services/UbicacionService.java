package com.stocktrack.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocktrack.dto.UbicacionDTO;
import com.stocktrack.model.Ubicacion;
import com.stocktrack.repository.UbicacionRepository;

@Service
public class UbicacionService {
    @Autowired
    private UbicacionRepository ubicacionRepository;  

    private UbicacionDTO convertToDTO(Ubicacion ubicacion) {
        UbicacionDTO dto = new UbicacionDTO();
        dto.setId(ubicacion.getId());
        dto.setNombre(ubicacion.getNombre());
        dto.setTipoUbicacion(ubicacion.getTipoUbicacion());
        return dto;
    } 

   private Ubicacion convertToEntity(UbicacionDTO dto) {
        Ubicacion ubicacion = new Ubicacion();
        // No seteamos el ID, es autogenerado
        ubicacion.setNombre(dto.getNombre());
        ubicacion.setTipoUbicacion(dto.getTipoUbicacion());
        return ubicacion;
    } 

    public UbicacionDTO createUbicacion(UbicacionDTO dto) {
        Ubicacion u = convertToEntity(dto);
        u = ubicacionRepository.save(u);
        return convertToDTO(u);
    }
    
    public List<UbicacionDTO> getAllUbicaciones() {
        return ubicacionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public UbicacionDTO getUbicacionById(Long id) {
        Ubicacion u = ubicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));
        return convertToDTO(u);
    }
    public UbicacionDTO updateUbicacion(Long id, UbicacionDTO dto) {
        Ubicacion u = ubicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));
        u.setNombre(dto.getNombre());
        u.setTipoUbicacion(dto.getTipoUbicacion());
        u = ubicacionRepository.save(u);
        return convertToDTO(u);
    }
    public void deleteUbicacion(Long id) {
        ubicacionRepository.deleteById(id);
    }

}
