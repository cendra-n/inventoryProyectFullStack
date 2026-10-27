package gm.inventoryproject.config;


import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración personalizada de OpenAPI/Swagger para la documentación de la API.
 * <p>
 * Define la información general de la API que se muestra en la interfaz
 * de Swagger UI, incluyendo título, versión y descripción.
 * </p>
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configura la instancia personalizada de OpenAPI.
     * @return Objeto OpenAPI con la información de la API.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CN Inventory App")
                        .version("1.0")
                        .description("Documentación de la API del proyecto sistema de inventario"));
    }
}

