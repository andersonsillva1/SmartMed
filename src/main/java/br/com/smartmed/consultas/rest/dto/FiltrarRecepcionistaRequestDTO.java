package br.com.smartmed.consultas.rest.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class FiltrarRecepcionistaRequestDTO {

    private String status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataInicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataFim;

    @Min(value = 0, message = "O número da página não pode ser negativo.")
    private Integer pagina = 0;

    @Min(value = 1, message = "O tamanho da página deve ser pelo menos 1.")
    private Integer tamanhoPagina = 10;

}
