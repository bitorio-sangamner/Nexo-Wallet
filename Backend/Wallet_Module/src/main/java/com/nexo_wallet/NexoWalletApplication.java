package com.nexo_wallet;

import com.nexo_wallet.entity.PlatformCurrencies;
import com.nexo_wallet.entity.User;
import com.nexo_wallet.entity.Wallet;
import com.nexo_wallet.repositories.PlatformCurrencyRepository;
import com.nexo_wallet.repositories.UserOwnCurrenciesRepository;
import com.nexo_wallet.repositories.UserRepository;
import com.nexo_wallet.repositories.WalletRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
@Slf4j
public class NexoWalletApplication {

	@Autowired
	private PlatformCurrencyRepository platformCurrencyRepository;

	@Autowired
	private UserOwnCurrenciesRepository userOwnCurrenciesRepository;
	@Autowired
	private WalletRepository walletRepository;
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(NexoWalletApplication.class, args);
	}

	@PostConstruct
	public void init() {
//		PlatformCurrencies platformCurrencies=PlatformCurrencies.builder()
//				.currencyName("Litecoin")
//				.symbol("LTC")
//				.marketPrice(null)
//				.build();
//
//		platformCurrencyRepository.save(platformCurrencies);



//		Wallet wallet=walletRepository.findWalletWithUserOwnCurrenciesById(12);
//
//		log.info("Wallet Id :"+wallet.getWalletId());
//		log.info("Wallet user email :"+wallet.getUser().getEmail());
//		log.info("Wallet user currencies:"+wallet.getUserOwnCurrenciesList().get(0).getCurrencyName());

//      Optional<User> optionaluser=userRepository.findById(11);
//		User user=optionaluser.get();
//		log.info("user wallet id :"+user.getWallet().getWalletId());
//		log.info("user email :"+user.getEmail());
//		log.info("user craeted on:"+user.getCreatedOn());
		//log.info("user craeted on:"+user.getWallet().getUserOwnCurrenciesList());
	}


}
