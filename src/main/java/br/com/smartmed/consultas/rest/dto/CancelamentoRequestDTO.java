package br.com.smartmed.consultas.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CancelamentoRequestDTO {
    @NotNull(message = "O ID da consulta é obrigatório.")
    private Integer consultaID;

    @NotBlank(message = "O motivo do cancelamento é obrigatório.")
    private String motivo;

}
