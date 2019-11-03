package hilfling.backend.hilfing.demodata;

import hilfling.backend.hilfing.model.Album;
import hilfling.backend.hilfing.model.Category;
import hilfling.backend.hilfing.service.AlbumService;
import hilfling.backend.hilfing.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SeedAlbums {
   @Autowired
   private AlbumService service;

   @EventListener
   public void seed(ApplicationReadyEvent event) {
      Integer num_of_albums = 12;
      for (Integer i = 0; i < num_of_albums; i++) {
         service.createAlbum(new Album(String.format("Album nr: %d",i),false) );
      }
}}
