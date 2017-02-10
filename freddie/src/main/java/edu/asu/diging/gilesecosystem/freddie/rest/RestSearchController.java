package edu.asu.diging.gilesecosystem.freddie.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.asu.diging.gilesecosystem.freddie.core.exception.SearchQueryException;
import edu.asu.diging.gilesecosystem.freddie.core.model.IDocument;
import edu.asu.diging.gilesecosystem.freddie.core.service.ISolrService;

@RestController
public class RestSearchController {
    
    @Autowired
    private ISolrService indexService;

    @RequestMapping(value="/search", method=RequestMethod.GET)
    public List<IDocument> search(@RequestParam("q") String query) throws SearchQueryException {
        return indexService.find(query);
    }
}
