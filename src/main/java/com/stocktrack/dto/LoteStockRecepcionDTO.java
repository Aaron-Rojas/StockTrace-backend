package com.stocktrack.dto;

import java.time.LocalDate;

import lombok.Data;


@Data
public class LoteStockRecepcionDTO {
    private Long productoId;
    private String numeroLote; // "LOTE-ABC-123"
    private LocalDate fechaCaducidad; // Clave para FIFO
    private Integer cantidadRecibida;
}
