package br.com.smartmed.consultas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDTO<T> {
    private List<T> conteudo;
    private int totalPaginas;
    private long totalElementos;
    private int paginaAtual;
    private int tamanhoPagina;

}
