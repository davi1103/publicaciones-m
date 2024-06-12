package com.greencomm.Publications.Controller;

import com.greencomm.Publications.Models.Publication;
import com.greencomm.Publications.Repository.PublicationRepository;
import com.greencomm.Publications.Services.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.greencomm.Publications.Jwt.jwtServices;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("")
@RestController
public class Controller {

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private jwtServices jwtutil;

    @Autowired
    private PublicationService publicationService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam("titulo") String titulo,
                                    @RequestParam("descripcion") String descripcion,
                                    @RequestParam("imagen") MultipartFile imagen,
                                    @RequestHeader("Authorization") String authHeader) throws IOException {

        String token = authHeader.substring(7);
        String subject = jwtutil.getUserNameFromToken(token);
        String nombre = publicationService.getUsernameFromEmail(subject);
        try {
            Publication publicacion = publicationService.crearPublicacion(titulo, descripcion, imagen, nombre);
            return ResponseEntity.ok().body(publicacion);
        } catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la imagen");

        }



    }

    @GetMapping("/list")
    public ResponseEntity<List<Publication>> list(){
        List<Publication> lista = publicationRepository.findAllByOrderByFechaDesc();

        return ResponseEntity.ok(lista);
    }
}
