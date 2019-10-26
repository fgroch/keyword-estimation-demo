package tools.blocks.estimateservice.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import tools.blocks.estimateservice.dto.EstimationDto;
import tools.blocks.estimateservice.dto.SuggestionDto;
import tools.blocks.estimateservice.dto.SuggestionResponseDto;
import tools.blocks.estimateservice.service.EstimateService;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class EstimateServiceImplTest {

    @Autowired
    EstimateService estimateService;

    @MockBean
    RestTemplate restTemplate;

    @TestConfiguration
    static class EstimateServiceImplTestContextConfiguration {
        @Bean
        public EstimateService estimateService() {
            return new EstimateServiceImpl();
        }
    }

    @Test
    public void shouldExecuteAmazonApi() {
        String keyword = "abc";
        given(restTemplate.exchange(
                "https://completion.amazon.com/api/2017/suggestions?page-type=Search&lop=en_US&site-variant=desktop&client-info=amazon-search-ui&mid=ATVPDKIKX0DER&alias=aps&b2b=0&fresh=0&ks=73&prefix={keyword}&event=onKeyPress&limit=10&fb=1&suggestion-type=KEYWORD&suggestion-type=WIDGET",
                    HttpMethod.GET,
                null,
                    SuggestionResponseDto.class,
                    keyword))
                .willReturn(createOkOneRowResponse(keyword));

        given(restTemplate.exchange(
                "https://completion.amazon.com/api/2017/suggestions?page-type=Search&lop=en_US&site-variant=desktop&client-info=amazon-search-ui&mid=ATVPDKIKX0DER&alias=aps&b2b=0&fresh=0&ks=73&prefix={keyword}&event=onKeyPress&limit=10&fb=1&suggestion-type=KEYWORD&suggestion-type=WIDGET",
                HttpMethod.GET,
                null,
                SuggestionResponseDto.class,
                keyword + "a"))
                .willReturn(createOkResponse(keyword));

        EstimationDto actualEstimationDto = estimateService.getCompletionSuggestion(keyword);
        assertEquals(keyword, actualEstimationDto.getKeyword());
        assertEquals(Integer.valueOf(5), actualEstimationDto.getScore());
    }

    private  ResponseEntity<SuggestionResponseDto> createOkResponse(String keyword) {
        SuggestionResponseDto suggestionResponseDto = new SuggestionResponseDto();
        ResponseEntity<SuggestionResponseDto> response = new ResponseEntity<>(suggestionResponseDto, HttpStatus.OK);
        return response;
    }

    private  ResponseEntity<SuggestionResponseDto> createOkOneRowResponse(String keyword) {
        SuggestionResponseDto suggestionResponseDto = new SuggestionResponseDto();
        SuggestionDto suggestionDto = new SuggestionDto();
        suggestionDto.setValue(keyword + "a");
        suggestionResponseDto.setSuggestions(Collections.singletonList(suggestionDto));
        ResponseEntity<SuggestionResponseDto> response = new ResponseEntity<>(suggestionResponseDto, HttpStatus.OK);
        return response;
    }
}