package com.igniters.sm.sandbox.json_redis;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@Controller
public class InstrumentController {
    @Autowired
    private JsonParserService jsonParserService;

    @GetMapping("/parsed")
    public List<Instrument> getParsedInstruments() {
        try {           
            List<Instrument> list= jsonParserService.getParsedInstruments();
            System.out.println("Succesfull parsed " + list.size());
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }


    @GetMapping("/getAll/{pattern}")
    public List<Instrument> getInstrumentsByPattern(@PathVariable String pattern) {
        return jsonParserService.getInstrumentsFromRedis(pattern);
    }

    @GetMapping("/getData/{key}")
    public Instrument getInstrumentByKey(@PathVariable String key) {
        return jsonParserService.findByKey(key);
    }


    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model){
        query = query.toUpperCase();
        List<Instrument> list = jsonParserService.getInstrumentsFromRedis(query);
        model.addAttribute("result", list);
        System.out.println("list size " + list.size());
        for(Instrument ins : list){
            System.out.println(ins.getDescription());
        }

        return "fragment/searchResult :: results";
    }
}
