<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.List" %>
<%@page import="com.desafioJava.desafioJava.model.Funcionario" %>
<%@page import="com.desafioJava.desafioJava.model.Portifolio" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Editar Portfólio</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .form-control, .form-select {
            max-width: 100%;
        }
        .alert-custom {
            position: fixed;
            top: 1rem;
            right: 1rem;
            max-width: 300px; /* Tamanho menor para o alerta */
            display: none; /* Inicialmente oculto */
        }
    </style>
</head>
<body>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <h1 class="text-center mb-4">Visualizar</h1>

            <div id="successAlert" class="alert alert-success alert-dismissible fade show alert-custom" role="alert">
                <strong>Salvo com sucesso!</strong> O portfólio foi atualizado.
                <button type="button" class="btn-close" aria-label="Close" onclick="hideAlert()"></button>
            </div>

            <form id="portifolioForm" action="/portifolios/detail/${portifolio.id}" method="post" onsubmit="return validateForm()">
                <input type="hidden" name="_method" value="PUT">
                <div class="row mb-3">
                    <div class="col-md-4">
                        <label for="nome" class="form-label">Nome</label>
                        <input type="text" class="form-control" id="nome" name="nome" placeholder="Digite o nome do portfólio" value="${portifolio.nome}" required disabled>
                        <div id="nomeError" class="invalid-feedback">O nome não pode estar vazio.</div>
                    </div>
                    <div class="col-md-4">
                        <label for="dataInicio" class="form-label">Data de Início</label>
                        <input type="date" class="form-control" id="dataInicio" name="dataInicio" value="${dataInicioFormatada}" required disabled>
                        <div id="dataInicioError" class="invalid-feedback">A data de início não pode estar vazia.</div>
                    </div>
                    <div class="col-md-4">
                        <label for="orcamentoTotal" class="form-label">Orçamento Total</label>
                        <input type="number" step="0.01" class="form-control" id="orcamentoTotal" name="orcamentoTotal" placeholder="Digite o orçamento total" value="${portifolio.orcamentoTotal}" disabled>
                        <div id="orcamentoTotalError" class="invalid-feedback">O orçamento total deve ser um número válido.</div>
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col-md-4">
                        <label for="funcionario" class="form-label">Nome</label>
                        <select id="gerenteResponsavel" name="gerenteResponsavel" class="form-select" disabled>
                            <option value="">Selecione um Funcionário</option>
                            <%
                            List<Funcionario> funcionarios = (List<Funcionario>) request.getAttribute("funcionarios");
                            Portifolio portifolio = (Portifolio) request.getAttribute("portifolio");
                            for (Funcionario funcionario : funcionarios) {
                                boolean selected = funcionario.getId().equals(portifolio.getGerenteResponsavel().getId());
                            %>
                                <option value="<%= funcionario.getId() %>" data-cargo="<%= funcionario.getCargo() %>" <%= selected ? "selected" : "" %>><%= funcionario.getNome() %></option>
                            <% } %>
                        </select>
                        <div id="gerenteResponsavelError" class="invalid-feedback">Você deve selecionar um funcionário.</div>
                    </div>
                    <div class="col-md-4">
                        <label for="cargo" class="form-label">Cargo</label>
                        <select id="cargo" name="cargo" class="form-select" disabled>
                            <option value="">Selecione um Cargo</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label for="status" class="form-label">Status</label>
                        <select id="status" name="status" class="form-select" disabled>
                            <option value="">Selecione um Status</option>
                            <option value="1" <%= portifolio.getStatus().equals(1l) ? "selected" : "" %>>Em Análise</option>
                            <option value="2" <%= portifolio.getStatus().equals(2l) ? "selected" : "" %>>Análise Realizada</option>
                            <option value="3" <%= portifolio.getStatus().equals(3l) ? "selected" : "" %>>Análise Aprovada</option>
                            <option value="4" <%= portifolio.getStatus().equals(4l) ? "selected" : "" %>>Iniciado</option>
                            <option value="5" <%= portifolio.getStatus().equals(5l) ? "selected" : "" %>>Planejado</option>
                            <option value="6" <%= portifolio.getStatus().equals(6l) ? "selected" : "" %>>Em Andamento</option>
                            <option value="7" <%= portifolio.getStatus().equals(7l) ? "selected" : "" %>>Encerrado</option>
                            <option value="8" <%= portifolio.getStatus().equals(8l) ? "selected" : "" %>>Cancelado</option>
                        </select>
                        <div id="statusError" class="invalid-feedback">Você deve selecionar um status.</div>
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col-md-4">
                        <label for="previsaoTermino" class="form-label">Previsão de Término</label>
                        <input type="date" class="form-control" id="previsaoTermino" name="previsaoTermino" value="${dataPrevisaoTermino}" disabled>
                    </div>
                    <div class="col-md-4">
                        <label for="dataRealTermino" class="form-label">Data Real de Término</label>
                        <input type="date" class="form-control" id="dataRealTermino" name="dataRealTermino" value="${dataRealTermino}" disabled>
                    </div>
                    <div class="col-md-4">
                        <label for="risco" class="form-label">Risco</label>
                        <select id="risco" name="risco" class="form-select" disabled>
                            <option value="">Selecione o Risco</option>
                            <option value="1" <%= portifolio.getRisco().equals(1l) ? "selected" : "" %>>Baixo Risco</option>
                            <option value="2" <%= portifolio.getRisco().equals(2l) ? "selected" : "" %>>Médio Risco</option>
                            <option value="3" <%= portifolio.getRisco().equals(3l) ? "selected" : "" %>>Alto Risco</option>
                        </select>
                        <div id="riscoError" class="invalid-feedback">Você deve selecionar um risco.</div>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="descricao" class="form-label">Descrição</label>
                    <textarea id="descricao" name="descricao" class="form-control" rows="3" disabled><%= portifolio.getDescricao() %></textarea>
                </div>

                <div class="d-grid gap-2 d-md-flex justify-content-md-between">
                    <a href="/portifolios" class="btn btn-primary">Voltar à Lista</a>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        var funcionarioSelect = document.getElementById('gerenteResponsavel');
        var cargoSelect = document.getElementById('cargo');

