package gm.inventoryproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot.
 * <p>
 * Punto de entrada para iniciar el contexto de Spring y levantar
 * la aplicación del sistema de inventario.
 * </p>
 */
@SpringBootApplication
public class InventoryprojectApplication {

	/**
	 * Inicia la aplicación Spring Boot.
	 * @param args Argumentos de línea de comandos (no utilizados).
	 */
	public static void main(String[] args) {
		SpringApplication.run(InventoryprojectApplication.class, args);
	}

}
