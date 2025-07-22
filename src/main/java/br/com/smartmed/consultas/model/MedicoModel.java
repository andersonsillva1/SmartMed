package br.com.smartmed.consultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medico")
public class MedicoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nome", length = 255, nullable = false)
    @NotNull(message = "O nome não pode ser nulo.")
    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @Column(name = "crm", length = 8, nullable = false)
    @NotNull(message = "O CRM não pode ser nulo.")
    @NotBlank(message = "O CRM é obrigatório.")
    private String crm;

    @Column(name = "telefone", length = 11, nullable = false)
    @Length(min = 11, max = 11, message = "O campo deve ter exatamente 11 números. Ex: 79981130366")
    @NotNull(message = "O telefone não pode ser nulo.")
    @NotBlank(message = "O telefone é obrigatório.")
    private String telefone;

    @Column(name = "email", length = 64, nullable = true)
    @Email(message = "E-mail inválido.")
    private String email;

    @Column(name = "valorConsultaReferencia", nullable = false)
    private float valorConsultaReferencia;

    @Column(name = "ativo", nullable = false)
    private boolean ativo;

    @Column(name = "especialidadeID", nullable = false)
    private int especialidadeID;

//    public MedicoDTO toDTO(){
//        ModelMapper mapper = new ModelMapper();
//        return mapper.map(this,MedicoDTO.class);
//    }

}