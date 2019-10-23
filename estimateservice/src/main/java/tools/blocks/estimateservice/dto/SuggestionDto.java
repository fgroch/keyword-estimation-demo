package tools.blocks.estimateservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuggestionDto implements Serializable {

    private String suggType;
    private String type;
    private String value;
    private String refTag;
    private Boolean ghost;
    private Boolean help;
    private Boolean fallback;
    private Boolean spellCorrected;
    private Boolean blackListed;
    private Boolean xcatOnly;
}
