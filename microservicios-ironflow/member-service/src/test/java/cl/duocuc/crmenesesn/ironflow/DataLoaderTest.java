package cl.duocuc.crmenesesn.ironflow;

import cl.duocuc.crmenesesn.ironflow.model.Miembro;
import cl.duocuc.crmenesesn.ironflow.repository.MiembroRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DataLoaderTest {

    @Test
    void createsTenDemoMembersInDevProfileRunner() throws Exception {
        // Given
        MiembroRepository repository = mock(MiembroRepository.class);
        DataLoader loader = new DataLoader();
        ReflectionTestUtils.setField(loader, "miembroRepository", repository);

        // When
        loader.run();

        // Then
        verify(repository, times(10)).save(any(Miembro.class));
    }
}
