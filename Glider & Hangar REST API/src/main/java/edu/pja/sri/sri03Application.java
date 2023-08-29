package edu.pja.sri;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class sri03Application {

	@Bean
	 public ModelMapper modelMapper() {
		 return new ModelMapper();
	 }

	public static void main(String[] args) {
		SpringApplication.run(sri03Application.class, args);
	}

}


