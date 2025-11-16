package com.stocktrack.dto;

import lombok.Data;

@Data
public class ProveedorDTO {
    private Long id;
    private String nombre;
    private String ruc;
    private String telefono;
    private String email;
}
