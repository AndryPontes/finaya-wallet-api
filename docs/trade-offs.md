# Trade‑offs

## 1. Visão Geral
Durante o desenvolvimento do Finaya Wallet, algumas decisões técnicas foram tomadas com **foco em entrega rápida e MVP funcional**.  
Isso implicou **trade-offs** entre complexidade, robustez, performance e abrangência de funcionalidades.

---

## 2. Principais Trade-offs

| Área                  | Decisão Adotada                                    | Compromisso / Limitação                             | Risco / Consequência |
|-----------------------|---------------------------------------------------|---------------------------------------------------|--------------------|
| Persistência          | Uso de **JPA/Hibernate direto**                   | Não há camada explícita de CQRS                   | Possível overhead em consultas complexas |
| Transações            | `@Transactional` nos casos de uso críticos       | Não implementado rollback detalhado para cada operação | Pode haver inconsistência parcial se exceções externas ocorrerem |
| Concurrency            | **Optimistic Locking** via `@Version`            | Não há lock pessimista                             | Em cenários de altíssima concorrência, pode gerar conflitos e retries |
| Pix Externo            | Bloqueio de saldo + webhook simples              | Não há retry automático externo sofisticado       | Possível falha de comunicação com BACEN leva a saldo bloqueado temporariamente |
| Validações            | Implementadas no Domain (`Wallet`, `Transaction`) | Não há regras avançadas de antifraude            | Regras manuais necessárias em produção |
| Estados de Transação   | State pattern simples (Pending, Confirmed, Rejected) | Não cobre todos os casos de erro intermediário    | Pode exigir ajustes futuros para casos de edge |
| Testes                 | Testes unitários e integração básica             | Não há cobertura completa de carga ou falhas externas | Potencial de bugs não detectados em alta escala |
| Logs & Auditoria       | Histórico de saldo (`BalanceHistory`)            | Não há logs detalhados de todas as ações          | Diagnóstico de problemas mais complexo em produção |
| Chaves Pix             | Única chave por tipo, verificação simples        | Não há suporte avançado de múltiplas chaves do mesmo tipo | Limitação para usuários com múltiplos canais Pix |

---

## 3. Compromissos Assumidos

1. **Foco em MVP funcional**: Entregar operações básicas de depósito, saque, Pix interno e Pix externo com consistência de saldo.
2. **Idempotência mínima**: Apenas `idempotencyKey` nas transações Pix.
3. **Auditabilidade básica**: Rastreabilidade de saldo via `BalanceHistory`.
4. **Performance suficiente para escala inicial**: Preparado para volume moderado, mas sem otimizações avançadas de query ou caching.
5. **Segurança básica**: Validações de valores, saldo e CPF único, sem antifraude avançado.

---

## 4. Possíveis Melhorias Futuras

- Implementar **CQRS** para separação de leitura/escrita, aumentando performance.
- Adicionar **retry e fallback inteligente** em Pix externo.
- Implementar **lock pessimista** para cenários de altíssima concorrência.
- Cobrir **testes de carga e integração com BACEN**.
- Adicionar **antifraude e monitoramento detalhado de transações**.
- Melhorar logs e métricas de auditoria em tempo real.

---

## 5. Conclusão

As decisões tomadas priorizam **velocidade de entrega e confiabilidade mínima**, permitindo que o sistema funcione de forma consistente para MVP, enquanto abre espaço para **otimizações e reforço de robustez** em versões futuras.

> Em suma: foi um **compromisso consciente entre tempo x robustez x complexidade**, garantindo funcionamento seguro, previsível e rastreável para operações financeiras essenciais.

---

## 6. Links

- [README](./../README.md)
- [Code Design](./code-design.md)