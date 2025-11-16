package com.stocktrack.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocktrack.dto.ProveedorDTO;
import com.stocktrack.model.Proveedor;
import com.stocktrack.repository.ProveedorRepository;

@Service
public class ProveedorService {
    
    @Autowired
    private ProveedorRepository proveedorRepository;
    
    private ProveedorDTO convertToDTO(Proveedor proveedor) {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setId(proveedor.getId());
        dto.setNombre(proveedor.getNombre());
        dto.setRuc(proveedor.getRuc());
        dto.setTelefono(proveedor.getTelefono());
        dto.setEmail(proveedor.getEmail());
        return dto;
    }

    private Proveedor convertToEntity(ProveedorDTO dto) {
        Proveedor proveedor = new Proveedor();
        // No seteamos el ID, es autogenerado
        proveedor.setNombre(dto.getNombre());
        proveedor.setRuc(dto.getRuc());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        return proveedor;
    }

    public ProveedorDTO createProveedor(ProveedorDTO dto) {
        Proveedor p = convertToEntity(dto);
        p = proveedorRepository.save(p);
        return convertToDTO(p);
    }

    public List<ProveedorDTO> getAllProveedores() {
        return proveedorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProveedorDTO getProveedorById(Long id) {
        Proveedor p = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        return convertToDTO(p);
    }

    public ProveedorDTO updateProveedor(Long id, ProveedorDTO dto) {
        Proveedor p = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        // Actualizar campos
        p.setNombre(dto.getNombre());
        p.setRuc(dto.getRuc());
        p.setTelefono(dto.getTelefono());
        p.setEmail(dto.getEmail());

        p = proveedorRepository.save(p);
        return convertToDTO(p);
    }

    public void deleteProveedor(Long id) {
        if (!proveedorRepository.existsById(id)) {
            throw new RuntimeException("Proveedor no encontrado");
        }
        proveedorRepository.deleteById(id);
    }
}
