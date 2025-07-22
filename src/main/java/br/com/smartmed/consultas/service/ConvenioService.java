
package br.com.smartmed.consultas.service;

import br.com.smartmed.consultas.exception.*;
import org.modelmapper.ModelMapper;
import br.com.smartmed.consultas.model.ConvenioModel;
import br.com.smartmed.consultas.repository.ConvenioRepository;
import br.com.smartmed.consultas.rest.dto.ConvenioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConvenioService {
    @Autowired
    private ConvenioRepository convenioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public ConvenioDTO obterPorCnpj(String cnpj){
        ConvenioModel convenio = convenioRepository.findByCnpj(cnpj).orElseThrow(() -> new ObjectNotFoundException("Convenio com CNPJ " + cnpj + " Não encontrado"));
        return modelMapper.map(convenio, ConvenioDTO.class);
    }

    @Transactional(readOnly = true)
    public List<ConvenioDTO> obterTodos() {
        List<ConvenioModel> convenios = convenioRepository.findAll();
        return convenios.stream().map(convenio -> modelMapper.map(convenio, ConvenioDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public ConvenioDTO salvar(ConvenioModel novoConvenio) {

        try {

            if (convenioRepository.existsByCnpj(novoConvenio.getCnpj())) {
                throw new ConstraintException("Já existe um convenio com esse CNPJ " + novoConvenio.getCnpj() + " na base de dados!");
            }

//          return convenioRepository.save(novoConvenio).toDTO();
            return modelMapper.map(convenioRepository.save(novoConvenio), ConvenioDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível salvar o convenio " + novoConvenio.getNome() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro de restrição de integridade ao salvar o convenio " + novoConvenio.getNome() + ".");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível salvar o convenio " + novoConvenio.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível salvar o convenio " + novoConvenio.getNome() + ". Falha na conexão com o banco de dados!");
        }
    }

    @Transactional
    public ConvenioDTO atualizar(ConvenioModel convenioExistente) {

        try {
            if (!convenioRepository.existsByCnpj(convenioExistente.getCnpj())) {
                throw new ConstraintException("O convenio com esse CNPJ " + convenioExistente.getCnpj() + " não existe na base de dados!");
            }

            return modelMapper.map(convenioRepository.save(convenioExistente), ConvenioDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível atualizar o convenio " + convenioExistente.getNome() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao atualizar o convenio " + convenioExistente.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível atualizar o convenio " + convenioExistente.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível atualizar o convenio " + convenioExistente.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível atualizar o convenio" + convenioExistente.getNome() + ". Não encontrado no banco de dados!");
        }
    }

    @Transactional
    public void deletar(ConvenioModel convenioExistente) {

        try {

            if (!convenioRepository.existsById(convenioExistente.getId())) {
                throw new ConstraintException("Convenio inexistente na base de dados!");
            }

            convenioRepository.delete(convenioExistente);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível deletar o convenio " + convenioExistente.getNome() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao deletar o convenio  " + convenioExistente.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível deletar o convenio  " + convenioExistente.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível atualizar o convenio  " + convenioExistente.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível deletar o convenio " + convenioExistente.getNome() + ". Não encontrado no banco de dados!");
        }
    }
}