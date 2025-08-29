package next.career;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class NextcareerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NextcareerApplication.class, args);
	}

}
