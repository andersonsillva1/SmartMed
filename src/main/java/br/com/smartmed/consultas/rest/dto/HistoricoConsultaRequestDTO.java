package br.com.smartmed.consultas.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class HistoricoConsultaRequestDTO {

    @NotNull(message = "O ID do paciente é obrigatório.")
    private Integer pacienteID;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataInicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataFim;

    private Integer medicoID;

    private String status;

    private Integer especialidadeID;

}
