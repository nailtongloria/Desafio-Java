package com.desafioJava.desafioJava;

import com.desafioJava.desafioJava.controller.PortifolioController;
import com.desafioJava.desafioJava.model.Funcionario;
import com.desafioJava.desafioJava.model.Portifolio;
import com.desafioJava.desafioJava.repository.FuncionarioRepository;
import com.desafioJava.desafioJava.repository.PortifolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.*;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PortifolioControllerTest {
    @InjectMocks
    private PortifolioController portifolioController;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private PortifolioRepository portifolioRepository;

    @Mock
    private Model model;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(portifolioController).build();
    }
    @Test
    public void testListPortifolios() throws Exception {
        Portifolio portifolio1 = new Portifolio();
        portifolio1.setId(1L);
        portifolio1.setNome("Portifolio 1");
        portifolio1.setDataInicio(new Date());
        portifolio1.setOrcamentoTotal(BigDecimal.valueOf(10000));
        portifolio1.setDescricao("Descrição 1");
        portifolio1.setStatus(1L);
        portifolio1.setRisco(2L);

        Portifolio portifolio2 = new Portifolio();
        portifolio2.setId(2L);
        portifolio2.setNome("Portifolio 2");
        portifolio2.setDataInicio(new Date());
        portifolio2.setOrcamentoTotal(BigDecimal.valueOf(20000));
        portifolio2.setDescricao("Descrição 2");
        portifolio2.setStatus(2L);
        portifolio2.setRisco(1L);

        Page<Portifolio> portifolioPage = new PageImpl<>(Arrays.asList(portifolio1, portifolio2));
        when(portifolioRepository.findAll(any(Pageable.class))).thenReturn(portifolioPage);

        mockMvc.perform(get("/portifolios"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-portifolios"))
                .andExpect(model().attributeExists("portifolios"))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("totalItems", (long) 2));
        verify(portifolioRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void testListPortifoliosWithPagination() throws Exception {
        Portifolio portifolio = new Portifolio();
        portifolio.setId(1L);
        portifolio.setNome("Portifolio Teste");
        portifolio.setDataInicio(new Date());
        portifolio.setOrcamentoTotal(BigDecimal.valueOf(10000));
        portifolio.setDescricao("Teste");
        portifolio.setStatus(1L);
        portifolio.setRisco(1L);

        Pageable pageable = PageRequest.of(0, 5, Sort.by("nome").ascending());
        Page<Portifolio> page = new PageImpl<>(Arrays.asList(portifolio), pageable, 1);

        when(portifolioRepository.findAll(any(Pageable.class))).thenReturn(page);

        String result = portifolioController.listPortifolios(0, 5, model);

        assertEquals("list-portifolios", result);
        verify(portifolioRepository, times(1)).findAll(any(Pageable.class));
        verify(model, times(1)).addAttribute(eq("portifolios"), anyList());
        verify(model, times(1)).addAttribute(eq("currentPage"), eq(0));
        verify(model, times(1)).addAttribute(eq("totalPages"), eq(1));
        verify(model, times(1)).addAttribute(eq("totalItems"), eq(1L));
    }

    @Test
    public void testShowCreateForm() throws Exception {
        Funcionario funcionario1 = new Funcionario();
        funcionario1.setId(1L);
        funcionario1.setNome("Funcionario 1");
        funcionario1.setCargo("Cargo 1");

        Funcionario funcionario2 = new Funcionario();
        funcionario2.setId(2L);
        funcionario2.setNome("Funcionario 2");
        funcionario2.setCargo("Cargo 2");

        List<Funcionario> funcionarios = Arrays.asList(funcionario1, funcionario2);
        when(funcionarioRepository.findAll()).thenReturn(funcionarios);

        mockMvc.perform(get("/portifolios/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-portifolio"))
                .andExpect(model().attributeExists("funcionarios"))
                .andExpect(model().attributeExists("portifolio"))
                .andExpect(model().attribute("funcionarios", funcionarios));

        verify(funcionarioRepository, times(1)).findAll();
    }

    @Test
    public void testSavePortifolio_Success() throws Exception {
        Portifolio portifolio = new Portifolio();
        portifolio.setNome("Novo Portifolio");
        mockMvc.perform(post("/portifolios")
                        .flashAttr("portifolio", portifolio)) // Simula o envio do objeto
                .andExpect(status().is3xxRedirection()) // Verifica redirecionamento
                .andExpect(redirectedUrl("/portifolios")); // Verifica a URL de redirecionamento
        verify(portifolioRepository, times(1)).save(any(Portifolio.class));
    }

    @Test
    void testShowPortifolio() throws Exception {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        Portifolio portifolio = new Portifolio();
        portifolio.setId(1L);
        portifolio.setNome("Portifólio de Teste");
        when(portifolioRepository.findById(anyLong())).thenReturn(Optional.of(portifolio));

        mockMvc = MockMvcBuilders.standaloneSetup(portifolioController).setViewResolvers(viewResolver).build();

        mockMvc.perform(get("/portifolios/1"))
                .andExpect(status().isOk()) // Verifica se o status HTTP é 200
                .andExpect(view().name("view-portifolio")) // Verifica se a view retornada é "view-portifolio"
                .andExpect(model().attributeExists("portifolio")) // Verifica se o modelo contém o atributo "portifolio"
                .andExpect(model().attribute("portifolio", portifolio)); // Verifica se o atributo tem o valor esperado
    }

    @Test
    void testEditPortifolio() throws Exception {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        Portifolio portifolio = new Portifolio();
        portifolio.setId(1L);
        portifolio.setNome("Portifólio Teste");
        portifolio.setDataInicio(new Date());
        portifolio.setDataRealTermino(new Date());
        portifolio.setPrevisaoTermino(new Date());
        portifolio.setOrcamentoTotal(new BigDecimal("1000.00"));
        portifolio.setDescricao("Descrição Teste");
        portifolio.setStatus(4L);
        portifolio.setRisco(2L);

        Funcionario funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("João");
        funcionario.setCargo("Gerente");
        portifolio.setGerenteResponsavel(funcionario);

        List<Funcionario> funcionarios = new ArrayList<>();
        funcionarios.add(funcionario);

        when(portifolioRepository.findById(anyLong())).thenReturn(Optional.of(portifolio));
        when(funcionarioRepository.findAll()).thenReturn(funcionarios);

        DateFormatter dateFormatter = new DateFormatter("yyyy-MM-dd");
        String dataInicioFormatada = dateFormatter.print(portifolio.getDataInicio(), LocaleContextHolder.getLocale());
        String dataRealTermino = dateFormatter.print(portifolio.getDataRealTermino(), LocaleContextHolder.getLocale());
        String dataPrevisaoTermino = dateFormatter.print(portifolio.getPrevisaoTermino(), LocaleContextHolder.getLocale());

        mockMvc = MockMvcBuilders.standaloneSetup(portifolioController).setViewResolvers(viewResolver).build();

        mockMvc.perform(get("/portifolios/edit/1"))
                .andExpect(status().isOk()) // Verifica se o status HTTP é 200
                .andExpect(view().name("edit-portifolio")) // Verifica se a view retornada é "edit-portifolio"
                .andExpect(model().attributeExists("portifolio", "funcionarios", "dataInicioFormatada", "dataRealTermino", "dataPrevisaoTermino")) // Verifica se o modelo contém os atributos corretos
                .andExpect(model().attribute("portifolio", portifolio)) // Verifica se o atributo "portifolio" no modelo é igual ao objeto mockado
                .andExpect(model().attribute("dataInicioFormatada", dataInicioFormatada)) // Verifica a data formatada
                .andExpect(model().attribute("dataRealTermino", dataRealTermino)) // Verifica a data formatada
                .andExpect(model().attribute("dataPrevisaoTermino", dataPrevisaoTermino)); // Verifica a data formatada
    }

    @Test
    void testEditPortifolio_NotFound() throws Exception {
        when(portifolioRepository.findById(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/portifolios/edit/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/portifolios"));
    }

    @Test
    void testDetailPortifolio() throws Exception {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        Portifolio portifolio = new Portifolio();
        portifolio.setId(1L);
        portifolio.setNome("Portifólio Teste");
        portifolio.setDataInicio(new Date());
        portifolio.setDataRealTermino(new Date());
        portifolio.setPrevisaoTermino(new Date());
        portifolio.setOrcamentoTotal(new BigDecimal("1000.00"));
        portifolio.setDescricao("Descrição Teste");
        portifolio.setStatus(4L);
        portifolio.setRisco(2L);

        Funcionario funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("João");
        funcionario.setCargo("Gerente");
        portifolio.setGerenteResponsavel(funcionario);

        List<Funcionario> funcionarios = new ArrayList<>();
        funcionarios.add(funcionario);

        when(portifolioRepository.findById(anyLong())).thenReturn(Optional.of(portifolio));
        when(funcionarioRepository.findAll()).thenReturn(funcionarios);

        DateFormatter dateFormatter = new DateFormatter("yyyy-MM-dd");
        String dataInicioFormatada = dateFormatter.print(portifolio.getDataInicio(), LocaleContextHolder.getLocale());
        String dataRealTermino = dateFormatter.print(portifolio.getDataRealTermino(), LocaleContextHolder.getLocale());
        String dataPrevisaoTermino = dateFormatter.print(portifolio.getPrevisaoTermino(), LocaleContextHolder.getLocale());

        mockMvc = MockMvcBuilders.standaloneSetup(portifolioController).setViewResolvers(viewResolver).build();

        mockMvc.perform(get("/portifolios/detail/1"))
                .andExpect(status().isOk()) // Verifica se o status HTTP é 200
                .andExpect(view().name("detail-portifolio")) // Verifica se a view retornada é "detail-portifolio"
                .andExpect(model().attributeExists("portifolio", "funcionarios", "dataInicioFormatada", "dataRealTermino", "dataPrevisaoTermino")) // Verifica se o modelo contém os atributos corretos
                .andExpect(model().attribute("portifolio", portifolio)) // Verifica se o atributo "portifolio" no modelo é igual ao objeto mockado
                .andExpect(model().attribute("dataInicioFormatada", dataInicioFormatada)) // Verifica a data formatada
                .andExpect(model().attribute("dataRealTermino", dataRealTermino)) // Verifica a data formatada
                .andExpect(model().attribute("dataPrevisaoTermino", dataPrevisaoTermino)); // Verifica a data formatada

    }


    @Test
    void testDetailPortifolio_NotFound() throws Exception {
        when(portifolioRepository.findById(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/portifolios/detail/999"))
                .andExpect(status().is3xxRedirection()) // Verifica se houve redirecionamento
                .andExpect(view().name("redirect:/portifolios")); // Verifica se redirecionou para a página de listagem
    }
    @Test
    void testUpdatePortifolio() throws Exception {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        Portifolio portifolio = new Portifolio();
        portifolio.setId(1L);
        portifolio.setNome("Portifólio Atualizado");
        portifolio.setDataInicio(new Date());
        portifolio.setDataRealTermino(new Date());
        portifolio.setPrevisaoTermino(new Date());
        portifolio.setOrcamentoTotal(new BigDecimal("2000.00"));
        portifolio.setDescricao("Descrição Atualizada");
        portifolio.setStatus(5L); // Planejado
        portifolio.setRisco(1L); // Baixo risco

        Funcionario funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("Gerente Atualizado");
        funcionario.setCargo("Gerente de Projeto");
        portifolio.setGerenteResponsavel(funcionario);


        when(portifolioRepository.save(any(Portifolio.class))).thenReturn(portifolio);


        mockMvc = MockMvcBuilders.standaloneSetup(portifolioController).setViewResolvers(viewResolver).build();


        mockMvc.perform(post("/portifolios/update/1")
                        .param("nome", "Portifólio Atualizado")
                        .param("dataInicio", "2024-09-22")
                        .param("dataRealTermino", "2024-10-22")
                        .param("previsaoTermino", "2024-10-15")
                        .param("orcamentoTotal", "2000.00")
                        .param("descricao", "Descrição Atualizada")
                        .param("status", "5")
                        .param("risco", "1")
                        .param("gerenteResponsavel.id", "1")
                        .param("gerenteResponsavel.nome", "Gerente Atualizado")
                        .param("gerenteResponsavel.cargo", "Gerente de Projeto"))
                .andExpect(status().is3xxRedirection()) // Verifica se o status HTTP é 302 (redirecionamento)
                .andExpect(view().name("redirect:/portifolios")); // Verifica se a view retornada é o redirecionamento para "/portifolios"


        verify(portifolioRepository, times(1)).save(any(Portifolio.class));
    }

    @Test
    void testDeletePortifolio_Success() throws Exception {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        Portifolio portifolio = new Portifolio();
        portifolio.setId(1L);
        portifolio.setStatus(3L);

        when(portifolioRepository.findById(1L)).thenReturn(java.util.Optional.of(portifolio));

        mockMvc = MockMvcBuilders.standaloneSetup(portifolioController).setViewResolvers(viewResolver).build();

        mockMvc.perform(get("/portifolios/delete/1"))
                .andExpect(status().is3xxRedirection()) // Verifica se o status HTTP é 302 (redirecionamento)
                .andExpect(view().name("redirect:/portifolios")); // Verifica se a view retornada é o redirecionamento para "/portifolios"

        verify(portifolioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePortifolio_NotAllowedStatus() throws Exception {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        Portifolio portifolio = new Portifolio();
        portifolio.setId(1L);
        portifolio.setStatus(4L);

        when(portifolioRepository.findById(1L)).thenReturn(java.util.Optional.of(portifolio));

        mockMvc = MockMvcBuilders.standaloneSetup(portifolioController).setViewResolvers(viewResolver).build();

        mockMvc.perform(get("/portifolios/delete/1"))
                .andExpect(status().is3xxRedirection()) // Verifica se o status HTTP é 302 (redirecionamento)
                .andExpect(view().name("redirect:/portifolios")) // Verifica se a view retornada é o redirecionamento para "/portifolios"
                .andExpect(flash().attribute("errorMessage", "Portfólio não pode ser excluído porque está em um status não permitido.")); // Verifica se a mensagem de erro foi adicionada ao RedirectAttributes

        verify(portifolioRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeletePortifolio_NotFound() throws Exception {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        when(portifolioRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        mockMvc = MockMvcBuilders.standaloneSetup(portifolioController).setViewResolvers(viewResolver).build();

        mockMvc.perform(get("/portifolios/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/portifolios"))
                .andExpect(flash().attribute("errorMessage", "Portfólio não encontrado."));
        verify(portifolioRepository, never()).deleteById(anyLong());
    }

}
