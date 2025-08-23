package br.com.smartmed.consultas.rest.dto;

import br.com.smartmed.consultas.model.PerfilModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioInfoDTO {

    private int id;
    private String nome;
    private PerfilModel perfil;
}
