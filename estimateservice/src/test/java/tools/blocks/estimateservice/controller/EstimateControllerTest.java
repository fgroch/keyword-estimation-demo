package tools.blocks.estimateservice.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import tools.blocks.estimateservice.dto.EstimationDto;
import tools.blocks.estimateservice.service.EstimateService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EstimateController.class)
public class EstimateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RestTemplate restTemplate;

    @MockBean
    private EstimateService estimateService;

    @Test
    public void shouldReturnEstimation() throws Exception {
        String keyword = "abc";
        EstimationDto actualEstimationDto = new EstimationDto();
        actualEstimationDto.setKeyword(keyword);
        actualEstimationDto.setScore(5);

        given(estimateService.getCompletionSuggestion(keyword)).willReturn(actualEstimationDto);

        mockMvc.perform(get("/estimate/{keyword}", keyword)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keyword").value(keyword))
                .andExpect(jsonPath("$.score").value(5));
    }

    @Test
    public void shouldReturn404() throws Exception {
        String keyword = "";
        EstimationDto actualEstimationDto = new EstimationDto();
        actualEstimationDto.setKeyword(keyword);
        actualEstimationDto.setScore(5);

        given(estimateService.getCompletionSuggestion(keyword)).willReturn(actualEstimationDto);

        mockMvc.perform(get("/estimate/{keyword}", keyword)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}