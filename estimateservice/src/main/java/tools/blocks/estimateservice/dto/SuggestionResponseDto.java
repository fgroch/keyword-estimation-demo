package tools.blocks.estimateservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuggestionResponseDto implements Serializable {
    private String alias;
    private String prefix;
    private String suffix;
    private List<SuggestionDto> suggestions;
    private String suggestionTitleId;
    private String responseId;
    private Boolean shuffled;
}
