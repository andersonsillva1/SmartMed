package br.com.smartmed.consultas.service;

import br.com.smartmed.consultas.exception.*;
import br.com.smartmed.consultas.model.RecepcionistaModel;
import br.com.smartmed.consultas.repository.RecepcionistaRepository;
import br.com.smartmed.consultas.rest.dto.RecepcionistaDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecepcionistaService {
    @Autowired
    private RecepcionistaRepository recepcionistaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public RecepcionistaDTO obterPorId(int id){
        RecepcionistaModel recepcionista = recepcionistaRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Recepcionista com o ID " + id + " não foi encontrada"));
//        return recepcionista.toDTO();
        return modelMapper.map(recepcionista, RecepcionistaDTO.class);
    }
    @Transactional(readOnly = true)
    public RecepcionistaDTO obterPorCpf(String cpf){
        RecepcionistaModel recepcionista = recepcionistaRepository.findByCpf(cpf).orElseThrow(() -> new ObjectNotFoundException("Recepcionista com o CPF " + cpf + " não foi encontrada"));
        return modelMapper.map(recepcionista, RecepcionistaDTO.class);

    }
    @Transactional(readOnly = true)
    public List<RecepcionistaDTO> obterTodos(){
        List<RecepcionistaModel> recepcionistas = recepcionistaRepository.findAll();
        return recepcionistas.stream().map(recepcionista -> modelMapper.map(recepcionista, RecepcionistaDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public RecepcionistaDTO salvar (RecepcionistaModel novaRecepcionista){

        try {
            if (recepcionistaRepository.existsByCpf(novaRecepcionista.getCpf())){
                throw new ConstraintException("Já existe uma Recepcionista com esse CPF " + novaRecepcionista.getCpf() + " na base de dados.");

            }
            return modelMapper.map(recepcionistaRepository.save(novaRecepcionista), RecepcionistaDTO.class);
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível salvar a recepcionista " + novaRecepcionista.getNome() + " !");
        } catch (ConstraintException e){
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro de restrição de integridade ao salvar a recepcionista " + novaRecepcionista.getNome() + ".");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível salvar a recepcionista " + novaRecepcionista.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível salvar a recepcionista " + novaRecepcionista.getNome() + ". Falha na conexão com o banco de dados!");
        }
    }

    @Transactional
    public RecepcionistaDTO atualizar(RecepcionistaModel recepcionistaExistente) {

        try {
            if (!recepcionistaRepository.existsByCpf(recepcionistaExistente.getCpf())) {
                throw new ConstraintException("a recepcionista com esse CPF " + recepcionistaExistente.getCpf() + " não existe na base de dados!");
            }

            return modelMapper.map(recepcionistaRepository.save(recepcionistaExistente), RecepcionistaDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível atualizar a recepcionista " + recepcionistaExistente.getNome() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao atualizar a recepcionista " + recepcionistaExistente.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível atualizar a recepcionista " + recepcionistaExistente.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível atualizar a recepcionista " + recepcionistaExistente.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível atualizar a recepcionista " + recepcionistaExistente.getNome() + ". Não encontrado no banco de dados!");
        }
    }

    @Transactional
    public void deletar(RecepcionistaModel recepcionistaExistente) {

        try {

            if (!recepcionistaRepository.existsById(recepcionistaExistente.getId())) {
                throw new ConstraintException("Recepcionista inexistente na base de dados!");
            }

            recepcionistaRepository.delete(recepcionistaExistente);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível deletar a recepcionista " + recepcionistaExistente.getNome() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao deletar a recepcionista " + recepcionistaExistente.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível deletar a recepcionista " + recepcionistaExistente.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível atualizar o deletar " + recepcionistaExistente.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível deletar a recepcionista" + recepcionistaExistente.getNome() + ". Não encontrado no banco de dados!");
        }
    }

}