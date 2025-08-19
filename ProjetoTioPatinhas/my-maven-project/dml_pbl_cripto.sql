-- declarar bind variables
VARIABLE id_conta NUMBER
VARIABLE id_conta_delete NUMBER
VARIABLE id_carteira NUMBER
VARIABLE id_carteira_del NUMBER
VARIABLE id_ativo NUMBER
VARIABLE id_segundo_ativo NUMBER
VARIABLE id_ativo_del NUMBER
VARIABLE id_ordem_compra NUMBER
VARIABLE id_ordem_venda NUMBER
VARIABLE id_ordem_del NUMBER
VARIABLE id_transacao NUMBER
VARIABLE id_transacao_del NUMBER

-- 1) Cria a conta base (PESSOAL)
INSERT INTO T_CONTA (nome, email, senha, telefone, saldo, tipo_conta)
VALUES ('João da Silva', 'joao@email.com', 'hash_da_senha', '(11) 99999-0000', 500, 'PESSOAL')
RETURNING id_conta INTO :id_conta;

-- 2) Cria os subtipos
INSERT INTO T_CONTA_PESSOAL (id_conta, cpf)
VALUES (:id_conta, '12345678901');

INSERT INTO T_CONTA_EMPRESARIAL (id_conta, cnpj) 
VALUES (:id_conta, '12345678912345');

-- READ: Seleciona os dados da conta recém-criada
SELECT 
  c.id_conta,
  c.nome,
  c.email,
  c.telefone,
  c.saldo,
  c.tipo_conta,
  p.cpf,
  e.cnpj
FROM T_CONTA c
LEFT JOIN T_CONTA_PESSOAL p ON p.id_conta = c.id_conta
LEFT JOIN T_CONTA_EMPRESARIAL e ON e.id_conta = c.id_conta
WHERE c.id_conta = :id_conta;

-- 3) Cria outra conta (EMPRESARIAL)
INSERT INTO T_CONTA (nome, email, senha, telefone, saldo, tipo_conta)
VALUES ('vitor', 'vitor@email.com', 'hash_da_senha', '(11) 99999-0000', 100, 'EMPRESARIAL')
RETURNING id_conta INTO :id_conta_delete;

INSERT INTO T_CONTA_PESSOAL (id_conta, cpf)
VALUES (:id_conta_delete, '12345678902');

INSERT INTO T_CONTA_EMPRESARIAL (id_conta, cnpj) 
VALUES (:id_conta_delete, '12345678912346');

SELECT :id_conta_delete AS id FROM dual;

-- UPDATE CONTA
UPDATE T_CONTA
SET saldo = 0
WHERE id_conta = :id_conta_delete;

-- DELETE CONTA e FKs

DELETE FROM T_CONTA
WHERE id_conta = :id_conta_delete;

-- CARTEIRA

INSERT INTO T_CARTEIRA (id_conta, saldo)
VALUES (:id_conta, 500)
RETURNING id_carteira INTO :id_carteira;

INSERT INTO T_CARTEIRA (id_conta, saldo)
VALUES (:id_conta, 600)
RETURNING id_carteira INTO :id_carteira_del;

SELECT * FROM T_CARTEIRA WHERE id_carteira = :id_carteira;

UPDATE T_CARTEIRA
SET saldo = 0
WHERE id_carteira = :id_carteira_del;


DELETE FROM T_CARTEIRA WHERE id_carteira = :id_carteira_del;

-- ATIVO
INSERT INTO T_ATIVO (nome, simbolo, cotacao)
VALUES ('Bitcoin','BTC', 100000.00)
RETURNING id_ativo INTO :id_ativo;

INSERT INTO T_ATIVO (nome, simbolo, cotacao)
VALUES ('Ethereum','ETH', 25000.00)
RETURNING id_ativo INTO :id_segundo_ativo;

INSERT INTO T_ATIVO (nome, simbolo, cotacao)
VALUES ('Tether','USDT', 5.39)
RETURNING id_ativo INTO :id_ativo_del;


SELECT * FROM T_ATIVO WHERE id_ativo = :id_ativo;

UPDATE T_ATIVO 
SET cotacao = 5.5
WHERE id_ativo = :id_ativo_del;

DELETE FROM T_ATIVO WHERE id_ativo = :id_ativo_del;

-- CONTA_ATIVO

INSERT INTO T_CARTEIRA_ATIVO (id_carteira,id_ativo, quantidade, preco_medio, data_primeira_compra)
VALUES (:id_carteira, :id_ativo, 2, 100000, TO_DATE('2025-08-15', 'YYYY-MM-DD'));

INSERT INTO T_CARTEIRA_ATIVO (id_carteira,id_ativo, quantidade, preco_medio, data_primeira_compra)
VALUES (:id_carteira, :id_segundo_ativo, 2, 500000, TO_DATE('2025-08-13', 'YYYY-MM-DD'));

SELECT * FROM T_CARTEIRA_ATIVO WHERE id_carteira = :id_carteira and id_ativo = :id_ativo;

UPDATE T_CARTEIRA_ATIVO 
SET data_primeira_compra = TO_DATE('2025-08-11', 'YYYY-MM-DD')
WHERE id_carteira = :id_carteira AND id_ativo = :id_segundo_ativo;

DELETE FROM T_CARTEIRA_ATIVO
WHERE id_carteira = :id_carteira
  AND id_ativo    = :id_segundo_ativo;

-- ORDEM 

INSERT INTO T_ORDEM (id_ativo, id_carteira, tipo, status, quantidade, preco)
VALUES (:id_ativo, :id_carteira, 'compra', 'executada', 2, 100000)
RETURNING id_ordem INTO :id_ordem_compra;

INSERT INTO T_ORDEM (id_ativo, id_carteira, tipo, status, quantidade, preco)
VALUES (:id_ativo, :id_carteira, 'venda', 'pendente', 2, 500000)
RETURNING id_ordem INTO :id_ordem_venda;


INSERT INTO T_ORDEM (id_ativo, id_carteira, tipo, status, quantidade, preco)
VALUES (:id_segundo_ativo, :id_carteira, 'venda', 'pendente', 2, 100000)
RETURNING id_ordem INTO :id_ordem_del;

SELECT * FROM T_ORDEM WHERE id_ordem = :id_ordem_del;

UPDATE T_ORDEM
SET status = 'executada'
WHERE id_ordem = :id_ordem_venda;

DELETE FROM T_ORDEM WHERE id_ordem = :id_ordem_del;

-- TRANSACAO 

INSERT INTO T_TRANSACAO (id_ordem_compra, id_ordem_venda, id_ativo, quantidade, preco, taxa)
VALUES ( :id_ordem_compra, :id_ordem_venda, :id_ativo, 10, 1000000,  0.05)
RETURNING id_transacao INTO :id_transacao;

INSERT INTO T_TRANSACAO (id_ordem_compra, id_ordem_venda, id_ativo, quantidade, preco, taxa)
VALUES ( :id_ordem_compra, :id_ordem_venda, :id_ativo, 5, 1000000,  0.06)
RETURNING id_transacao INTO :id_transacao_del;

SELECT * FROM T_TRANSACAO WHERE id_transacao = :id_transacao;

UPDATE T_TRANSACAO 
SET taxa = 0.06
WHERE id_transacao = :id_transacao;

DELETE FROM T_TRANSACAO WHERE id_transacao = :id_transacao_del;


COMMIT;