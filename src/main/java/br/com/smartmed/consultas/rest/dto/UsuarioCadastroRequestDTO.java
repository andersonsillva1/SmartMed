package br.com.smartmed.consultas.rest.dto;

import br.com.smartmed.consultas.model.PerfilModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioCadastroRequestDTO {
    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 8, message = "A senha deve ter no minimo 8 caracteres.")
    private String senha;

    @NotNull(message = "O perfil é obrigatório.")
    private PerfilModel perfil;
}
