package tools.blocks.estimateservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.blocks.estimateservice.dto.EstimationDto;
import tools.blocks.estimateservice.dto.SuggestionDto;
import tools.blocks.estimateservice.dto.SuggestionResponseDto;
import tools.blocks.estimateservice.service.EstimateService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EstimateServiceImpl implements EstimateService {

    @Autowired
    RestTemplate restTemplate;

    private static int FIRST_LEVEL_WEIGHT = 5;

    /**
     * Method for processing estimation requests
     * @param keyword keyword for search in amazon api
     * @return data object with result of estimation
     */
    @Override
    public EstimationDto getCompletionSuggestion(String keyword) {
        List<String> keywordsList = new ArrayList<>();
        keywordsList.addAll(
                getSuggestionsDto(keyword).stream()
                        .map(suggestionDto -> suggestionDto.getValue())
                        .filter(suggestion -> suggestion != null)
                        .collect(Collectors.toList())
        );
        int score = keywordsList.size() * FIRST_LEVEL_WEIGHT;
        List<String> list = new ArrayList();
        for (String s : keywordsList) {
            list.addAll(
                    getSuggestionsDto(s).parallelStream()
                            .map(suggestionDto -> suggestionDto.getValue())
                            .filter(suggestion -> suggestion != null)
                            .collect(Collectors.toList())
            );
        }
        score = score + (list.size() / 2);
        return new EstimationDto(keyword, score);
    }

    /**
     * Method for handling Amazon api calls
     * @param keyword  keyword for search
     * @return list of suggestion data objects
     */
    private List<SuggestionDto> getSuggestionsDto(String keyword) {
        List<SuggestionDto> suggestions = new ArrayList<>();
        try {
            ResponseEntity<SuggestionResponseDto> response = restTemplate.exchange(
                    "https://completion.amazon.com/api/2017/suggestions?page-type=Search&lop=en_US&site-variant=desktop&client-info=amazon-search-ui&mid=ATVPDKIKX0DER&alias=aps&b2b=0&fresh=0&ks=73&prefix={keyword}&event=onKeyPress&limit=10&fb=1&suggestion-type=KEYWORD&suggestion-type=WIDGET",
                    HttpMethod.GET,
                    null,
                    SuggestionResponseDto.class,
                    keyword);
            if (response != null && response.hasBody() && HttpStatus.OK.equals(response.getStatusCode())) {
                suggestions.addAll(response.getBody().getSuggestions());
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        return suggestions;
    }
}
