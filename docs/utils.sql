-- transactions out
select u.name, t.amount, t.end_to_end_id, t.status, t.type, t.from_wallet_id, t.to_wallet_id, bh.balance_after, bh.balance_before from users u
inner join wallets w on w.user_id = u.id
-- inner join keys k on k.wallet_id = w.id
inner join transactions t on t.from_wallet_id = w.id
inner join balance_histories bh on bh.transaction_id = t.id and bh.wallet_id = w.id
where u.name = '';

-- transactions in
select u.name, t.amount, t.end_to_end_id, t.status, t.type, t.from_wallet_id, t.to_wallet_id, bh.balance_after, bh.balance_before from users u
inner join wallets w on w.user_id = u.id
-- inner join keys k on k.wallet_id = w.id
inner join transactions t on t.to_wallet_id = w.id
inner join balance_histories bh on bh.transaction_id = t.id and bh.wallet_id = w.id
where u.name = '';
