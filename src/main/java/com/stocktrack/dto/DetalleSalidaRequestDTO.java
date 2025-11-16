package com.stocktrack.dto;

import lombok.Data;

@Data
public class DetalleSalidaRequestDTO {
    private Long loteId;
    private Integer cantidadRetirada;
}
