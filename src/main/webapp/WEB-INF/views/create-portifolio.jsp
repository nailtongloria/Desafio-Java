<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.List" %>
<%@page import="com.desafioJava.desafioJava.model.Funcionario" %>
<%@page import="com.desafioJava.desafioJava.model.Portifolio" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Criar Portfólio</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .form-control {
            max-width: 100%;
        }
        .form-select {
            max-width: 100%;
        }
        .alert-custom {
            position: fixed;
            top: 1rem;
            right: 1rem;
            max-width: 300px; /* Tamanho menor para o alerta */
            display: none; /* Inicialmente oculto */
        }
        .invalid-feedback {
            display: none;
        }
    </style>
</head>
<body>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <h1 class="text-center mb-4">Criar Novo Portfólio</h1>

            <div id="successAlert" class="alert alert-success alert-dismissible fade show alert-custom" role="alert">
                <strong>Salvo com sucesso!</strong> O portfólio foi salvo.
                <button type="button" class="btn-close" aria-label="Close" onclick="hideAlert()"></button>
            </div>

            <form id="portifolioForm" action="/portifolios" method="post" onsubmit="return validateForm()">
                <div class="row mb-3">
                    <div class="col-md-4">
                        <label for="nome" class="form-label">Nome</label>
                        <input type="text" class="form-control" id="nome" name="nome" placeholder="Digite o nome do portfólio" required>
                        <div id="nomeError" class="invalid-feedback">O nome não pode estar vazio.</div>
                    </div>
                    <div class="col-md-4">
                        <label for="dataInicio" class="form-label">Data de Início</label>
                        <input type="date" class="form-control" id="dataInicio" name="dataInicio" required>
                        <div id="dataInicioError" class="invalid-feedback">A data de início não pode estar vazia.</div>
                    </div>
                    <div class="col-md-4">
                        <label for="orcamentoTotal" class="form-label">Orçamento Total</label>
                        <input type="number" step="0.01" class="form-control" id="orcamentoTotal" name="orcamentoTotal" placeholder="Digite o orçamento total">
                        <div id="orcamentoTotalError" class="invalid-feedback">O orçamento total deve ser um número válido.</div>
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col-md-4">
                        <label for="funcionario" class="form-label">Nome</label>
                        <select id="gerenteResponsavel" name="gerenteResponsavel" class="form-select" required>
                            <option value="">Selecione um Funcionário</option>
                            <%
                            List<Funcionario> funcionarios = (List<Funcionario>) request.getAttribute("funcionarios");
                            for (Funcionario funcionario : funcionarios) {
                            %>
                                <option value="<%= funcionario.getId() %>" data-cargo="<%= funcionario.getCargo() %>"><%= funcionario.getNome() %></option>
                            <% } %>
                        </select>
                        <div id="gerenteResponsavelError" class="invalid-feedback">Você deve selecionar um funcionário.</div>
                    </div>
                    <div class="col-md-4">
                        <label for="cargo" class="form-label">Cargo</label>
                        <select id="cargo" name="cargo" class="form-select">
                            <option value="">Selecione um Cargo</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label for="status" class="form-label">Status</label>
                        <select id="status" name="status" class="form-select" required>
                            <option value="">Selecione um Status</option>
                            <option value="1">Em Análise</option>
                            <option value="2">Análise Realizada</option>
                            <option value="3">Análise Aprovada</option>
                            <option value="4">Iniciado</option>
                            <option value="5">Planejado</option>
                            <option value="6">Em Andamento</option>
                            <option value="7">Encerrado</option>
                            <option value="8">Cancelado</option>
                        </select>
                        <div id="statusError" class="invalid-feedback">Você deve selecionar um status.</div>
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col-md-4">
                        <label for="previsaoTermino" class="form-label">Previsão de Término</label>
                        <input type="date" class="form-control" id="previsaoTermino" name="previsaoTermino">
                    </div>
                    <div class="col-md-4">
                        <label for="dataRealTermino" class="form-label">Data Real de Término</label>
                        <input type="date" class="form-control" id="dataRealTermino" name="dataRealTermino">
                    </div>
                    <div class="col-md-4">
                        <label for="risco" class="form-label">Risco</label>
                        <select id="risco" name="risco" class="form-select">
                            <option value="">Selecione o Risco</option>
                            <option value="1">Baixo Risco</option>
                            <option value="2">Médio Risco</option>
                            <option value="3">Alto Risco</option>
                        </select>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="descricao" class="form-label">Descrição</label>
                    <textarea id="descricao" name="descricao" class="form-control" rows="3"></textarea>
                    <div id="descricaoError" class="invalid-feedback">A descrição não pode estar vazia.</div>
                </div>


                <div class="d-grid gap-2 d-md-flex justify-content-md-between">
                    <button type="submit" class="btn btn-success">Salvar</button>
                    <button type="reset" class="btn btn-secondary">Limpar Campos</button>
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

        funcionarioSelect.addEventListener('change', function() {
            var selectedOption = funcionarioSelect.options[funcionarioSelect.selectedIndex];
            var cargo = selectedOption.getAttribute('data-cargo');

            cargoSelect.innerHTML = '';

            if (cargo) {
                var option = document.createElement('option');
                option.textContent = cargo;
                option.value = cargo;
                cargoSelect.appendChild(option);
            }
        });
    });

    function validateForm() {
        let isValid = true;

        const nome = document.getElementById('nome');
        const nomeError = document.getElementById('nomeError');
        if (!nome.value.trim()) {
            nome.classList.add('is-invalid');
            nomeError.style.display = 'block';
            isValid = false;
        } else {
            nome.classList.remove('is-invalid');
            nomeError.style.display = 'none';
        }

         const descricao = document.getElementById('descricao');
         const descricaoError = document.getElementById('descricaoError');
         if (!descricao.value.trim()) {
             descricao.classList.add('is-invalid');
             descricaoError.style.display = 'block';
             isValid = false;
         } else {
             descricao.classList.remove('is-invalid');
             descricaoError.style.display = 'none';
         }


        const dataInicio = document.getElementById('dataInicio');
        const dataInicioError = document.getElementById('dataInicioError');
        if (!dataInicio.value.trim()) {
            dataInicio.classList.add('is-invalid');
            dataInicioError.style.display = 'block';
            isValid = false;
        } else {
            dataInicio.classList.remove('is-invalid');
            dataInicioError.style.display = 'none';
        }


        const orcamentoTotal = document.getElementById('orcamentoTotal');
        const orcamentoTotalError = document.getElementById('orcamentoTotalError');
        if (orcamentoTotal.value && isNaN(orcamentoTotal.value)) {
            orcamentoTotal.classList.add('is-invalid');
            orcamentoTotalError.style.display = 'block';
            isValid = false;
        } else {
            orcamentoTotal.classList.remove('is-invalid');
            orcamentoTotalError.style.display = 'none';
        }


        const gerenteResponsavel = document.getElementById('gerenteResponsavel');
        const gerenteResponsavelError = document.getElementById('gerenteResponsavelError');
        if (!gerenteResponsavel.value.trim()) {
            gerenteResponsavel.classList.add('is-invalid');
            gerenteResponsavelError.style.display = 'block';
            isValid = false;
        } else {
            gerenteResponsavel.classList.remove('is-invalid');
            gerenteResponsavelError.style.display = 'none';
        }

        const status = document.getElementById('status');
        const statusError = document.getElementById('statusError');
        if (!status.value.trim()) {
            status.classList.add('is-invalid');
            statusError.style.display = 'block';
            isValid = false;
        } else {
            status.classList.remove('is-invalid');
            statusError.style.display = 'none';
        }

        if (isValid) {

            document.getElementById('successAlert').style.display = 'block';


            setTimeout(() => {
                document.getElementById('portifolioForm').submit();
            }, 1500);
        }

        return false;
    }

    function hideAlert() {
        document.getElementById('successAlert').style.display = 'none';
    }
</script>

</body>
</html>
