package tools.blocks.estimateservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tools.blocks.estimateservice.dto.EstimationDto;
import tools.blocks.estimateservice.service.EstimateService;

@RestController
@RequestMapping(value="estimate")
public class EstimateController {

    @Autowired
    EstimateService estimateService;

    /**
     * Method for estimation request handling
     * @param keyword keyword for estimate
     * @return estimation data object, 404 HttpStatus if @param keyword is empty
     */
    @RequestMapping(value="/{keyword}",method = RequestMethod.GET)
    public EstimationDto hello(@PathVariable("keyword") String keyword) {
        return estimateService.getCompletionSuggestion(keyword);
    }
}
