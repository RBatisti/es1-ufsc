GERENCIADOR DE CONDOMINIOS

Este projeto e um sistema de gestao de condominios desenvolvido em Java. O software permite a interacao entre o sindico e os condominos para a administracao das rotinas do condominio.

FUNCIONALIDADES

Gestao de utilizadores: Permite gerir os condominos com acesso ao sistema.
Gestao de espacos partilhados: Possibilita a criacao de espacos comuns e a realizacao de reservas pelos condominos, evitando a sobreposicao de horarios.
Avisos e chamados: Sistema de comunicacao para o envio de avisos pelo sindico e a abertura de chamados para manutencao pelos condominos, incluindo o acompanhamento do estado da solicitacao.
Prestacao de contas: Permite a visualizacao das transacoes financeiras e do saldo do condominio.

ESTRUTURA DO CODIGO

O projeto segue o padrao MVC (Model-View-Controller) e esta dividido nos seguintes pacotes principais:

br.ufsc.condominio.model: Contem as entidades do sistema.
br.ufsc.condominio.view: Contem as classes de interface com o utilizador via consola.
br.ufsc.condominio.controller: Contem a logica de controlo e intermediacao.
br.ufsc.condominio.utils: Contem classes para a persistencia de dados.
