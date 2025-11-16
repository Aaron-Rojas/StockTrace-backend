package com.stocktrack.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
@Data
public class OrdenDeCompraResponseDTO {
    private Long id;
    private LocalDateTime fechaHoraSolicitudTs;
    private String estatus;
    private Long proveedorId;
    private String nombreProveedor; // Útil para el frontend
    private List<DetalleOCResponseDTO> detalles;
}
