package tech.finaya.wallet.domain.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.finaya.wallet.adapter.outbounds.persistence.repositories.WalletRepository;

@Service
public class CreateWallet {
    
    @Autowired
    public WalletRepository repository;

}
