package my.code.nftmarketplacepublic.controllers.utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI configure() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nft Market Place")
                        .contact(new Contact().name("Fall-23"))
                );
    }
}