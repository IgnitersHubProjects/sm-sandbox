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



import lombok.extern.slf4j.Slf4j;

//@RestController
@Slf4j
@Controller
public class InstrumentController {
    @Autowired
    private JsonParserService jsonParserService;

    //! to save data in db 
    @GetMapping("/parsed")
    public String getParsedInstruments() {
        try {           
            List<Instrument> list= jsonParserService.getParsedInstruments();
            System.out.println("Succesfull parsed " + list.size());
            return "redirect:/home";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/home";
        }
    }

    //! Temporary
    @GetMapping("/getAll/{pattern}")
    public List<Instrument> getInstrumentsByPattern(@PathVariable String pattern) {
        return jsonParserService.getInstrumentsFromRedis(pattern);
    }

    //! Temporary
    @GetMapping("/getData/{key}")
    public Instrument getInstrumentByKey(@PathVariable String key) {
        return jsonParserService.findByKey(key);
    }


    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model){
        query = query.toUpperCase();
        List<Instrument> list = jsonParserService.getInstrumentsFromRedis(query);
        System.out.println("ist size if " + list.size());
        model.addAttribute("result", list);
        
        return "fragment/searchResult :: results";
    }

    @GetMapping("/getDetails/{key}")
    public String getDetails(@PathVariable String key, Model model){
        Instrument instrument = jsonParserService.findByKey(key);
        model.addAttribute("instrument", instrument);
        System.out.println(instrument.toString());
        return "fragment/details :: details";
    }


    

}
