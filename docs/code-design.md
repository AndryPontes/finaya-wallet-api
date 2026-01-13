# Code design

## 1. Visão Geral
O Finaya Wallet é um sistema financeiro que gerencia carteiras digitais, transações e chaves Pix. O objetivo é fornecer operações confiáveis de **depósito, saque, transferência Pix intra e Pix out**, com **consistência de saldo, auditoria e idempotência**.

---

## 2. Arquitetura

### 2.1 Camadas
- **Domain**: Modelos de negócio (`Wallet`, `Transaction`, `User`, `Key`, `BalanceHistory`) e regras de consistência.
- **Use Cases**: Serviços transacionais (`MakeDeposit`, `MakeWithdraw`, `MakePix`, `MakePixOut`, `ReceivePixWebhook`, `GetBalance`, `CreateUser`, `CreateKey`, `FindAllUsers`).
- **Persistence**: Repositórios JPA (`WalletRepository`, `TransactionRepository`, `UserRepository`, `KeyRepository`, `BalanceHistoryRepository`, `PixWebhookEventRepository`).
- **Mapper/DTO**: Conversão entre entidades e requests/responses.

### 2.2 Fluxo de Transações Pix
1. `MakePix` → Pix entre carteiras internas, atualiza saldos, cria histórico e transação.
2. `MakePixOut` → Pix externo, bloqueia saldo, prepara transação para integração BACEN.
3. `ReceivePixWebhook` → Confirma ou rejeita Pix externo, liberando ou revertendo saldo bloqueado.

### 2.3 Concorrência e Idempotência
- Uso de **`@Retryable`** com `OptimisticLockingFailureException` para concorrência.
- **Idempotência** garantida por `idempotencyKey` nas transações.
- Bloqueio de saldo (`lockedBalance`) para operações pendentes.

---

## 3. Modelagem de Domínio

### 3.1 Wallet
- Saldo e saldo bloqueado (`balance`, `lockedBalance`).
- Operações: `deposit`, `withdraw`, `lockBalance`, `confirmLockedBalance`, `rejectLockedBalance`.
- Validações: `InsufficientBalanceException` e `AmountIsInvalidException`.
- Chaves Pix (`Key`) e histórico de saldo (`BalanceHistory`).

### 3.2 Transaction
- Estados: `PendingState`, `ConfirmedState`, `RejectedState`.
- Tipos: `DEPOSIT`, `WITHDRAW`, `PIX`.
- Transições de estado: `confirm()` e `reject()`.

### 3.3 User
- Vinculado a uma wallet.
- Controle de ativo/inativo.

---

## 4. Casos de Uso

| Caso de Uso            | Descrição                                                                 |
|------------------------|---------------------------------------------------------------------------|
| MakeDeposit             | Deposita saldo na carteira e registra histórico.                           |
| MakeWithdraw            | Realiza saque, valida saldo, cria histórico e transação.                  |
| MakePix                 | Transfere saldo entre wallets internas, garantindo consistência.          |
| MakePixOut              | Bloqueia saldo para Pix externo, gera transação e histórico.              |
| ReceivePixWebhook       | Atualiza transação externa com status CONFIRMED ou REJECTED.              |
| GetBalance              | Retorna saldo atual ou histórico de saldo em um ponto no tempo.           |
| CreateUser              | Cria usuário com wallet vinculada, valida CPF único.                      |
| CreateKey               | Adiciona chave Pix à wallet, valida duplicidade por tipo e valor.         |
| FindAllUsers            | Lista todos os usuários cadastrados.                                       |

---

## 5. Auditoria e Histórico
- `BalanceHistory` registra cada alteração de saldo, mantendo rastreabilidade completa.
- `Transaction` registra todas as operações com status e tipo, garantindo consistência financeira.

---

## 6. Confiabilidade
- **Transações atômicas** via `@Transactional`.
- **Idempotência** e **retry** para falhas concorrentes.
- **Validações robustas** de saldo e valores negativos.
- **Estados de transação** para prevenir alterações inconsistentes.

---

## 7. Tempo Investido
O projeto demandou aproximadamente **40~50 horas**, cobrindo análise de requisitos, modelagem de domínio, definição da arquitetura e implementação de fluxos Pix (intra e externo). Foram aplicados mecanismos de **concorrência, idempotência e auditoria**, garantindo consistência financeira mesmo diante de eventos duplicados ou fora de ordem. Todo o esforço focou em entregar um sistema robusto, previsível e alinhado a boas práticas de engenharia de software.

---

## 8. Tecnologias
- Java 21
- Spring Boot
- JPA/Hibernate
- Banco de dados relacional (PostgreSQL)
- Testes com jUnit e testcontainer

---

## 9. Links
- [README](./../README.md)
- [Trade-Offs](./trade-offs.md)
