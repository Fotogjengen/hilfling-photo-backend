package hilfling.backend.hilfling;

import hilfling.backend.hilfling.configurations.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class HilflingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HilflingApplication.class, args);
    }
}
