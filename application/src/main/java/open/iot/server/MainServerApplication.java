package open.iot.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableAsync
@EnableSwagger2
@SpringBootApplication(scanBasePackages = {"open.iot.server"})
public class MainServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainServerApplication.class);
    }
}
