package com.stocktrack.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrdenDeCompraRequestDTO {
    private Long proveedorId;
    private List<DetalleOCRequestDTO> detalles;
}
