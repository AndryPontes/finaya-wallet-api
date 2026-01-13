## TODO

### Requisitos Funcionais

- [x] Criar Conta/Carteira: criação de carteira para um usuário.
- [x] Registrar Chave Pix: vincular uma chave Pix única à carteira (e.g.,email/telefone/EVP).​
- [x] Consultar Saldo: saldo atual da carteira.​
- [x] Saldo Histórico: saldo em um timestamp passado.​
- [x] Depósito: crédito na carteira (simula entrada de recursos).​
- [x] Saque: débito na carteira (valida saldo).​
- [x] Transferência Pix (interna): enviar Pix para outra carteira usando chave Pix (gera endToEndId).​
- [x] Webhook Pix (simulado): endpoint que recebe eventos CONFIRMED/REJECTED para um endToEndId. Eventos podem chegar duplicados e fora de ordem.

### Requisitos Não Funcionais

- [x] Missão crítica: evite inconsistências; efeito exactly-once no débito.​
- [x] Rastreabilidade/Auditoria: trilha completa de operações (ledger/eventos).​
- [x] Concorrência: o sistema deve resistir a requisições simultâneas do mesmo Pix.​
- [x] Idempotência​
- [x] Observabilidade: logs estruturados e métricas mínimas.​​

### Cenários-Chave (Concorrência & Race)

- [x] Duplo disparo: duas requisições simultâneas de transferência Pix com mesmo Idempotency-Key e/ou mesmo endToEndId ⇒ um único débito efetivo.​
- [x] Webhook duplicado: vários POST /pix/webhook com mesmo eventId ⇒ aplicar uma vez.​
- [x] Ordem trocada: REJECTED chegando antes de CONFIRMED ⇒ respeitar máquina de estados e manter consistência.​
- [x] Reprocesso: reexecutar o processamento do mesmo evento (simulate “at least once”) sem mudar o saldo final.​

### Sugestão de Endpoints (contratos simples)

- [x] POST /wallets → cria carteira.​ (/api/users → ao criar um usuario uma carteira e criada automaticamente)
- [x] POST /wallets/{id}/pix-keys → registra chave Pix.​ (/api/wallets/{wallet_id}/key)
- [x] GET /wallets/{id}/balance → saldo atual.​ (/api/wallets/{wallet_id}/balance)
- [x] GET /wallets/{id}/balance?at=2025-10-09T15:00:00Z → saldo histórico.​ (/api/wallets/{wallet_id}/balance?at=2000-10-31T01%3A30%3A00.000-05%3A00)
- [x] POST /wallets/{id}/deposit → depósito.​ (/api/wallets/{wallet_id}/deposit)
- [x] POST /wallets/{id}/withdraw → saque.​ ((/api/wallets/{wallet_id}/withdraw))
- [x] POST /pix/transfers​ (/api/intra/pix)
        Headers: Idempotency-Key: <uuid>
        ​Body: { fromWalletId, toPixKey, amount }​
        Resposta: { endToEndId, status }​
- [x] POST /pix/webhook​ (criacao de pix out → /api/pix/webhook e recebida de evento de pix criado → /api/pix/webhook)
        Body: { endToEndId, eventId, eventType, occurredAt }​
        Idempotente por eventId.​

        Obs.: Não precisa integrar ao Bacen; o “webhook” simula a confirmação do arranjo Pix.

### Entregáveis (GitHub)

- [x] Implementação do microserviço.​
- [x] Instruções para instalar, testar e executar (README claro; Docker Compose do Postgres opcional).​
- [ ] Explicação das decisões de design e como atendem aos requisitos (funcionais e não-funcionais).​
- [ ] Explicação de trade-offs/compromissos por limite de tempo.​
- [x] Testes unitários e testes integrados​

### Critérios de Avaliação (alto nível)

- [x] Corretude & Consistência (efeito único, estados válidos, ledger fechando).​
- [x] Concorrência & Falhas (prevenção de corrida, idempotência demonstrada).​
- [x] Qualidade de Arquitetura & Código (Clean Architecture, coesão, clareza).​
- [x] Observabilidade & Auditabilidade (logs/metrics e trilha de eventos).​
- [x] Documentação & Execução (README, como subir/rodar/testar).​

### Pistas Técnicas (opcionais, escolha sua abordagem)

- [x] Idempotência: tabela idempotency(scope,key) com unique; retornar o mesmo resultado ao detectar repetição.​
- [x] Race condition: optimistic locking (version) ou pessimista (SELECT FOR UPDATE) em saldo/ledger; retries curtos.​
- [x] Ledger: entradas imutáveis (+/−) vinculadas ao endToEndId; saldo derivado ou mantido com locking.​
- [x] Máquina de estados: PENDING → CONFIRMED → (SETTLED opcional) ou PENDING → REJECTED.​
- [x] Observabilidade: log com endToEndId, eventId, idempotencyKey.
