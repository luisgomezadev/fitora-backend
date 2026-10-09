package com.lgsoftworks.service;

import com.lgsoftworks.auth.application.service.CurrentUserService;
import com.lgsoftworks.plan.application.dto.mapper.PlanModelMapper;
import com.lgsoftworks.plan.application.dto.request.PlanRequest;
import com.lgsoftworks.plan.application.dto.response.PlanResponse;
import com.lgsoftworks.plan.application.service.PlanService;
import com.lgsoftworks.plan.domain.exception.PlanByIdNotFoundException;
import com.lgsoftworks.plan.domain.exception.PlanWithNameExistsException;
import com.lgsoftworks.plan.domain.model.Plan;
import com.lgsoftworks.plan.domain.port.out.PlanRepositoryPort;
import com.lgsoftworks.plan.domain.service.PlanUniquenessValidator;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// --- Imports estáticos de JUnit/Mockito ---
// assertEquals, assertThrows, etc. permiten hacer las comprobaciones (asserts)
import static org.junit.jupiter.api.Assertions.*;
// any(), eq(), verify(), when(), times() son los "helpers" de Mockito para
// configurar comportamiento de mocks y luego verificar que se llamaron
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * ============================================================
 *  CONCEPTOS CLAVE DE ESTE TEST (para entender JUnit + Mockito)
 * ============================================================

 * 1) @ExtendWith(MockitoExtension.class)
 *    Le dice a JUnit 5 que active la integración con Mockito.
 *    Gracias a esto, las anotaciones @Mock y @InjectMocks funcionan
 *    "mágicamente" sin tener que instanciarlas a mano.

 * 2) @Mock
 *    Crea un "doble de prueba" (test double) de una dependencia.
 *    Es un objeto falso que se comporta como tú le digas con when(...).
 *    No ejecuta lógica real: si no le configuras nada, sus métodos
 *    devuelven null / 0 / false por defecto.

 * 3) @InjectMocks
 *    Crea una instancia REAL de la clase que se está probando
 *    (PlanService) e inyecta automáticamente los @Mock declarados
 *    arriba en su constructor (gracias a @RequiredArgsConstructor
 *    de Lombok, Mockito encuentra el constructor y mete los mocks).

 * 4) when(mock.metodo(args)).thenReturn(valor)
 *    "Cuando llamen a este metodo del mock con estos argumentos,
 *    responde con este valor". Así simulamos la base de datos,
 *    el usuario actual, etc., sin tocar nada real.

 * 5) verify(mock).metodo(args)
 *    Comprueba que el mock SÍ fue llamado con esos argumentos.
 *    Sirve para probar comportamiento (por ejemplo, que se guardó
 *    el plan) además de resultados.

 * 6) ArgumentCaptor<T>
 *    "Atrapa" el objeto real que se pasó a un mock para poder
 *    inspeccionar sus campos después (útil cuando el objeto se
 *    construye dentro del metodo probado, como el Plan en create()).

 * 7) assertThrows(Exception.class, () -> ...)
 *    Comprueba que se lanza la excepción esperada, por ejemplo
 *    cuando no se encuentra el plan por id.

 * 8) @Nested + @DisplayName
 *    Permiten agrupar tests relacionados (uno por metodo del
 *    servicio) y darles nombres legibles en los reportes.
 */
@ExtendWith(MockitoExtension.class)
class PlanServiceTest {

    // --- Dependencias falsas (mocks) que PlanService necesita ---
    @Mock
    private PlanRepositoryPort planRepositoryPort;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private PlanUniquenessValidator planUniquenessValidator;

    @Mock
    private PlanModelMapper planModelMapper;

    // --- La clase real que vamos a probar, con los mocks ya inyectados ---
    @InjectMocks
    private PlanService planService;

    // Datos comunes reutilizados en varios tests
    private UUID gymId;
    private UUID planId;
    private PlanRequest planRequest;

    @BeforeEach
    void setUp() {
        // Se ejecuta ANTES de cada @Test, para no repetir código
        // y asegurar que cada test empieza desde un estado limpio.
        gymId = UUID.randomUUID();
        planId = UUID.randomUUID();

        planRequest = new PlanRequest();
        planRequest.setName("Plan Mensual");
        planRequest.setDescription("Acceso ilimitado por 30 días");
        planRequest.setDurationDays(30);
        planRequest.setPrice(BigDecimal.valueOf(50000));
    }

    // =========================================================
    //  TESTS PARA execute() -> creación de un plan
    // =========================================================
    @Nested
    @DisplayName("execute() - Crear plan")
    class ExecuteTests {

