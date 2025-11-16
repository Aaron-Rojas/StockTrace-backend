package com.stocktrack.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoResponseDTO {
    private Long movimientoId;
    private Long loteId;
    private Long contenedorId;
    private String nombreContenedor;
    private Long usuarioId;
    private LocalDateTime fechaHoraOrganizacionTo;
}
