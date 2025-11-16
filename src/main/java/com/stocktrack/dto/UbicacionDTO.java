package com.stocktrack.dto;

import lombok.Data;

@Data
public class UbicacionDTO {
    private Long id;
    private String nombre; // Ej: "Almacén Principal", "Zona Fría"
    private String tipoUbicacion; // Ej: "MainAlmacen", "Recepcion"
}
