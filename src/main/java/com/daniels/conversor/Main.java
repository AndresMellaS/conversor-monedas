package com.daniels.conversor;

import com.daniels.conversor.repository.HistorialRepository;
import com.daniels.conversor.service.ApiService;
import com.daniels.conversor.service.ConversorService;
import com.daniels.conversor.util.Menu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        // Si se pasa el argumento --consola, arranca el menú
        for (String arg : args) {
            if (arg.equals("--consola")) {
                ApiService          apiService = new ApiService();
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
}