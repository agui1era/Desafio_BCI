// bci/Application.java
package bci.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // <--- ¡Asegúrate de que esta anotación esté aquí!
public class Application { // O el nombre que le hayas dado a tu clase principal
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}