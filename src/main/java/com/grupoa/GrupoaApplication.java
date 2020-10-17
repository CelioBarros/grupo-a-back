package com.grupoa;

import com.grupoa.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
@EnableJpaAuditing
public class GrupoaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrupoaApplication.class, args);
	}

}
