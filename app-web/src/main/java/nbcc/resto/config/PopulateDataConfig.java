package nbcc.resto.config;

import nbcc.resto.entity.UrbanPlatterEventEntity;
import nbcc.resto.repository.UrbanPlatterEventJPARepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class PopulateDataConfig {
    @Bean
    public ApplicationRunner populateEvents(UrbanPlatterEventJPARepository eventRepository){
        return args -> {
            if (eventRepository.count() > 0) return;
            List<UrbanPlatterEventEntity> events = new ArrayList<>();

            events.add(createEvent("Nexora Summit",
                    "Business innovation conference",
                    LocalDateTime.of(2026, 4, 15, 9, 0),
                    LocalDateTime.of(2026, 4, 15, 17, 0),
                    49.99));

            events.add(createEvent("CodeFusion 2026",
                    "Developer conference",
                    LocalDateTime.of(2026, 4, 16, 10, 0),
                    LocalDateTime.of(2026, 4, 16, 18, 0),
                    29.99));

            events.add(createEvent("Urban Pulse Fest",
                    "Street culture festival with music and food",
                    LocalDateTime.of(2026, 4, 20, 10, 0),
                    LocalDateTime.of(2026, 4, 22, 22, 0),
                    15.0));

            events.add(createEvent("Neon Nights Party",
                    "Music nightlife event",
                    LocalDateTime.of(2026, 4, 25, 20, 0),
                    LocalDateTime.of(2026, 4, 26, 2, 0),
                    20.0));

            events.add(createEvent("Vertex Leadership Conference",
                    "Leadership training",
                    LocalDateTime.of(2026, 4, 26, 9, 0),
                    LocalDateTime.of(2026, 4, 26, 14, 0),
                    39.99));

            events.add(createEvent("EchoWave Showcase",
                    "Music performance showcase",
                    LocalDateTime.of(2026, 4, 27, 18, 0),
                    LocalDateTime.of(2026, 4, 27, 22, 0),
                    22.5));

            events.add(createEvent("Catalyst Business Expo",
                    "Business expo",
                    LocalDateTime.of(2026, 4, 28, 10, 0),
                    LocalDateTime.of(2026, 4, 29, 18, 0),
                    45.0));

            events.add(createEvent("Prism Culture Night",
                    "Cultural celebration",
                    LocalDateTime.of(2026, 4, 30, 17, 0),
                    LocalDateTime.of(2026, 4, 30, 23, 0),
                    18.0));

            events.add(createEvent("ByteStorm Conference",
                    "Software architecture talks",
                    LocalDateTime.of(2026, 5, 1, 9, 0),
                    LocalDateTime.of(2026, 5, 1, 17, 0),
                    35.0));

            eventRepository.saveAll(events);

        };
    }
    private UrbanPlatterEventEntity createEvent(String name,
                                          String description,
                                          LocalDateTime start,
                                          LocalDateTime end,
                                          Double price) {

        var minutes = java.time.Duration.between(start, end).toMinutes();

        return new UrbanPlatterEventEntity()
                .setName(name)
                .setDescription(description)
                .setStartDate(start)
                .setEndDate(end)
                .setMinutes((int) minutes)
                .setPrice(price);
    }
}
