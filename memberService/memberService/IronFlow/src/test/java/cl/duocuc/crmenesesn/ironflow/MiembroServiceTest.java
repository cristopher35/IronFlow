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
import java.util.NoSuchElementException;

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
    }
    @Test
    @DisplayName("Debe lanzar excepción si el email ya existe")
    public void testCrearMiembroEmailDuplicado() {
        // GIVEN
        MiembroRequest request = new MiembroRequest("Juan Pérez", "12345678-9", "juan@gmail.com", "912345678");
        when(miembroRepository.existsByRut("12345678-9")).thenReturn(false);
        when(miembroRepository.existsByEmail("juan@gmail.com")).thenReturn(true);

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> miembroService.crearMiembro(request));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar un miembro que no existe")
    public void testActualizarMiembroNoEncontrado() {
        // GIVEN
        MiembroRequest request = new MiembroRequest("Juan Pérez", "12345678-9", "juan@gmail.com", "912345678");
        when(miembroRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(NoSuchElementException.class, () -> miembroService.actualizarMiembro(99L, request));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar un miembro INACTIVO")
    public void testActualizarMiembroInactivo() {
        // GIVEN
        Miembro miembro = Miembro.builder()
                .id(1L).nombre("Juan Pérez").rut("12345678-9")
                .email("juan@gmail.com").telefono("912345678").estado("INACTIVO").build();
        MiembroRequest request = new MiembroRequest("Juan Pérez", "12345678-9", "juan@gmail.com", "912345678");
        when(miembroRepository.findById(1L)).thenReturn(Optional.of(miembro));

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> miembroService.actualizarMiembro(1L, request));
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar un miembro que no existe")
    public void testEliminarMiembroNoEncontrado() {
        // GIVEN
        when(miembroRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(NoSuchElementException.class, () -> miembroService.eliminarMiembro(99L));
    }
    @Test
    @DisplayName("Debe lanzar excepción al actualizar con un RUT que ya pertenece a otro miembro")
    public void testActualizarMiembroRutDuplicado() {
        // GIVEN
        Miembro miembro = Miembro.builder()
                .id(1L).nombre("Juan Pérez").rut("12345678-9")
                .email("juan@gmail.com").telefono("912345678").estado("ACTIVO").build();
        MiembroRequest request = new MiembroRequest("Juan Pérez", "98765432-1", "juan@gmail.com", "912345678");
        when(miembroRepository.findById(1L)).thenReturn(Optional.of(miembro));
        when(miembroRepository.existsByRutAndIdNot("98765432-1", 1L)).thenReturn(true);

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> miembroService.actualizarMiembro(1L, request));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar con un email que ya pertenece a otro miembro")
    public void testActualizarMiembroEmailDuplicado() {
        // GIVEN
        Miembro miembro = Miembro.builder()
                .id(1L).nombre("Juan Pérez").rut("12345678-9")
                .email("juan@gmail.com").telefono("912345678").estado("ACTIVO").build();
        MiembroRequest request = new MiembroRequest("Juan Pérez", "12345678-9", "otro@gmail.com", "912345678");
        when(miembroRepository.findById(1L)).thenReturn(Optional.of(miembro));
        when(miembroRepository.existsByRutAndIdNot("12345678-9", 1L)).thenReturn(false);
        when(miembroRepository.existsByEmailAndIdNot("otro@gmail.com", 1L)).thenReturn(true);

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> miembroService.actualizarMiembro(1L, request));
    }
}