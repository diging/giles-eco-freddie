package edu.asu.diging.gilesecosystem.freddie.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.gilesecosystem.freddie.core.model.impl.Document;
import edu.asu.diging.gilesecosystem.freddie.core.repository.DocumentRepository;

@Controller
public class SearchController {
    
    @Autowired
    private DocumentRepository repository;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String showPage(String searchTerm) {
        return "search";
    }
    
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@RequestParam String searchTerm, Model model) {
        List<Document> docs = repository.findByContent(searchTerm);
        model.addAttribute("results", docs);
        return "search";
    }
}
