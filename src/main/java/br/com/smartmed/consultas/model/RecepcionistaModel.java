package br.com.smartmed.consultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recepcionista")
public class RecepcionistaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nome", length = 255, nullable = false)
    @NotNull(message = "O nome não pode ser nulo.")
    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @Column(name = "cpf", length = 11, nullable = false)
    @CPF(message = "CPF Inválido.")
    private String cpf;

    @Column(name = "dataNascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "dataAdmissao", nullable = false)
    private LocalDate dataAdmissao;

    @Column(name = "dataDemissao", nullable = false)
    private LocalDate dataDemissao;

    @Column(name = "telefone", length = 11, nullable = false)
    @Length(min = 11, max = 11, message = "Deve ter exatamente 11 digitos.")
    @NotNull(message = "O telefone não pode ser nulo.")
    @NotBlank(message = "O telefone é obrigatório.")
    private String telefone;

    @Column(name = "email", length = 64, nullable = false)
    @Email(message = "E-mail inválido")
    private String email;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

//    public RecepcionistaDTO toDTO(){
//        ModelMapper mapper = new ModelMapper();
//        return mapper.map(this,RecepcionistaDTO.class);
//    }

}