package com.stocktrack.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class TransaccionSalidaRequestDTO {
    private String motivo; // "Venta", "Perdida", "Consumo"
    private BigDecimal precioDeVenta; // (Opcional, solo si es "Venta")
    private Long usuarioId; // (Opcional, para saber quién lo hizo)
    
    // Esta lista es la que nos dio la "sugerencia-fifo"   
    private List<DetalleSalidaRequestDTO> detalles;
}
