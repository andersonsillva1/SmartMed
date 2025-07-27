package br.com.smartmed.consultas.rest.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AgendaMedicoResponseDTO {
    private String medico;
    private LocalDate data;
    private List<String> horariosOcupados;
    private List<String> horariosDisponiveis;
}
