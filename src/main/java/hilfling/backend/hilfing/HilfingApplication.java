package hilfling.backend.hilfing;

import hilfling.backend.hilfing.demodata.SeedCategories;
import hilfling.backend.hilfing.model.*;
import hilfling.backend.hilfing.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HilfingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HilfingApplication.class, args);
	}
}
