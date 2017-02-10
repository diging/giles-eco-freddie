package edu.asu.diging.gilesecosystem.freddie.web;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.gilesecosystem.freddie.core.exception.SearchQueryException;
import edu.asu.diging.gilesecosystem.freddie.core.model.IDocument;
import edu.asu.diging.gilesecosystem.freddie.core.service.ISolrService;

@Controller
public class SearchController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ISolrService service;
    
    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String showPage(String searchTerm) {
        return "search";
    }
    
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@RequestParam String searchTerm, Model model, Locale locale) {
        List<IDocument> docs;
        try {
            docs = service.find(searchTerm);
            model.addAttribute("results", docs);
        } catch (SearchQueryException e) {
            logger.error("Could not execute search query.", e);
            model.addAttribute("show_alert", true);
            model.addAttribute("alert_type", "warning");
            model.addAttribute("alert_msg", messageSource.getMessage("search.query.error", null, locale));
        }
        return "search";
    }
}
