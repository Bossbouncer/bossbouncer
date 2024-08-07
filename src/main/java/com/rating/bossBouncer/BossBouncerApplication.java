package com.rating.bossbouncer;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableEncryptableProperties
//@EnableConfigurationProperties(AppProperties.class)
public class BossBouncerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BossBouncerApplication.class, args);
	}

}
