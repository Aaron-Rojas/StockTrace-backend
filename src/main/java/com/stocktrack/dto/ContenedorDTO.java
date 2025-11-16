package com.stocktrack.dto;

import lombok.Data;

@Data
public class ContenedorDTO {
    private Long id;
    private String codigoQrId;
    private String nombre; // Ej: "Estante A-1-C4"
    private String estatus; // Ej: "Vacio", "Ocupado"
    
    private Long ubicacionId; // ID de la Ubicación a la que pertenece el contenedor

}
