package org.upb.project;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.upb.project.services.LoadAndRun;

@SpringBootApplication
@EnableJms
@EnableScheduling
public class ProjectApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(ProjectApplication.class, args);
	}

}