        @Test
        @DisplayName("Debe crear el plan y devolver un ApiResponse exitoso")
        void deberiaCrearPlanCorrectamente() {
            // ---- ARRANGE (preparar el escenario) ----
            // 1. Simulamos que el usuario actual pertenece a "gymId"
            when(currentUserService.getGymId()).thenReturn(gymId);

            // 2. El validador de unicidad no hace nada especial si el nombre
            //    es válido (es un metodo void, así que no requiere "when").
            //    Mockito, por defecto, no lanza excepción -> simula ok.

            // 3. Creamos el objeto Plan real que "debería" salir de Plan.create(...)
            Plan planCreado = Plan.create(
                    gymId,
                    planRequest.getName(),
                    planRequest.getDescription(),
                    planRequest.getDurationDays(),
                    planRequest.getPrice()
            );

            // 4. Simulamos que al guardar, el repositorio devuelve ese mismo plan
            when(planRepositoryPort.save(any(Plan.class))).thenReturn(planCreado);

            // 5. Simulamos el mapeo a DTO de respuesta
            PlanResponse planResponseEsperado = PlanResponse.builder().name("Plan Mensual").build();
            when(planModelMapper.toResponse(planCreado)).thenReturn(planResponseEsperado);

            // ---- ACT (ejecutar el metodo real que probamos) ----
            ApiResponse<PlanResponse> resultado = planService.execute(planRequest);

            // ---- ASSERT (comprobar resultados) ----
            assertNotNull(resultado);
            assertEquals("Plan creado correctamente", resultado.getMessage());
            assertEquals(planResponseEsperado, resultado.getData());

            // ---- VERIFY (comprobar interacciones/comportamiento) ----
            // Nos aseguramos de que se validó la unicidad ANTES de guardar
            verify(planUniquenessValidator).validate(planRequest.getName(), gymId);
            verify(planRepositoryPort).save(any(Plan.class));
            verify(planModelMapper).toResponse(planCreado);
        }

        @Test
        @DisplayName("Debe propagar la excepción si el nombre del plan ya existe")
        void deberiaLanzarExcepcionSiNombreDuplicado() {
            when(currentUserService.getGymId()).thenReturn(gymId);

            // Configuramos el mock para que LANCE una excepción cuando se
            // invoque el validador (simulando "nombre duplicado")
            doThrow(new PlanWithNameExistsException())
                    .when(planUniquenessValidator)
                    .validate(planRequest.getName(), gymId);

            // Comprobamos que la excepción se propaga hacia arriba
            assertThrows(PlanWithNameExistsException.class,
                    () -> planService.execute(planRequest));

            // Como falló la validación, NUNCA debió llegar a guardar el plan
            verify(planRepositoryPort, never()).save(any(Plan.class));
        }
    }

    // =========================================================
    //  TESTS PARA findByGymId() -> listar planes
    // =========================================================
    @Nested
    @DisplayName("findByGymId() - Listar planes")
    class FindByGymIdTests {

        @Test
        @DisplayName("Debe devolver la lista de planes del gimnasio actual")
        void deberiaRetornarListaDePlanes() {
            when(currentUserService.getGymId()).thenReturn(gymId);

            Plan plan1 = Plan.create(gymId, "Plan A", "desc", 30, BigDecimal.TEN);
            Plan plan2 = Plan.create(gymId, "Plan B", "desc", 60, BigDecimal.ONE);
            List<Plan> planesEnBd = List.of(plan1, plan2);

            when(planRepositoryPort.findByGymId(gymId)).thenReturn(planesEnBd);

            PlanResponse resp1 = PlanResponse.builder().name("Plan A").build();
            PlanResponse resp2 = PlanResponse.builder().name("Plan B").build();
            when(planModelMapper.toResponse(plan1)).thenReturn(resp1);
            when(planModelMapper.toResponse(plan2)).thenReturn(resp2);

            ApiResponse<List<PlanResponse>> resultado = planService.findByGymId();

            assertEquals(2, resultado.getData().size());
            assertTrue(resultado.getData().containsAll(List.of(resp1, resp2)));
            assertEquals("Planes obtenidos correctamente", resultado.getMessage());
        }

        @Test
        @DisplayName("Debe devolver lista vacía si el gimnasio no tiene planes")
        void deberiaRetornarListaVaciaSinPlanes() {
            when(currentUserService.getGymId()).thenReturn(gymId);
            when(planRepositoryPort.findByGymId(gymId)).thenReturn(List.of());

            ApiResponse<List<PlanResponse>> resultado = planService.findByGymId();

            assertTrue(resultado.getData().isEmpty());
            // Si no hay planes, el mapper nunca debería llamarse
            verify(planModelMapper, never()).toResponse(any(Plan.class));
        }
    }

    // =========================================================
    //  TESTS PARA update() -> actualizar un plan existente
    // =========================================================
    @Nested
    @DisplayName("update() - Actualizar plan")
    class UpdateTests {

