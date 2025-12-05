package com.doculens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "DocuLens API",
        version = "1.0.0",
        description = "DocuLens Backend API, enabling efficient administrative workflows.",
        contact = @Contact(
            name = "DocuLens Support",
            email = "doculens.library@gmail.com"
        ),
        license = @License(name = "MIT", url = "https://opensource.org/licenses/MIT")
    )
)
public class DoculensApplication {
	public static void main(String[] args) {
		SpringApplication.run(DoculensApplication.class, args);
	}

}
