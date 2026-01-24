package com.sprint.mission.sb8hrbankteamquerity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
public class Sb8HrbankTeamQuerityApplication {

    public static void main(String[] args) {
        SpringApplication.run(Sb8HrbankTeamQuerityApplication.class, args);
    }

}
