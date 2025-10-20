package io.hhplus.tdd.web;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.hhplus.tdd.point.application.PointChargingService;
import io.hhplus.tdd.point.application.PointHistoryReadingService;
import io.hhplus.tdd.point.application.PointReadingService;
import io.hhplus.tdd.point.application.PointUsingService;
import io.hhplus.tdd.point.domain.model.PointHistory;
import io.hhplus.tdd.point.domain.model.TransactionType;
import io.hhplus.tdd.web.dto.response.PointHistoryResponseDto;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.point").value("100"));

    verify(pointReadingService).read(anyLong());
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

    verify(pointChargingService, times(1)).charge(anyLong(), anyLong());
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

    verify(pointUsingService, times(1)).use(anyLong(), anyLong());
  }

  @Test
  @DisplayName("포인트 사용 내역을 정상적으로 조회한다.")
  void readHistories() throws Exception {
    // given
    long id = 1L;
    long currentTimestamp = System.currentTimeMillis();
    when(pointHistoryReadingService.readHistories(id))
        .thenReturn(List.of(
                PointHistoryResponseDto.of(new PointHistory(1L, 1L, 100L, TransactionType.CHARGE,
                    currentTimestamp)),
                PointHistoryResponseDto.of(new PointHistory(2L, 1L, 100L, TransactionType.USE,
                    currentTimestamp))
            )
        );

    // when
    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .get("/points/" + id + "/histories")
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(StandardCharsets.UTF_8);

    // then
    mockMvc.perform(builder)
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id").value("1"))
        .andExpect(jsonPath("$[0].userId").value("1"))
        .andExpect(jsonPath("$[0].amount").value("100"))
        .andExpect(jsonPath("$[0].type").value(TransactionType.CHARGE.name()))
        .andExpect(jsonPath("$[0].updateMillis").value(currentTimestamp))
        .andExpect(jsonPath("$[1].id").value("2"))
        .andExpect(jsonPath("$[1].userId").value("1"))
        .andExpect(jsonPath("$[1].amount").value("100"))
        .andExpect(jsonPath("$[1].type").value(TransactionType.USE.name()))
        .andExpect(jsonPath("$[1].updateMillis").value(currentTimestamp));

    verify(pointHistoryReadingService).readHistories(anyLong());
  }
}
