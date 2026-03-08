package com.daniels.conversor.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Component
public class ApiService {
    private static final String BASE_URL = "https://api.exchangerate-api.com/v4/latest/";

    private final HttpClient client;

    public ApiService() {
        this.client = HttpClient.newHttpClient();
    }

    public double obtenerTasa(String origen, String destino) throws Exception {
        String url = BASE_URL + origen.toUpperCase();

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
        JsonObject rates = json.getAsJsonObject("rates");

        if (!rates.has(destino.toUpperCase())) {
            throw new Exception("Moneda no encontrada" + destino);
        }

        return rates.get(destino.toUpperCase()).getAsDouble();

    }

}










