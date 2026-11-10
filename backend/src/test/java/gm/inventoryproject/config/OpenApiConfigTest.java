package gm.inventoryproject.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para OpenApiConfig.
 * <p>
 * Verifica que la configuración de OpenAPI devuelva el título y versión correctos.
 * </p>
 */
@DisplayName("Tests de OpenApiConfig")
class OpenApiConfigTest {

    @Test
    @DisplayName("customOpenAPI devuelve título correcto")
    void testCustomOpenAPITitle() {
        // Arrange
        OpenApiConfig config = new OpenApiConfig();

        // Act
        OpenAPI openAPI = config.customOpenAPI();

        // Assert
        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("CN Inventory App", openAPI.getInfo().getTitle());
    }

    @Test
    @DisplayName("customOpenAPI devuelve versión correcta")
    void testCustomOpenAPIVersion() {
        // Arrange
        OpenApiConfig config = new OpenApiConfig();

        // Act
        OpenAPI openAPI = config.customOpenAPI();

        // Assert
        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("1.0", openAPI.getInfo().getVersion());
    }

    @Test
    @DisplayName("customOpenAPI devuelve descripción correcta")
    void testCustomOpenAPIDescription() {
        // Arrange
        OpenApiConfig config = new OpenApiConfig();

        // Act
        OpenAPI openAPI = config.customOpenAPI();

        // Assert
        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("Documentación de la API del proyecto sistema de inventario", openAPI.getInfo().getDescription());
    }
}
