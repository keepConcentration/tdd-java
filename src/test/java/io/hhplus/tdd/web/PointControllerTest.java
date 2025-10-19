package io.hhplus.tdd.web;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.hhplus.tdd.point.application.PointChargingService;
import io.hhplus.tdd.point.application.PointReadingService;
import io.hhplus.tdd.point.application.PointUsingService;
import java.nio.charset.StandardCharsets;
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
}
