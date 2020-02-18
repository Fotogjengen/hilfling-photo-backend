package hilfling.backend.hilfing;

import hilfling.backend.hilfing.service.*;
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
