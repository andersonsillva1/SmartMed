package br.com.smartmed.consultas.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AgendamentoInteligenteRequestDTO {
    @NotNull(message = "O ID do paciente é obrigatório.")
    private Integer pacienteID;

    private Integer medicoID;

    private Integer especialidadeID;

    @NotNull(message = "A data e hora inicial para busca é obrigatória.")
    private LocalDateTime dataHoraInicial;

    @NotNull(message = "A duração da consulta é obrigatória.")
    private Integer duracaoConsultaMinutos;

    private Integer convenioID;

    @NotNull(message = "A forma de pagamento é obrigatória.")
    private Integer formaPagamentoID;
}