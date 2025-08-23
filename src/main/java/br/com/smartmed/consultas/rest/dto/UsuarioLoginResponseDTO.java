package br.com.smartmed.consultas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioLoginResponseDTO {

    private String mensagem;
    private UsuarioInfoDTO usuario;
    private String token;
}
