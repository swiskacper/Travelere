package swis.kacper.start.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@org.springframework.stereotype.Controller
@RequestMapping("/map")
public class MapController {

    @GetMapping
    public String getMap(){
        return "src/main/ui/";
    }
}
