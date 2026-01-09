package tech.finaya.wallet.adapter.inbounds.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.finaya.wallet.domain.usecases.CreateUser;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    
    @Autowired
    public CreateUser createWallet;

}
