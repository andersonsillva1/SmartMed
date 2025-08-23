package br.com.smartmed.consultas.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UsuarioLoginRequestDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senha;

}
