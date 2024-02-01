package com.rmnnorbert.dentocrates.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/** Controller class for handling requests related to the application's index or home page. */
@Controller
public class IndexController {
    /**
     * Handles GET requests for the root ("/") endpoint and returns the path to the index.html file.
     *
     * @return The path to the index.html file.
     */
    @GetMapping({"/"})
    public String index() {
        return "/index.html";
    }

}
