package br.com.smartmed.consultas.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReagendamentoRequestDTO {

    @NotNull (message = "O Id é obrigatorio")
    @NotBlank
    private Integer consultaID;

    @NotBlank
    @NotNull (message = "A nova data é obrigatoria!")
    private LocalDateTime novaDataHora;

    @NotBlank
    @NotNull (message = "O motivo do reagendamento é obrigatório.")
    private String motivo;
}
