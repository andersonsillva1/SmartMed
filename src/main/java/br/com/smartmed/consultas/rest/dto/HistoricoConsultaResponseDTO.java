package br.com.smartmed.consultas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoConsultaResponseDTO {

    private LocalDateTime dataHora;
    private String medico;
    private String especialidade;
    private Double valor;
    private String status;
    private String observacoes;

}
