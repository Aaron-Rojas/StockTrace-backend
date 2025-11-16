package com.stocktrack.dto;

import lombok.Data;

@Data
public class DetalleOCRequestDTO {
    private Long productoId;
    private Integer cantidadSolicitada;
}
