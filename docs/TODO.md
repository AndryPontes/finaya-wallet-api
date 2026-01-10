## TODO

### Requisitos Funcionais

- [x] Criar Conta/Carteira: criação de carteira para um usuário.
- [x] Registrar Chave Pix: vincular uma chave Pix única à carteira (e.g.,email/telefone/EVP).​
- [x] Consultar Saldo: saldo atual da carteira.​
- [x] Saldo Histórico: saldo em um timestamp passado.​
- [ ] Depósito: crédito na carteira (simula entrada de recursos).​
- [ ] Saque: débito na carteira (valida saldo).​
- [ ] Transferência Pix (interna): enviar Pix para outra carteira usando chave Pix (gera endToEndId).​
- [ ] Webhook Pix (simulado): endpoint que recebe eventos CONFIRMED/REJECTED para um endToEndId. Eventos podem chegar duplicados e fora de ordem.

### Requisitos Não Funcionais

- [ ] Missão crítica: evite inconsistências; efeito exactly-once no débito.​
- [ ] Rastreabilidade/Auditoria: trilha completa de operações (ledger/eventos).​
- [ ] Concorrência: o sistema deve resistir a requisições simultâneas do mesmo Pix.​
- [ ] Idempotência​
- [ ] Observabilidade: logs estruturados e métricas mínimas.​​

### Cenários-Chave (Concorrência & Race)

- [ ] Duplo disparo: duas requisições simultâneas de transferência Pix com mesmo Idempotency-Key e/ou mesmo endToEndId ⇒ um único débito efetivo.​
- [ ] Webhook duplicado: vários POST /pix/webhook com mesmo eventId ⇒ aplicar uma vez.​
- [ ] Ordem trocada: REJECTED chegando antes de CONFIRMED ⇒ respeitar máquina de estados e manter consistência.​
- [ ] Reprocesso: reexecutar o processamento do mesmo evento (simulate “at least once”) sem mudar o saldo final.​

### Sugestão de Endpoints (contratos simples)

- [x] POST /wallets → cria carteira.​ (/api/users → ao criar um usuario uma carteira e criada automaticamente)
- [x] POST /wallets/{id}/pix-keys → registra chave Pix.​ (/api/wallets/{wallet_id}/key)
- [x] GET /wallets/{id}/balance → saldo atual.​ (/api/wallets/{wallet_id}/balance)
- [x] GET /wallets/{id}/balance?at=2025-10-09T15:00:00Z → saldo histórico.​ (/api/wallets/{wallet_id}/balance?at=2000-10-31T01%3A30%3A00.000-05%3A00)
- [ ] POST /wallets/{id}/deposit → depósito.​
- [ ] POST /wallets/{id}/withdraw → saque.​
- [ ] POST /pix/transfers​
        Headers: Idempotency-Key: <uuid>​Body: { fromWalletId, toPixKey, amount }​
        Resposta: { endToEndId, status }​
- [ ] POST /pix/webhook​
        Body: { endToEndId, eventId, eventType, occurredAt }​
        Idempotente por eventId.​

        Obs.: Não precisa integrar ao Bacen; o “webhook” simula a confirmação do arranjo Pix.

### Entregáveis (GitHub)

- [ ] Implementação do microserviço.​
- [ ] Instruções para instalar, testar e executar (README claro; Docker Compose do Postgres opcional).​
- [ ] Explicação das decisões de design e como atendem aos requisitos (funcionais e não-funcionais).​
- [ ] Explicação de trade-offs/compromissos por limite de tempo.​
- [ ] Testes unitários e testes integrados​

### Critérios de Avaliação (alto nível)

- [ ] Corretude & Consistência (efeito único, estados válidos, ledger fechando).​
- [ ] Concorrência & Falhas (prevenção de corrida, idempotência demonstrada).​
- [ ] Qualidade de Arquitetura & Código (Clean Architecture, coesão, clareza).​
- [ ] Observabilidade & Auditabilidade (logs/metrics e trilha de eventos).​
- [ ] Documentação & Execução (README, como subir/rodar/testar).​

### Pistas Técnicas (opcionais, escolha sua abordagem)

- [ ] Idempotência: tabela idempotency(scope,key) com unique; retornar o mesmo resultado ao detectar repetição.​
- [ ] Race condition: optimistic locking (version) ou pessimista (SELECT FOR UPDATE) em saldo/ledger; retries curtos.​
- [ ] Ledger: entradas imutáveis (+/−) vinculadas ao endToEndId; saldo derivado ou mantido com locking.​
- [ ] Máquina de estados: PENDING → CONFIRMED → (SETTLED opcional) ou PENDING → REJECTED.​
- [ ] Observabilidade: log com endToEndId, eventId, idempotencyKey.
