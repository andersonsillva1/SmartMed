package br.com.smartmed.consultas.service;

import br.com.smartmed.consultas.exception.*;
import br.com.smartmed.consultas.model.FormaPagamentoModel;
import br.com.smartmed.consultas.repository.FormaPagamentoRepository;
import br.com.smartmed.consultas.rest.dto.FormaPagamentoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormaPagamentoService {
    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public FormaPagamentoDTO obterPorId(int id){
        FormaPagamentoModel formaPagamento = formaPagamentoRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Forma de Pagamento com ID " + id + " não encontrada."));
//        return formaPagamento.toDTO();
        return modelMapper.map(formaPagamento, FormaPagamentoDTO.class);
    }

    @Transactional(readOnly = true)
    public List<FormaPagamentoDTO> obterTodas(){
        List<FormaPagamentoModel> formaPagamentos = formaPagamentoRepository.findAll();
        return formaPagamentos.stream().map(formaPagamento -> modelMapper.map(formaPagamento, FormaPagamentoDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public FormaPagamentoDTO salvar (FormaPagamentoModel novaFormaPagamento){
        try {
            if (formaPagamentoRepository.existsByDescricao(novaFormaPagamento.getDescricao())){
                throw new ConstraintException("Já existe uma Forma de Pagamento com essa Descricao " + novaFormaPagamento.getDescricao() + " na base de dados.");
            }
//            return formaPagamentoRepository.save(novaFormaPagamento).toDTO();
            return modelMapper.map(formaPagamentoRepository.save(novaFormaPagamento), FormaPagamentoDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível salvar a Forma de Pagamento " + novaFormaPagamento.getDescricao() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro de restrição de integridade ao salvar a Forma de Pagamento " + novaFormaPagamento.getDescricao() + ".");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível salvar a Forma de Pagamento " + novaFormaPagamento.getDescricao() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível salvar a Forma de Pagamento " + novaFormaPagamento.getDescricao() + ". Falha na conexão com o banco de dados!");
        }
    }

    @Transactional
    public FormaPagamentoDTO atualizar (FormaPagamentoModel FormaPagamentoExistente){
        try {
            if (!formaPagamentoRepository.existsById(FormaPagamentoExistente.getId())){
                throw new ConstraintException("Não existe uma Forma de Pagamento com esse ID " + FormaPagamentoExistente.getId() + " na base de dados.");
            }
//            return formaPagamentoRepository.save(FormaPagamentoExistente).toDTO();
            return modelMapper.map(formaPagamentoRepository.save(FormaPagamentoExistente), FormaPagamentoDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível salvar a Forma de Pagamento " + FormaPagamentoExistente.getId() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro de restrição de integridade ao salvar a Forma de Pagamento " + FormaPagamentoExistente.getId() + ".");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível salvar a Forma de Pagamento " + FormaPagamentoExistente.getDescricao() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível salvar a Forma de Pagamento " + FormaPagamentoExistente.getDescricao() + ". Falha na conexão com o banco de dados!");
        }
    }

    @Transactional
    public void deletar(FormaPagamentoModel formaPagamentoExistente) {

        try {

            if (!formaPagamentoRepository.existsById(formaPagamentoExistente.getId())) {
                throw new ConstraintException("Forma de Pagamento inexistente na base de dados!");
            }

            formaPagamentoRepository.delete(formaPagamentoExistente);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível deletar a Forma de Pagamento " + formaPagamentoExistente.getId() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao deletar a Forma de Pagamento " + formaPagamentoExistente.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível deletar a Forma de Pagamento " + formaPagamentoExistente.getId() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível atualizar o deletar " + formaPagamentoExistente.getId() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível deletar a Forma de Pagamento " + formaPagamentoExistente.getId() + ". Não encontrado no banco de dados!");
        }
    }
}
