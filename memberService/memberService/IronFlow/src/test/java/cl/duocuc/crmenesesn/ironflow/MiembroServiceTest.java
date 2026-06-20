package cl.duocuc.crmenesesn.ironflow;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.context.ActiveProfiles;

import cl.duocuc.crmenesesn.ironflow.dto.MiembroRequest;
import cl.duocuc.crmenesesn.ironflow.dto.MiembroResponse;
import cl.duocuc.crmenesesn.ironflow.model.Miembro;
import cl.duocuc.crmenesesn.ironflow.repository.MiembroRepository;
import cl.duocuc.crmenesesn.ironflow.service.MiembroService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;



@SpringBootTest
@ActiveProfiles("test")
public class MiembroServiceTest {

    @Autowired
    private MiembroService miembroService;

    @MockitoBean
    private MiembroRepository miembroRepository;

    @Test
    @DisplayName("Debe retornar todos los miembros correctamente")
    public void testObtenerTodosLosMiembros() {
        // GIVEN
        Miembro miembro = Miembro.builder()
                .id(1L).nombre("Juan Pérez").rut("12345678-9")
                .email("juan@gmail.com").telefono("912345678").estado("ACTIVO").build();
        when(miembroRepository.findAll()).thenReturn(List.of(miembro));

        // WHEN
        List<MiembroResponse> result = miembroService.obtenerTodosLosMiembros();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan Pérez", result.get(0).nombre());
    }

    @Test
    @DisplayName("Debe retornar un miembro por su ID")
    public void testObtenerMiembroPorId() {
        // GIVEN
        Miembro miembro = Miembro.builder()
                .id(1L).nombre("Juan Pérez").rut("12345678-9")
                .email("juan@gmail.com").telefono("912345678").estado("ACTIVO").build();
        when(miembroRepository.findById(1L)).thenReturn(Optional.of(miembro));

        // WHEN
        MiembroResponse result = miembroService.obtenerMiembroPorId(1L);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("12345678-9", result.rut());
    }

    @Test
    @DisplayName("Debe crear un miembro cuando los datos son válidos")
    public void testCrearMiembro() {
        // GIVEN
        MiembroRequest request = new MiembroRequest("Juan Pérez", "12345678-9", "juan@gmail.com", "912345678");
        Miembro miembro = Miembro.builder()
                .id(1L).nombre("Juan Pérez").rut("12345678-9")
                .email("juan@gmail.com").telefono("912345678").estado("ACTIVO").build();
        when(miembroRepository.existsByRut("12345678-9")).thenReturn(false);
        when(miembroRepository.existsByEmail("juan@gmail.com")).thenReturn(false);
        when(miembroRepository.save(any(Miembro.class))).thenReturn(miembro);

        // WHEN
        MiembroResponse result = miembroService.crearMiembro(request);

        // THEN
        assertNotNull(result);
        assertEquals("Juan Pérez", result.nombre());
        assertEquals("ACTIVO", result.estado());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el RUT ya existe")
    public void testCrearMiembroRutDuplicado() {
        // GIVEN
        MiembroRequest request = new MiembroRequest("Juan Pérez", "12345678-9", "juan@gmail.com", "912345678");
        when(miembroRepository.existsByRut("12345678-9")).thenReturn(true);

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> miembroService.crearMiembro(request));
    }

    @Test
    @DisplayName("Debe desactivar un miembro al eliminarlo")
    public void testEliminarMiembro() {
        // GIVEN
        Miembro miembro = Miembro.builder()
                .id(1L).nombre("Juan Pérez").rut("12345678-9")
                .email("juan@gmail.com").telefono("912345678").estado("ACTIVO").build();
        when(miembroRepository.findById(1L)).thenReturn(Optional.of(miembro));
        when(miembroRepository.save(any(Miembro.class))).thenReturn(miembro);

        // WHEN
        miembroService.eliminarMiembro(1L);

        // THEN
        verify(miembroRepository, times(1)).save(any(Miembro.class));
        assertEquals("INACTIVO", miembro.getEstado());
    }

    @Test
    @DisplayName("No debe permitir actualizar con el RUT de otro miembro")
    void testActualizarMiembroRutDuplicado() {
        Miembro miembro = Miembro.builder()
                .id(1L).nombre("Juan Pérez").rut("12345678-9")
                .email("juan@gmail.com").telefono("912345678").estado("ACTIVO").build();
        MiembroRequest request = new MiembroRequest("Juan Pérez", "11111111-1", "juan@gmail.com", "912345678");
        when(miembroRepository.findById(1L)).thenReturn(Optional.of(miembro));
        when(miembroRepository.existsByRutAndIdNot("11111111-1", 1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> miembroService.actualizarMiembro(1L, request));
        verify(miembroRepository, never()).save(any());
    }

    @Test
    @DisplayName("No debe permitir actualizar un miembro inactivo")
    void testActualizarMiembroInactivo() {
        Miembro miembro = Miembro.builder()
                .id(1L).nombre("Juan Pérez").rut("12345678-9")
                .email("juan@gmail.com").telefono("912345678").estado("INACTIVO").build();
        MiembroRequest request = new MiembroRequest("Juan Pérez", "12345678-9", "juan@gmail.com", "912345678");
        when(miembroRepository.findById(1L)).thenReturn(Optional.of(miembro));

        assertThrows(IllegalArgumentException.class, () -> miembroService.actualizarMiembro(1L, request));
        verify(miembroRepository, never()).save(any());
    }
}
