package br.com.smartmed.consultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "formaPagamento")
public class FormaPagamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "descricao", length = 64, nullable = false)
    @NotNull
    @NotBlank
    private String descricao;

//    public FormaPagamentoDTO toDTO(){
//        ModelMapper mapper = new ModelMapper();
//        return mapper.map(this,FormaPagamentoDTO.class);
//    }


}