package co.com.nequi.franchising.api.handler;

import co.com.nequi.franchising.api.dto.request.BranchRequestDto;
import co.com.nequi.franchising.api.dto.request.BranchUpdateNameDto;
import co.com.nequi.franchising.api.dto.response.BranchResponseDto;
import co.com.nequi.franchising.model.branch.Branch;
import co.com.nequi.franchising.usecase.branch.BranchUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

class BranchHandlerTest {

    private BranchUseCase branchUseCase;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        branchUseCase = mock(BranchUseCase.class);
        BranchHandler handler = new BranchHandler(branchUseCase);

        webTestClient = WebTestClient.bindToRouterFunction(
                route().POST("/api/branch/create", handler::saveBranch).build()
                        .andRoute(PUT("/api/branch/{branchId}/update-name"), handler::updateBranchName)
        ).build();
    }

    @Test
    void shouldReturnOkWhenBranchIsValid() {
        BranchRequestDto requestDto = new BranchRequestDto("Branch Name", 1L);
        BranchResponseDto responseDto = new BranchResponseDto(1L, "Branch Name", 1L);
        Branch branch = Branch.builder()
                .id(1L)
                .name("Branch Name")
                .franchiseId(1L)
                .build();
        when(branchUseCase.saveBranch(any())).thenReturn(Mono.just(branch));

        webTestClient.post()
                .uri("/api/branch/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BranchResponseDto.class)
                .value(response -> {
                    assertEquals(1L, response.id());
                    assertEquals("Branch Name", response.name());
                    assertEquals(1L, response.franchiseId());
                });
    }

    @Test
    void shouldReturnBadRequestWhenBranchNameIsEmpty() {
        BranchRequestDto requestDto = new BranchRequestDto("", 1L);

        webTestClient.post()
                .uri("/api/branch/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Branch name must not be null or empty");
    }

    @Test
    void shouldReturnBadRequestWhenBranchNameIsNull() {
        BranchRequestDto requestDto = new BranchRequestDto(null, 1L);

        webTestClient.post()
                .uri("/api/branch/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Branch name must not be null or empty");
    }

    @Test
    void shouldReturnBadRequestWhenFranchiseIdIsNull() {
        BranchRequestDto requestDto = new BranchRequestDto("Branch Name", null);

        webTestClient.post()
                .uri("/api/branch/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Branch franchise ID must not be null");
    }

    @Test
    void shouldUpdateBranchNameSuccessfully() {
        Long branchId = 1L;
        String newName = "Updated Branch Name";
        Branch branch = Branch.builder()
                .id(branchId)
                .name(newName)
                .franchiseId(1L)
                .build();
        when(branchUseCase.updateBranchName(branchId, newName)).thenReturn(Mono.just(branch));

        webTestClient.put()
                .uri("/api/branch/{branchId}/update-name", branchId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new BranchUpdateNameDto(newName))
                .exchange()
                .expectStatus().isOk()
                .expectBody(BranchResponseDto.class)
                .value(response -> {
                    assertEquals(branchId, response.id());
                    assertEquals(newName, response.name());
                    assertEquals(1L, response.franchiseId());
                });
    }


    @Test
    void shouldReturnBadRequestWhenBranchIdIsNotNumeric() {
        String invalidBranchId = "abc";

        webTestClient.put()
                .uri("/api/branch/{branchId}/update-name", invalidBranchId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("New Branch Name")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Branch ID must be numeric");
    }

    @Test
    void shouldReturnBadRequestWhenBranchNameIsEmptyOnUpdate() {
        Long branchId = 1L;
        BranchUpdateNameDto requestDto = new BranchUpdateNameDto("");

        webTestClient.put()
                .uri("/api/branch/{branchId}/update-name", branchId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Branch name must not be null or empty");
    }

    @Test
    void shouldReturnBadRequestWhenBranchNameIsNullOnUpdate() {
        Long branchId = 1L;
        BranchUpdateNameDto requestDto = new BranchUpdateNameDto(null);

        webTestClient.put()
                .uri("/api/branch/{branchId}/update-name", branchId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Branch name must not be null or empty");
    }
}