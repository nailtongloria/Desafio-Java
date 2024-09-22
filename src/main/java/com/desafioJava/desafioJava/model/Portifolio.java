package com.desafioJava.desafioJava.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "portifolio")
public class Portifolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataInicio;
    @ManyToOne
    @JoinColumn(nullable = false) // Garante que o relacionamento não pode ser nulo
    private Funcionario gerenteResponsavel;
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date previsaoTermino;
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataRealTermino;
    @Column(nullable = false)
    private BigDecimal orcamentoTotal;
    @Column(nullable = false)
    private String descricao;
    @Column(nullable = false)
    private Long status;
    @Column(nullable = false)
    private Long risco;
    public Portifolio() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Date getDataInicio() {
        return dataInicio;
    }
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }
    public Funcionario getGerenteResponsavel() {
        return gerenteResponsavel;
    }
    public void setGerenteResponsavel(Funcionario gerenteResponsavel) {
        this.gerenteResponsavel = gerenteResponsavel;
    }
    public Date getPrevisaoTermino() {
        return previsaoTermino;
    }
    public void setPrevisaoTermino(Date previsaoTermino) {
        this.previsaoTermino = previsaoTermino;
    }
    public Date getDataRealTermino() {
        return dataRealTermino;
    }
    public void setDataRealTermino(Date dataRealTermino) {
        this.dataRealTermino = dataRealTermino;
    }
    public BigDecimal getOrcamentoTotal() {
        return orcamentoTotal;
    }
    public void setOrcamentoTotal(BigDecimal orcamentoTotal) {
        this.orcamentoTotal = orcamentoTotal;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public Long getStatus() {
        return status;
    }
    public void setStatus(Long status) {
        this.status = status;
    }
    public Long getRisco() {
        return risco;
    }
    public void setRisco(Long risco) {
        this.risco = risco;
    }


}
