package com.desafioJava.desafioJava.controller;
import com.desafioJava.desafioJava.model.Funcionario;
import com.desafioJava.desafioJava.model.Portifolio;
import com.desafioJava.desafioJava.repository.FuncionarioRepository;
import com.desafioJava.desafioJava.repository.PortifolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/portifolios")
public class PortifolioController {
    @Autowired
    private PortifolioRepository portifolioRepository;
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    public static String FuncionarioModel="funcionarios";
    public static String PortifolioModel="portifolio";
    public static String redirectPagePortifolio="redirect:/portifolios";
    @GetMapping
    public String listPortifolios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());
        Page<Portifolio> portifolioPage = portifolioRepository.findAll(pageable);
        model.addAttribute("portifolios", portifolioPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", portifolioPage.getTotalPages());
        model.addAttribute("totalItems", portifolioPage.getTotalElements());
        return "list-portifolios";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        model.addAttribute(FuncionarioModel, funcionarios);
        model.addAttribute(PortifolioModel, new Portifolio());
        return "create-portifolio";
    }
    @PostMapping
    public String savePortifolio(@ModelAttribute Portifolio portifolio, BindingResult result) {
        if (result.hasErrors()) {
            // Retorna para o formulário em caso de erros
            return "create-portifolio";
        }
        portifolioRepository.save(portifolio);
        return redirectPagePortifolio;
    }
    @GetMapping("/{id}")
    public String showPortifolio(@PathVariable Long id, Model model) {
        Portifolio portifolio = portifolioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid portfolio Id:" + id));
        model.addAttribute(PortifolioModel, portifolio);
        return "view-portifolio";
    }
    @GetMapping("/edit/{id}")
    public String editPortifolio(@PathVariable("id") Long id, Model model) {
        Portifolio portifolio = portifolioRepository.findById(id).orElse(null);
        if (portifolio == null) {
            return "redirect:/portifolios"; // Redireciona para a página de listagem caso o portifolio não exista
        }
        DateFormatter dateFormatter = new DateFormatter("yyyy-MM-dd");
        String dataInicioFormatada = dateFormatter.print(portifolio.getDataInicio(), LocaleContextHolder.getLocale());
        String dataRealTermino= dateFormatter.print(portifolio.getDataRealTermino(), LocaleContextHolder.getLocale());
        String dataPrevisaoTermino= dateFormatter.print(portifolio.getPrevisaoTermino(), LocaleContextHolder.getLocale());
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        model.addAttribute(PortifolioModel, portifolio);
        model.addAttribute("funcionarios", funcionarios);
        model.addAttribute("dataInicioFormatada", dataInicioFormatada);
        model.addAttribute("dataRealTermino", dataRealTermino);
        model.addAttribute("dataPrevisaoTermino", dataPrevisaoTermino);
        return "edit-portifolio";
    }
    @GetMapping("/detail/{id}")
    public String detailPortifolio(@PathVariable("id") Long id, Model model) {
        Portifolio portifolio = portifolioRepository.findById(id).orElse(null);
        if (portifolio == null) {
            return redirectPagePortifolio;
        }
        DateFormatter dateFormatter = new DateFormatter("yyyy-MM-dd");
        String dataInicioFormatada = dateFormatter.print(portifolio.getDataInicio(), LocaleContextHolder.getLocale());
        String dataRealTermino= dateFormatter.print(portifolio.getDataRealTermino(), LocaleContextHolder.getLocale());
        String dataPrevisaoTermino= dateFormatter.print(portifolio.getPrevisaoTermino(), LocaleContextHolder.getLocale());

        List<Funcionario> funcionarios = funcionarioRepository.findAll();

        model.addAttribute(PortifolioModel, portifolio);
        model.addAttribute(FuncionarioModel, funcionarios);
        model.addAttribute("dataInicioFormatada", dataInicioFormatada);
        model.addAttribute("dataRealTermino", dataRealTermino);
        model.addAttribute("dataPrevisaoTermino", dataPrevisaoTermino);

        return "detail-portifolio";
    }
    @PostMapping("/update/{id}")
    public String updatePortifolio(@PathVariable Long id, @ModelAttribute Portifolio portifolio) {
        portifolio.setId(id);
        portifolioRepository.save(portifolio);
        return redirectPagePortifolio;
    }

    @GetMapping("/delete/{id}")
    public String deletePortifolio(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Portifolio portifolio = portifolioRepository.findById(id).orElse(null);
        if (portifolio != null) {
            if (portifolio.getStatus() == 4 || portifolio.getStatus() == 6 || portifolio.getStatus() == 7) {
                redirectAttributes.addFlashAttribute("errorMessage", "Portfólio não pode ser excluído porque está em um status não permitido.");
                return redirectPagePortifolio;
            } else {
                portifolioRepository.deleteById(id);
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Portfólio não encontrado.");
        }
        return redirectPagePortifolio;
    }

}
