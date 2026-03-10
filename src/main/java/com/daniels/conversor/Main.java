package com.daniels.conversor;

import com.daniels.conversor.repository.HistorialRepository;
import com.daniels.conversor.service.ApiService;
import com.daniels.conversor.service.ConversorService;
import com.daniels.conversor.util.Menu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        // Si se pasa el argumento --consola, arranca el menú
        for (String arg : args) {
            if (arg.equals("--consola")) {
                // Leer la key desde application.properties
                String apiKey = cargarApiKey();
                ApiService          apiService = new ApiService(apiKey);
                ConversorService    conversor  = new ConversorService(apiService);
                HistorialRepository historial  = new HistorialRepository();
                Menu                menu       = new Menu(conversor, historial);
                menu.iniciar();
                return;
            }
        }

        // Sin argumentos arranca Spring Boot (modo web)
        SpringApplication.run(Main.class, args);
    }

    private static String cargarApiKey() {
        Properties props = new Properties();
        try (InputStream input = Main.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer application.properties: " + e.getMessage());
        }
        String key = props.getProperty("exchangerate.api.key", "");
        if (key.isBlank()) {
            System.err.println("⚠️  API key no configurada en application.properties");
        }
        return key;
    }
}