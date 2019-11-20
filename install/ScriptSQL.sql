CREATE TABLE pessoa (
pessoaId SERIAL,
nome VARCHAR(35),
sobrenome VARCHAR(35),
usuario VARCHAR(35),
senha VARCHAR(35),
PRIMARY KEY (pessoaId)
);

INSERT INTO pessoa (nome, sobrenome, usuario, senha) VALUES ('Rafael', 'Tessarolo', 'rafael', '1234');

CREATE TABLE casa (
casaId SERIAL,
proprietarioId INTEGER,
nome VARCHAR(35),
endereco VARCHAR(100),
cidade VARCHAR(35),
cep VARCHAR(12),
FOREIGN KEY (proprietarioId) REFERENCES pessoa(pessoaId),
PRIMARY KEY (casaID)
);

INSERT INTO casa (proprietarioId, nome, endereco, cidade, cep) VALUES (1, 'Casa da Praia', 'Av Banaguara', 'Fortaleza', '12345678');
INSERT INTO casa (proprietarioId, nome, endereco, cidade, cep) VALUES (1, 'Casa de Valinhos', 'Estrada Municipal', 'Valinhos', '12312455');

CREATE TABLE morador (
moradorId SERIAL,
pessoaId INTEGER,
casaId INTEGER,
data_cadastro DATE,
FOREIGN KEY (pessoaId) REFERENCES pessoa(pessoaId) ON DELETE CASCADE,
FOREIGN KEY (casaId) REFERENCES casa(casaId) ON DELETE CASCADE,
PRIMARY KEY (moradorId)
);

INSERT INTO morador (pessoaId, casaId, data_cadastro) VALUES (1, 1, '2019-08-21');

CREATE TABLE comodo (
comodoId SERIAL,
casaId INTEGER,
nome VARCHAR(35),
andar INTEGER,
FOREIGN KEY (casaId) REFERENCES casa(casaId) ON DELETE CASCADE,
PRIMARY KEY (comodoId)
);

INSERT INTO comodo (casaId, nome, andar) VALUES (1, 'Sala de TV', 1);
INSERT INTO comodo (casaId, nome, andar) VALUES (2, 'Cozinha', 1);

CREATE TABLE aparelho (
aparelhoId SERIAL,
comodoId INTEGER,
nome VARCHAR(35),
descricao VARCHAR(35),
FOREIGN KEY (comodoId) REFERENCES comodo(comodoId) ON DELETE CASCADE,
PRIMARY KEY (aparelhoId)
);

INSERT INTO aparelho (comodoId, nome, descricao) VALUES (1, 'TV 42', 'TV da sala');
INSERT INTO aparelho (comodoId, nome, descricao) VALUES (1, 'Lampada LED', 'Lampada principal da Sala');
INSERT INTO aparelho (comodoId, nome, descricao) VALUES (2, 'TV 32', 'TV da cozinha');

CREATE TABLE rotina (
rotinaId SERIAL,
aparelhoId INTEGER,
dataHora TIMESTAMP,
acao BOOLEAN,
descricao VARCHAR(35),
FOREIGN KEY (aparelhoId) REFERENCES aparelho(aparelhoId) ON DELETE CASCADE,
PRIMARY KEY (rotinaId)
);

INSERT INTO rotina (aparelhoId, dataHora, acao, descricao) VALUES (2, '2019-12-21 16:24:12', true, 'Apagar a luz no dia 21');