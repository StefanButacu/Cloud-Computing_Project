//package com.example.ccbe.controller;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//
//@RestController
//@CrossOrigin(origins = "*")
//@RequestMapping("/category")
//public class CategoryController {
//
//    @Value(value = "${image.api.key}")
//    private String IMAGE_API_KEY;
//    @Value(value = "${python.server.api}")
//    private String PYTHON_API;
//
//    @GetMapping()
//    public String getCategories() throws IOException, InterruptedException {
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(PYTHON_API + "/category"))
//                .header("x-api-key", IMAGE_API_KEY)
//                .GET()
//                .build();
//
//        // Send the HTTP request
//        HttpClient client = HttpClient.newHttpClient();
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        return response.body();
//    }
//
//
//    @GetMapping("/{id}")
//    public String getCategory(@PathVariable("id") String id) throws IOException, InterruptedException {
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(PYTHON_API + "/category/" + id))
//                .header("x-api-key", IMAGE_API_KEY)
//                .GET()
//                .build();
//
//        // Send the HTTP request
//        HttpClient client = HttpClient.newHttpClient();
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        return response.body();
//    }
//
//}
