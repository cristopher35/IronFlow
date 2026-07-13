package cl.duocuc.crmenesesn.ironflow.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import cl.duocuc.crmenesesn.ironflow.dto.MiembroRequest;
import cl.duocuc.crmenesesn.ironflow.dto.MiembroResponse;
import cl.duocuc.crmenesesn.ironflow.service.MiembroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(MiembroController.class)
public class MiembroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MiembroService miembroService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MiembroResponse miembroResponse;
    private MiembroRequest miembroRequest;

    @BeforeEach
    void setUp() {
        miembroResponse = new MiembroResponse(1L, "Juan Pérez", "12345678-9", "juan@gmail.com", "912345678", "ACTIVO");
        miembroRequest = new MiembroRequest("Juan Pérez", "12345678-9", "juan@gmail.com", "912345678");
    }

    @Test
    @DisplayName("GET /api/members debe retornar lista de miembros con status 200")
    public void testGetAllMiembros() throws Exception {
        when(miembroService.obtenerTodosLosMiembros()).thenReturn(List.of(miembroResponse));

        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].rut").value("12345678-9"));
    }

    @Test
    @DisplayName("GET /api/members/{id} debe retornar un miembro por ID con status 200")
    public void testGetMiembroById() throws Exception {
        when(miembroService.obtenerMiembroPorId(1L)).thenReturn(miembroResponse);

        mockMvc.perform(get("/api/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("juan@gmail.com"));
    }

    @Test
    @DisplayName("POST /api/members debe crear un miembro y retornar status 201")
    public void testCreateMiembro() throws Exception {
        when(miembroService.crearMiembro(any(MiembroRequest.class))).thenReturn(miembroResponse);

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(miembroRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"));
    }

    @Test
    @DisplayName("PUT /api/members/{id} debe actualizar un miembro y retornar status 200")
    public void testUpdateMiembro() throws Exception {
        when(miembroService.actualizarMiembro(eq(1L), any(MiembroRequest.class))).thenReturn(miembroResponse);

        mockMvc.perform(put("/api/members/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(miembroRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    @DisplayName("DELETE /api/members/{id} debe eliminar un miembro y retornar status 204")
    public void testDeleteMiembro() throws Exception {
        doNothing().when(miembroService).eliminarMiembro(1L);

        mockMvc.perform(delete("/api/members/1"))
                .andExpect(status().isNoContent());

        verify(miembroService, times(1)).eliminarMiembro(1L);
    }
}