        @Test
        @DisplayName("Debe actualizar el plan cuando existe y pertenece al gym")
        void deberiaActualizarPlanCorrectamente() {
            when(currentUserService.getGymId()).thenReturn(gymId);

            Plan planExistente = Plan.create(gymId, "Plan Viejo", "desc vieja", 15, BigDecimal.ONE);

            when(planRepositoryPort.findByIdAndGymId(planId, gymId))
                    .thenReturn(Optional.of(planExistente));
            when(planRepositoryPort.save(planExistente)).thenReturn(planExistente);

            PlanResponse planResponseEsperado = PlanResponse.builder().name("Plan Mensual").build();
            when(planModelMapper.toResponse(planExistente)).thenReturn(planResponseEsperado);

            ApiResponse<PlanResponse> resultado = planService.update(planId, planRequest);

            assertEquals("Plan actualizado correctamente", resultado.getMessage());
            assertEquals(planResponseEsperado, resultado.getData());

            // Verificamos que se buscó por el id y gym correctos, y se guardó
            verify(planRepositoryPort).findByIdAndGymId(planId, gymId);
            verify(planRepositoryPort).save(planExistente);
        }

        @Test
        @DisplayName("Debe lanzar PlanByIdNotFoundException si el plan no existe")
        void deberiaLanzarExcepcionSiPlanNoExiste() {
            when(currentUserService.getGymId()).thenReturn(gymId);
            when(planRepositoryPort.findByIdAndGymId(planId, gymId))
                    .thenReturn(Optional.empty());

            assertThrows(PlanByIdNotFoundException.class,
                    () -> planService.update(planId, planRequest));

            // Si no se encontró el plan, jamás debe intentar guardarlo
            verify(planRepositoryPort, never()).save(any(Plan.class));
        }
    }

    // =========================================================
    //  TESTS PARA deactivate() y activate()
    // =========================================================
    @Nested
    @DisplayName("deactivate() / activate() - Cambiar estado del plan")
    class ActivateDeactivateTests {

        @Test
        @DisplayName("Debe desactivar un plan existente")
        void deberiaDesactivarPlan() {
            when(currentUserService.getGymId()).thenReturn(gymId);

            // Usamos un mock (en vez de un objeto real) para poder verificar
            // que se llamó al metodo de dominio plan.deactivate()
            Plan planMock = mock(Plan.class);
            when(planRepositoryPort.findByIdAndGymId(planId, gymId))
                    .thenReturn(Optional.of(planMock));

            ApiResponse<Void> resultado = planService.deactivate(planId);

            assertEquals("Plan desactivado correctamente", resultado.getMessage());

            verify(planMock).deactivate();
            verify(planRepositoryPort).save(planMock);
        }

        @Test
        @DisplayName("Debe activar un plan existente")
        void deberiaActivarPlan() {
            when(currentUserService.getGymId()).thenReturn(gymId);

            Plan planMock = mock(Plan.class);
            when(planRepositoryPort.findByIdAndGymId(planId, gymId))
                    .thenReturn(Optional.of(planMock));

            ApiResponse<Void> resultado = planService.activate(planId);

            assertEquals("Plan activado correctamente", resultado.getMessage());
            verify(planMock).activate();
            verify(planRepositoryPort).save(planMock);
        }

        @Test
        @DisplayName("Debe lanzar excepción al desactivar un plan inexistente")
        void deberiaLanzarExcepcionAlDesactivarPlanInexistente() {
            when(currentUserService.getGymId()).thenReturn(gymId);
            when(planRepositoryPort.findByIdAndGymId(planId, gymId))
                    .thenReturn(Optional.empty());

            assertThrows(PlanByIdNotFoundException.class,
                    () -> planService.deactivate(planId));

            verify(planRepositoryPort, never()).save(any());
        }
    }

    // =========================================================
    //  EJEMPLO EXTRA: uso de ArgumentCaptor
    // =========================================================
    @Test
    @DisplayName("Ejemplo con ArgumentCaptor: revisa los datos del Plan creado internamente")
    void ejemploArgumentCaptorAlCrearPlan() {
        when(currentUserService.getGymId()).thenReturn(gymId);

        // "Capturamos" el objeto Plan real que PlanService construye
        // y pasa a planRepositoryPort.save(...), para poder inspeccionarlo.
        ArgumentCaptor<Plan> planCaptor = ArgumentCaptor.forClass(Plan.class);

        // No nos importa el valor de retorno exacto aquí, solo capturar el argumento
        when(planRepositoryPort.save(planCaptor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(planModelMapper.toResponse(any(Plan.class))).thenReturn(PlanResponse.builder().build());

        planService.execute(planRequest);

        Plan planCapturado = planCaptor.getValue();
        assertEquals("Plan Mensual", planCapturado.getName());
        assertEquals(gymId, planCapturado.getGymId());
    }
}