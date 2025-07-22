package br.com.smartmed.consultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consulta")
public class ConsultaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "dataHoraConsulta", nullable = false)
    private LocalDateTime dataHoraConsulta;

    @Column(name = "status", length = 16, nullable = false)
    @NotNull(message = "O status não pode ser Nulo.")
    @NotBlank(message = "O status é obrigatório.")
    private String status;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "observacoes", length = 1024, nullable = true)
    private String observacoes;

    @Column(name = "pacienteID", nullable = false)
    private int pacienteID;

    @Column(name = "medicoID", nullable = false)
    private int medicoID;

    @Column(name = "formaPagamentoID", nullable = false)
    private int formaPagamentoID;

    @Column(name = "convenioID", nullable = false)
    private int convenioID;

    @Column(name = "recepcionistaID", nullable = false)
    private int recepcionistaID;

//    public ConsultaDTO toDTO(){
//        ModelMapper mapper = new ModelMapper();
//        return mapper.map(this,ConsultaDTO.class);
//    }

}