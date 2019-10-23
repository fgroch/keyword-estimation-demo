package tools.blocks.estimateservice.service;

import tools.blocks.estimateservice.dto.EstimationDto;

public interface EstimateService {

    public EstimationDto getCompletionSuggestion(String keyword);
}