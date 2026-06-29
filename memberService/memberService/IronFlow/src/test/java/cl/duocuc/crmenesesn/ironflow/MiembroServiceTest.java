package cl.duocuc.crmenesesn.ironflow;

import cl.duocuc.crmenesesn.ironflow.dto.MiembroRequest;
import cl.duocuc.crmenesesn.ironflow.dto.MiembroResponse;
import cl.duocuc.crmenesesn.ironflow.model.Miembro;
import cl.duocuc.crmenesesn.ironflow.repository.MiembroRepository;
import cl.duocuc.crmenesesn.ironflow.service.MiembroService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        Miembro miembro = miembroActivo();
        when(miembroRepository.findAll()).thenReturn(List.of(miembro));

        List<MiembroResponse> result = miembroService.obtenerTodosLosMiembros();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan Pérez", result.get(0).nombre());
    }

    @Test
    @DisplayName("Debe retornar un miembro por su ID")
    public void testObtenerMiembroPorId() {
        Miembro miembro = miembroActivo();
        when(miembroRepository.findById(1L)).thenReturn(Optional.of(miembro));

        MiembroResponse result = miembroService.obtenerMiembroPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("12345678-9", result.rut());
    }

    @Test
    @DisplayName("Debe crear un miembro cuando los datos son válidos")
    public void testCrearMiembro() {
        MiembroRequest request = request("12345678-9", "juan@gmail.com");
        Miembro miembro = miembroActivo();
        when(miembroRepository.existsByRut("12345678-9")).thenReturn(false);
        when(miembroRepository.existsByEmail("juan@gmail.com")).thenReturn(false);
        when(miembroRepository.save(any(Miembro.class))).thenReturn(miembro);

        MiembroResponse result = miembroService.crearMiembro(request);

        assertNotNull(result);
        assertEquals("Juan Pérez", result.nombre());
        assertEquals("ACTIVO", result.estado());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el RUT ya existe")
    public void testCrearMiembroRutDuplicado() {
        MiembroRequest request = request("12345678-9", "juan@gmail.com");
        when(miembroRepository.existsByRut("12345678-9")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> miembroService.crearMiembro(request));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el email ya existe")
    public void testCrearMiembroEmailDuplicado() {
        MiembroRequest request = request("12345678-9", "juan@gmail.com");
        when(miembroRepository.existsByRut("12345678-9")).thenReturn(false);
        when(miembroRepository.existsByEmail("juan@gmail.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> miembroService.crearMiembro(request));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar un miembro que no existe")
    public void testActualizarMiembroNoEncontrado() {
        MiembroRequest request = request("12345678-9", "juan@gmail.com");
        when(miembroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> miembroService.actualizarMiembro(99L, request));
    }

    @Test
    @DisplayName("No debe permitir actualizar un miembro inactivo")
    public void testActualizarMiembroInactivo() {
        Miembro miembro = miembroActivo();
        miembro.setEstado("INACTIVO");
        MiembroRequest request = request("12345678-9", "juan@gmail.com");
        when(miembroRepository.findById(1L)).thenReturn(Optional.of(miembro));

        assertThrows(IllegalArgumentException.class, () -> miembroService.actualizarMiembro(1L, request));
        verify(miembroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar con un RUT que ya pertenece a otro miembro")
    public void testActualizarMiembroRutDuplicado() {
        Miembro miembro = miembroActivo();
        MiembroRequest request = request("98765432-1", "juan@gmail.com");
        when(miembroRepository.findById(1L)).thenReturn(Optional.of(miembro));
        when(miembroRepository.existsByRutAndIdNot("98765432-1", 1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> miembroService.actualizarMiembro(1L, request));
        verify(miembroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar con un email que ya pertenece a otro miembro")
    public void testActualizarMiembroEmailDuplicado() {
        Miembro miembro = miembroActivo();
        MiembroRequest request = request("12345678-9", "otro@gmail.com");
        when(miembroRepository.findById(1L)).thenReturn(Optional.of(miembro));
        when(miembroRepository.existsByRutAndIdNot("12345678-9", 1L)).thenReturn(false);
        when(miembroRepository.existsByEmailAndIdNot("otro@gmail.com", 1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> miembroService.actualizarMiembro(1L, request));
        verify(miembroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe desactivar un miembro al eliminarlo")
    public void testEliminarMiembro() {
        Miembro miembro = miembroActivo();
        when(miembroRepository.findById(1L)).thenReturn(Optional.of(miembro));
        when(miembroRepository.save(any(Miembro.class))).thenReturn(miembro);

        miembroService.eliminarMiembro(1L);

        verify(miembroRepository, times(1)).save(any(Miembro.class));
        assertEquals("INACTIVO", miembro.getEstado());
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar un miembro que no existe")
    public void testEliminarMiembroNoEncontrado() {
        when(miembroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> miembroService.eliminarMiembro(99L));
    }

    private Miembro miembroActivo() {
        return Miembro.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .rut("12345678-9")
                .email("juan@gmail.com")
                .telefono("912345678")
                .estado("ACTIVO")
                .build();
    }

    private MiembroRequest request(String rut, String email) {
        return new MiembroRequest("Juan Pérez", rut, email, "912345678");
    }
}
