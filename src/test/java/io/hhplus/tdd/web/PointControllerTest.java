package io.hhplus.tdd.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.hhplus.tdd.point.application.PointChargingService;
import io.hhplus.tdd.point.application.PointHistoryReadingService;
import io.hhplus.tdd.point.application.PointReadingService;
import io.hhplus.tdd.point.application.PointUsingService;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PointController.class)
class PointControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PointReadingService pointReadingService;

  @MockBean
  private PointChargingService pointChargingService;

  @MockBean
  private PointUsingService pointUsingService;

  @MockBean
  private PointHistoryReadingService pointHistoryReadingService;

  @Test
  @DisplayName("포인트를 정상적으로 조회한다.")
  void read() throws Exception {
    // given
    long id = 1L;
    when(pointReadingService.read(id))
        .thenReturn(100L);

    // when
    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .get("/points/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(StandardCharsets.UTF_8);

    // then
    mockMvc.perform(builder)
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("포인트를 정상적으로 충전한다.")
  void charge() throws Exception {
    // given
    long id = 1L;
    long amount = 100L;

    // when
    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .patch("/points/" + id + "/charge")
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(StandardCharsets.UTF_8)
        .content("""
            {"amount": %d}
            """.formatted(amount));

    // then
    mockMvc.perform(builder)
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("포인트를 정상적으로 사용한다.")
  void use() throws Exception {
    // given
    long id = 1L;
    long amount = 100L;

    // when
    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .patch("/points/" + id + "/use")
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(StandardCharsets.UTF_8)
        .content("""
            {"amount": %d}
            """.formatted(amount));

    // then
    mockMvc.perform(builder)
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("포인트 사용 내역을 정상적으로 조회한다.")
  void readHistories() throws Exception {
    // given
    long id = 1L;
    when(pointHistoryReadingService.readHistories(id))
        .thenReturn(Collections.emptyList());

    // when
    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .get("/points/" + id + "/histories")
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(StandardCharsets.UTF_8);

    // then
    mockMvc.perform(builder)
        .andExpect(status().isOk());
  }

  @ParameterizedTest
  @ValueSource(longs = {-1L, 0L})
  @DisplayName("포인트 조회 시 유효하지 않은 ID면 400 에러를 반환한다.")
  void read_ShouldReturn400_WhenIdIsInvalid(long invalidId) throws Exception {
    // when
    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .get("/points/" + invalidId)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(StandardCharsets.UTF_8);

    // then
    mockMvc.perform(builder)
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @ValueSource(longs = {-1L, 0L})
  @DisplayName("포인트 이력 조회 시 유효하지 않은 id면 400 에러를 반환한다.")
  void readHistories_ShouldReturn400_WhenIdIsInvalid(long invalidId) throws Exception {
    // when
    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .get("/points/" + invalidId + "/histories")
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(StandardCharsets.UTF_8);

    // then
    mockMvc.perform(builder)
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "0, -1", "-1, 0", "-1, -1"})
  @DisplayName("포인트 충전 시 id나 amount가 유효하지 않으면 400 에러를 반환한다.")
  void charge_ShouldReturn400_WhenIdOrAmountIsInvalid(long invalidId, long invalidAmount) throws Exception {

    // when
    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .patch("/points/" + invalidId + "/charge")
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(StandardCharsets.UTF_8)
        .content("""
            {"amount": %d}
            """.formatted(invalidAmount));

    // then
    mockMvc.perform(builder)
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "0, -1", "-1, 0", "-1, -1"})
  @DisplayName("포인트 사용 시 id나 amount가 유효하지 않으면 400 에러를 반환한다.")
  void use_ShouldReturn400_WhenIdOrAmountIsInvalid() throws Exception {
    // given
    long negativeId = -1L;
    long amount = 100L;

    // when
    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .patch("/points/" + negativeId + "/use")
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(StandardCharsets.UTF_8)
        .content("""
            {"amount": %d}
            """.formatted(amount));

    // then
    mockMvc.perform(builder)
        .andExpect(status().isBadRequest());
  }
}
