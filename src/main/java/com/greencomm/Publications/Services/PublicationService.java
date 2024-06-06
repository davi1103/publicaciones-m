package com.greencomm.Publications.Services;

import com.greencomm.Publications.Models.Publication;
import com.greencomm.Publications.Repository.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Service
public class PublicationService {

    @Autowired
    private PublicationRepository publicationRepository;


    public String getUsernameFromEmail(String email) {
        return email.split("@")[0];
    }

    public Publication crearPublicacion(String titulo, String descripcion, MultipartFile imagen, String nombre) throws IOException {

        Publication publication = new Publication();

        publication.setTitulo(titulo);
        publication.setDescripcion(descripcion);
        publication.setAutor(nombre);
        publication.setFecha(LocalDate.now());
        publication.setHora(LocalTime.now());



        if (imagen != null && !imagen.isEmpty()) {
            System.out.println("Tipo de dato de 'imagen': " + imagen.getBytes());
            publication.setImagen(imagen.getBytes());


        } else {

            publication.setImagen(null);
            assert imagen != null;
            System.out.println("Tipo de dato de 'imagen': " + imagen.getBytes().getClass().getName());

        }

        publicationRepository.save(publication);
        return publication;



    }
}
