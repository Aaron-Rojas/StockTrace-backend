package com.stocktrack.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String nombreCompleto;
    private String email;
    private String password;
    // Omitimos el password en la respuesta por seguridad (se mapea manualmente en
    // el servicio y no se incluye)
    private String rol;

}
