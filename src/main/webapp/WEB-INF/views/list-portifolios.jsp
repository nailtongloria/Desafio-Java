<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="pt_br">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Desafio Java</title>

    <link href="<c:url value='/static/node_modules/bootstrap/dist/css/bootstrap.min.css'/>" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

    <style>
        .table th, .table td {
            text-align: center;
            vertical-align: middle;
            word-wrap: break-word;
        }

        .action-icons {
            display: flex;
            justify-content: center;
            gap: 10px;
        }

        .action-cell {
            min-height: 60px;
        }

        .btn-sm i {
            font-size: 0.85rem;
        }
    </style>
</head>
<body>

<nav class="navbar navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand">Portfólio de Projetos</a>
    </div>
</nav>

<div class="container mt-3">
    <div class="row">
        <div class="col text-end">
            <a href="/portifolios/new" class="btn btn-success btn-sm" data-bs-toggle="tooltip" title="Cadastro">
                <i class="bi bi-plus-lg"></i>
            </a>
        </div>
    </div>
</div>

<div class="container mt-3">
    <div class="table-responsive">
        <table class="table table-striped" style="table-layout: fixed;">
            <thead>
                <tr>
                    <th>Nome</th>
                    <th>Data Início</th>
                    <th>Previsão Término</th>
                    <th>Data Real Término</th>
                    <th>Orçamento Total</th>
                    <th>Descrição</th>
                    <th>Status</th>
                    <th>Funcionário</th>
                    <th>Cargo</th>
                    <th>Risco</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="portifolio" items="${portifolios}">
                    <tr>
                        <td>${portifolio.nome}</td>
                        <td><fmt:formatDate value="${portifolio.dataInicio}" pattern="dd/MM/yyyy" /></td>
                        <td><fmt:formatDate value="${portifolio.previsaoTermino}" pattern="dd/MM/yyyy" /></td>
                        <td><fmt:formatDate value="${portifolio.dataRealTermino}" pattern="dd/MM/yyyy" /></td>
                        <td><fmt:formatNumber value="${portifolio.orcamentoTotal}" type="currency" /></td>
                        <td>${portifolio.descricao}</td>
                        <td>
                            <c:choose>
                                <c:when test="${portifolio.status == 1}">em análise</c:when>
                                <c:when test="${portifolio.status == 2}">análise realizada</c:when>
                                <c:when test="${portifolio.status == 3}">análise aprovada</c:when>
                                <c:when test="${portifolio.status == 4}">iniciado</c:when>
                                <c:when test="${portifolio.status == 5}">planejado</c:when>
                                <c:when test="${portifolio.status == 6}">em andamento</c:when>
                                <c:when test="${portifolio.status == 7}">encerrado</c:when>
                                <c:when test="${portifolio.status == 8}">cancelado</c:when>
                                <c:otherwise>Indefinido</c:otherwise>
                            </c:choose>
                        </td>
                        <td>${portifolio.gerenteResponsavel.nome}</td>
                        <td>${portifolio.gerenteResponsavel.cargo}</td>
                        <td>
                            <c:choose>
                                <c:when test="${portifolio.risco == 1}">baixo risco</c:when>
                                <c:when test="${portifolio.risco == 2}">médio risco</c:when>
                                <c:when test="${portifolio.risco == 3}">alto risco</c:when>
                                <c:otherwise>Indefinido</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="action-cell">
                            <div class="action-icons">
                                <a href="/portifolios/detail/${portifolio.id}" class="btn btn-primary btn-sm" data-bs-toggle="tooltip" title="Detalhes">
                                    <i class="bi bi-eye"></i>
                                </a>
                                <a href="/portifolios/edit/${portifolio.id}" class="btn btn-warning btn-sm" data-bs-toggle="tooltip" title="Editar">
                                    <i class="bi bi-pencil"></i>
                                </a>
                                <c:choose>
                                    <c:when test="${portifolio.status == 4 || portifolio.status == 6 || portifolio.status == 7}">
                                        <button class="btn btn-danger btn-sm" disabled>
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="/portifolios/delete/${portifolio.id}" class="btn btn-danger btn-sm" data-bs-toggle="tooltip" title="Deletar">
                                            <i class="bi bi-trash"></i>
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<c:if test="${totalPages > 1}">
    <div class="container mt-3">
        <nav aria-label="Page navigation">
            <ul class="pagination justify-content-center">
                <c:if test="${currentPage > 0}">
                    <li class="page-item">
                        <a class="page-link" href="?page=${currentPage - 1}&size=${pageSize}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                </c:if>
                <c:forEach begin="0" end="${totalPages - 1}" var="i">
                    <li class="page-item <c:if test='${i == currentPage}'>active</c:if>'">
                        <a class="page-link" href="?page=${i}&size=${pageSize}">${i + 1}</a>
                    </li>
                </c:forEach>
                <c:if test="${currentPage < totalPages - 1}">
                    <li class="page-item">
                        <a class="page-link" href="?page=${currentPage + 1}&size=${pageSize}" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </c:if>
            </ul>
        </nav>
    </div>
</c:if>

<script src="<c:url value='/static/node_modules/bootstrap/dist/js/bootstrap.bundle.min.js'/>"></script>
<script>
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
</script>

</body>
</html>
