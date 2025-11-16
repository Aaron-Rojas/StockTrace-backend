package com.stocktrack.model;

import lombok.Data;

@Data
public class MovimientoRequestDTO {
    private Long loteId;
    private Long contenedorId;
    private Long usuarioId;
}
