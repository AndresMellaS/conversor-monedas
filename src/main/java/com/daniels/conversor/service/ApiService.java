package com.daniels.conversor.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Component
public class ApiService {
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    @Value("${exchangerate.api.key:}")
    private String apiKey;

    private final HttpClient client;

    // Constructor para modo web — Spring inyecta la key vía @Value
    public ApiService() {
        this.client = HttpClient.newHttpClient();
    }

    // Constructor para modo consola — key se pasa manualmente
    public ApiService(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newHttpClient();
    }

    public double obtenerTasa(String origen, String destino) throws Exception {
        String url = BASE_URL + apiKey + "/latest/" + origen.toUpperCase();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Error al conectar con la API. Código: " + response.statusCode());
        }

        JsonObject json =
                JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject rates = json.getAsJsonObject("conversion_rates");

        if (!rates.has(destino.toUpperCase())) {
            throw new Exception("Moneda no encontrada: " + destino);
        }

        return rates.get(destino.toUpperCase()).getAsDouble();
    }
}










