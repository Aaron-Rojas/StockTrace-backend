package com.stocktrack.dto;

import java.util.List;

import lombok.Data;

@Data
public class RecepcionRequestDTO {
    
    private Long ordenDeCompraId;
    private Long usuarioId; // Quién está recibiendo
    private List<LoteStockRecepcionDTO> lotes;   
}
