package br.com.smartmed.consultas.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CadastroConsultaRequestDTO {

    @NotNull(message = "A data e a hora da consulta é obrigatória.")
    private LocalDateTime dataHora;

    @NotNull(message = "A duração da consulta é obrigatória.")
    @Positive(message = "A duração da consulta deve ser um valor positivo.")
    private Integer duracaoMinutos;

    @NotNull(message = "O ID do paciente é obrigatório.")
    private Integer pacienteID;

    @NotNull(message = "O ID do médico é obrigatório.")
    private Integer medicoID;

    private Integer convenioID;

    @NotNull(message = "O ID da forma de pagamento é obrigatório.")
    private Integer formaPagamentoID;

    @NotNull(message = "O ID do recepcionista é obrigatório.")
    private Integer recepcionistaID;
    private long duracaoMinutes;

    public long getDuracaoMinutes() {
        return duracaoMinutes;
    }

    public void setDuracaoMinutes(long duracaoMinutes) {
        this.duracaoMinutes = duracaoMinutes;
    }
}
