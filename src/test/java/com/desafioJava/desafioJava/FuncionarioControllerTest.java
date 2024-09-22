package com.desafioJava.desafioJava;

import com.desafioJava.desafioJava.controller.FuncionarioController;
import com.desafioJava.desafioJava.model.Funcionario;
import com.desafioJava.desafioJava.repository.FuncionarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@WebMvcTest(FuncionarioController.class)
public class FuncionarioControllerTest {
    @Autowired
    private FuncionarioController funcionarioController;
    @MockBean
    private FuncionarioRepository funcionarioRepository;

    @Test
    public void testGetAllFuncionarios() {
        Funcionario funcionario1 = new Funcionario();
        funcionario1.setId(1L);
        funcionario1.setNome("João");
        funcionario1.setCargo("Gerente");
        Funcionario funcionario2 = new Funcionario();
        funcionario2.setId(2L);
        funcionario2.setNome("Maria");
        funcionario2.setCargo("Analista");
        List<Funcionario> funcionarios = Arrays.asList(funcionario1, funcionario2);
        when(funcionarioRepository.findAll()).thenReturn(funcionarios);
        ResponseEntity<List<Funcionario>> response = funcionarioController.getAllFuncionarios();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("João", response.getBody().get(0).getNome());
        assertEquals("Maria", response.getBody().get(1).getNome());
        verify(funcionarioRepository, times(1)).findAll();
    }

    @Test
    public void testCreateFuncionario() {
        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setId(1L);
        novoFuncionario.setNome("João");
        novoFuncionario.setCargo("Gerente");
        when(funcionarioRepository.save(novoFuncionario)).thenReturn(novoFuncionario);
        ResponseEntity<Funcionario> response = funcionarioController.createFuncionario(novoFuncionario);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(novoFuncionario.getId(), response.getBody().getId());
        assertEquals(novoFuncionario.getNome(), response.getBody().getNome());
        assertEquals(novoFuncionario.getCargo(), response.getBody().getCargo());
        verify(funcionarioRepository, times(1)).save(novoFuncionario);
    }

    @Test
    public void testGetFuncionarioByIdFound() {
        Long funcionarioId = 1L;
        Funcionario funcionario = new Funcionario();
        funcionario.setId(funcionarioId);
        funcionario.setNome("João");
        funcionario.setCargo("Gerente");
        when(funcionarioRepository.findById(funcionarioId)).thenReturn(Optional.of(funcionario));
        ResponseEntity<Funcionario> response = funcionarioController.getFuncionarioById(funcionarioId);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(funcionario.getId(), response.getBody().getId());
        assertEquals(funcionario.getNome(), response.getBody().getNome());
        assertEquals(funcionario.getCargo(), response.getBody().getCargo());
        verify(funcionarioRepository, times(1)).findById(funcionarioId);
    }

    @Test
    public void testGetFuncionarioByIdNotFound() {
        Long funcionarioId = 1L;
        when(funcionarioRepository.findById(funcionarioId)).thenReturn(Optional.empty());
        ResponseEntity<Funcionario> response = funcionarioController.getFuncionarioById(funcionarioId);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(null, response.getBody());
        verify(funcionarioRepository, times(1)).findById(funcionarioId);
    }

    @Test
    void testUpdateFuncionarioSuccess() {
        Long funcionarioId = 1L;
        Funcionario existingFuncionario = new Funcionario();
        existingFuncionario.setId(funcionarioId);
        existingFuncionario.setNome("João");
        existingFuncionario.setCargo("Gerente");
        Funcionario updatedFuncionario = new Funcionario();
        updatedFuncionario.setNome("João Atualizado");
        updatedFuncionario.setCargo("Diretor");
        when(funcionarioRepository.findById(funcionarioId)).thenReturn(Optional.of(existingFuncionario));
        when(funcionarioRepository.save(existingFuncionario)).thenReturn(existingFuncionario);
        ResponseEntity<Funcionario> response = funcionarioController.updateFuncionario(funcionarioId, updatedFuncionario);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("João Atualizado", response.getBody().getNome());
        assertEquals("Diretor", response.getBody().getCargo());
        verify(funcionarioRepository, times(1)).findById(funcionarioId);
        verify(funcionarioRepository, times(1)).save(existingFuncionario);
    }

    @Test
    void testUpdateFuncionarioNotFound() {
        Long funcionarioId = 1L;
        Funcionario updatedFuncionario = new Funcionario();
        updatedFuncionario.setNome("João Atualizado");
        updatedFuncionario.setCargo("Diretor");
        when(funcionarioRepository.findById(funcionarioId)).thenReturn(Optional.empty());
        ResponseEntity<Funcionario> response = funcionarioController.updateFuncionario(funcionarioId, updatedFuncionario);
        assertEquals(404, response.getStatusCodeValue());
        verify(funcionarioRepository, times(1)).findById(funcionarioId);
        verify(funcionarioRepository, never()).save(any(Funcionario.class));
    }

    @Test
    public void testDeleteFuncionarioFound() {
        Long funcionarioId = 1L;
        Funcionario funcionario = new Funcionario();
        funcionario.setId(funcionarioId);
        when(funcionarioRepository.findById(funcionarioId)).thenReturn(Optional.of(funcionario));
        ResponseEntity<Void> response = funcionarioController.deleteFuncionario(funcionarioId);
        verify(funcionarioRepository, times(1)).deleteById(funcionarioId);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    public void testDeleteFuncionarioNotFound() {
        Long funcionarioId = 1L;
        when(funcionarioRepository.findById(funcionarioId)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = funcionarioController.deleteFuncionario(funcionarioId);
        verify(funcionarioRepository, never()).deleteById(funcionarioId);
        assertEquals(404, response.getStatusCodeValue());
    }

}
