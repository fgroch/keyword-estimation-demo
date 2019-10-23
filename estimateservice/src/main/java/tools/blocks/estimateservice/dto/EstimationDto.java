package tools.blocks.estimateservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstimationDto implements Serializable {
    private String keyword;
    private Integer score;
}
