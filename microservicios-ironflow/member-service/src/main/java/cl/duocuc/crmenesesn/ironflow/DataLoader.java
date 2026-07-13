package cl.duocuc.crmenesesn.ironflow;

import cl.duocuc.crmenesesn.ironflow.model.Miembro;
import cl.duocuc.crmenesesn.ironflow.repository.MiembroRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private MiembroRepository miembroRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(new java.util.Locale("es"));

        for (int i = 0; i < 10; i++) {
            Miembro miembro = Miembro.builder()
                    .nombre(faker.name().fullName())
                    .rut(faker.numerify("########-#"))
                    .email(faker.internet().emailAddress())
                    .telefono(faker.phoneNumber().cellPhone())
                    .estado("ACTIVO")
                    .build();
            miembroRepository.save(miembro);
        }
        System.out.println("✅ DataLoader: 10 miembros generados con DataFaker");
    }
}