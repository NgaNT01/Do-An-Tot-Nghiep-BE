package uit.streaming.livestreamapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uit.streaming.livestreamapp.repository.UserRepository;

@SpringBootApplication
public class LivestreamappApplication {
	public static void main(String[] args) {
		SpringApplication.run(LivestreamappApplication.class, args);
	}

}
