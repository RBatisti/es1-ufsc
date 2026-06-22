-- Banco de dados de ES1 - ConDom, o gerenciador de condominios

-- Tabela de referência para os tipos de usuário
CREATE TABLE tipo_usuario (
    id_tipo_usuario SERIAL PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO tipo_usuario (descricao) VALUES 
('Sindico'), ('Condômino');

-- Tabela de referência para o gênero
CREATE TABLE genero (
    id_genero SERIAL PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO genero (descricao) VALUES 
('Feminino'), ('Masculino'), ('Outro'), ('Prefiro não dizer');

-- Tabela de referência para status
CREATE TABLE status (
    id_status SERIAL PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO status (descricao) VALUES 
('Não Iniciado'), ('Em Andamento'), ('Pronto');


-- Tabela de Usuário
CREATE TABLE usuario (
    cpf VARCHAR(14) PRIMARY KEY,
    nome VARCHAR,
    data_nascimento DATE,
    id_genero INT,
    senha VARCHAR,
    email VARCHAR,
    id_tipo_usuario INT,
    unidade INT,
    data_cadastro DATE,
    CONSTRAINT fk_usuario_genero FOREIGN KEY (id_genero) REFERENCES genero (id_genero),
    CONSTRAINT fk_usuario_tipo FOREIGN KEY (id_tipo_usuario) REFERENCES tipo_usuario (id_tipo_usuario)
);

CREATE TABLE notificacao (
    id_notificacao SERIAL PRIMARY KEY,
    mensagem TEXT,
    data TIMESTAMP,
    categoria VARCHAR,
    tipo VARCHAR,
    cpf_criador VARCHAR(14),
    CONSTRAINT fk_notificacao_usuario FOREIGN KEY (cpf_criador) REFERENCES usuario (cpf)
);

CREATE TABLE aviso (
    id_aviso SERIAL PRIMARY KEY,
    id_notificacao INT,
    CONSTRAINT fk_aviso_notificacao FOREIGN KEY (id_notificacao) REFERENCES notificacao (id_notificacao)
);

-- Tabela de ligação aviso_destinatário
CREATE TABLE aviso_destinatario (
    id_aviso INT,
    cpf_destinatario VARCHAR(14),
    visualizado BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_aviso_destinatario_aviso FOREIGN KEY (id_aviso) REFERENCES aviso (id_aviso),
    CONSTRAINT fk_aviso_destinatario_usuario FOREIGN KEY (cpf_destinatario) REFERENCES usuario (cpf),
    PRIMARY KEY (id_aviso, cpf_destinatario)
);

CREATE TABLE chamado (
    id_chamado SERIAL PRIMARY KEY,
    id_status INT,
    cpf_condomino VARCHAR(14),
    visualizado_sindico BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_chamado_status FOREIGN KEY (id_status) REFERENCES status (id_status),
    CONSTRAINT fk_chamado_usuario FOREIGN KEY (cpf_condomino) REFERENCES usuario (cpf)
);