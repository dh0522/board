package pj.librarymanage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // index.html을 찾아간다.
    @GetMapping("/")
    public String index(){
        return "index";
    }
}
