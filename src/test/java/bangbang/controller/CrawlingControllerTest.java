package bangbang.controller;

import bangbang.service.CrawlingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CrawlingControllerTest {
    @InjectMocks
    private CrawlingController crawlingController;

    @Mock
    private CrawlingService crawlingService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // set validator
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(crawlingController)
                .setValidator(validator)
                .build();
    }

    @Test
    @DisplayName("정상적인 요청 - 크롤링 성공")
    void crawlPrice_Success() throws Exception {
        // given
        String accommodationId = "12345";
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = checkIn.plusDays(2);
        Long expectedPrice = 89000L;

        when(crawlingService.extractFinalPrice(anyString())).thenReturn(expectedPrice);

        // when & then
        mockMvc.perform(get("/api/crawl/price")
                        .param("accommodationId", accommodationId)
                        .param("checkIn", checkIn.toString())
                        .param("checkOut", checkOut.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.price").value(expectedPrice));
    }

    @Test
    @DisplayName("정상적인 요청 - 크롤링 실패 (가격 못찾음)")
    void crawlPrice_CrawlingFailed() throws Exception {
        // given
        String accommodationId = "12345";
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = checkIn.plusDays(2);

        when(crawlingService.extractFinalPrice(anyString())).thenReturn(-1L);

        // when & then
        mockMvc.perform(get("/api/crawl/price")
                        .param("accommodationId", accommodationId)
                        .param("checkIn", checkIn.toString())
                        .param("checkOut", checkOut.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("fail"));
    }

    // ============== Validation 테스트들 ==============

    @Test
    @DisplayName("Validation 실패 - accommodationId가 비어있음")
    void crawlPrice_EmptyAccommodationId() throws Exception {
        // given
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = checkIn.plusDays(2);

        // when & then
        mockMvc.perform(get("/api/crawl/price")
                        .param("accommodationId", "")
                        .param("checkIn", checkIn.toString())
                        .param("checkOut", checkOut.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Validation 실패 - accommodationId가 숫자가 아님")
    void crawlPrice_InvalidAccommodationId() throws Exception {
        // given
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = checkIn.plusDays(2);

        // when & then
        mockMvc.perform(get("/api/crawl/price")
                        .param("accommodationId", "abc123")
                        .param("checkIn", checkIn.toString())
                        .param("checkOut", checkOut.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Validation 실패 - checkIn이 과거 날짜")
    void crawlPrice_PastCheckInDate() throws Exception {
        // given
        String accommodationId = "12345";
        LocalDate checkIn = LocalDate.now().minusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(1);

        // when & then
        mockMvc.perform(get("/api/crawl/price")
                        .param("accommodationId", accommodationId)
                        .param("checkIn", checkIn.toString())
                        .param("checkOut", checkOut.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Validation 실패 - checkOut이 checkIn보다 빠름")
    void crawlPrice_CheckOutBeforeCheckIn() throws Exception {
        // given
        String accommodationId = "12345";
        LocalDate checkIn = LocalDate.now().plusDays(3);
        LocalDate checkOut = LocalDate.now().plusDays(1);

        // when & then
        mockMvc.perform(get("/api/crawl/price")
                        .param("accommodationId", accommodationId)
                        .param("checkIn", checkIn.toString())
                        .param("checkOut", checkOut.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Validation 실패 - 필수 파라미터 누락 (accommodationId)")
    void crawlPrice_MissingAccommodationId() throws Exception {
        // given
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = checkIn.plusDays(2);

        // when & then
        mockMvc.perform(get("/api/crawl/price")
                        .param("checkIn", checkIn.toString())
                        .param("checkOut", checkOut.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Validation 실패 - 필수 파라미터 누락 (checkIn)")
    void crawlPrice_MissingCheckIn() throws Exception {
        // given
        String accommodationId = "12345";
        LocalDate checkOut = LocalDate.now().plusDays(2);

        // when & then
        mockMvc.perform(get("/api/crawl/price")
                        .param("accommodationId", accommodationId)
                        .param("checkOut", checkOut.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Validation 실패 - 필수 파라미터 누락 (checkOut)")
    void crawlPrice_MissingCheckOut() throws Exception {
        // given
        String accommodationId = "12345";
        LocalDate checkIn = LocalDate.now().plusDays(1);

        // when & then
        mockMvc.perform(get("/api/crawl/price")
                        .param("accommodationId", accommodationId)
                        .param("checkIn", checkIn.toString())
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Validation 실패 - 잘못된 날짜 형식")
    void crawlPrice_InvalidDateFormat() throws Exception {
        // given
        String accommodationId = "12345";
        LocalDate checkOut = LocalDate.now().plusDays(2);

        // when & then
        mockMvc.perform(get("/api/crawl/price")
                        .param("accommodationId", accommodationId)
                        .param("checkIn", "2025/09/30")
                        .param("checkOut", checkOut.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("여러 Validation 오류가 함께 발생")
    void crawlPrice_MultipleValidationErrors() throws Exception {
        // given
        LocalDate checkIn = LocalDate.now().minusDays(1);
        LocalDate checkOut = checkIn.minusDays(1);

        // when & then
        mockMvc.perform(get("/api/crawl/price")
                        .param("accommodationId", "")
                        .param("checkIn", checkIn.toString())
                        .param("checkOut", checkOut.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
