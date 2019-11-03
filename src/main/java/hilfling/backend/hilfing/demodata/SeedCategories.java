package hilfling.backend.hilfing.demodata;

import hilfling.backend.hilfing.model.Category;
import hilfling.backend.hilfing.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SeedCategories {
   @Autowired
   private CategoryService service;

   @EventListener
   public void seed(ApplicationReadyEvent event) {
      List<Category> categories = new ArrayList<Category>(){
         {
            add(new Category("Fotostand"));
            add(new Category("Konsert"));
            add(new Category("Miljøbilder"));
            add(new Category("Onsdagsdebatt"));
            add(new Category("Bilder av Sindre"));
         }
      };
      categories.stream().forEach(category -> this.service.createCategory(category));
}}
