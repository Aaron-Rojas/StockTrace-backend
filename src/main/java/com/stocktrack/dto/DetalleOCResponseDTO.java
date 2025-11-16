package com.stocktrack.dto;
import lombok.Data;

@Data
public class DetalleOCResponseDTO {
    private Long id;
    private Long productoId;
    private String nombreProducto; // Útil para el frontend
    private Integer cantidadSolicitada;  
}
