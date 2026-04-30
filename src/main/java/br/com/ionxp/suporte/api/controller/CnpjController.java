package br.com.ionxp.suporte.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/cnpj")
public class CnpjController {

	private final RestTemplate restTemplate;

    public CnpjController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{cnpj}")
    public ResponseEntity<Object> buscarCnpj(@PathVariable String cnpj) {
        String url = "https://api.opencnpj.org/" + cnpj;
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("CNPJ não encontrado");
        }
    }
}
