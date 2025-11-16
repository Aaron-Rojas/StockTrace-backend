package com.stocktrack.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class RecepcionResponseDTO {
    private Long recepcionId;
    private Long ordenDeCompraId;
    private LocalDateTime fechaHoraLlegadaTl; // El KPI!
    private Long usuarioId;
    private Boolean discrepancia;
    private String detalleDiscrepancia;
    private Integer totalLotesCreados;   
}
