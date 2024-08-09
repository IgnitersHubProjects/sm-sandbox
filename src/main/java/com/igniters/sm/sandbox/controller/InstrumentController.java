package com.igniters.sm.sandbox.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.igniters.sm.sandbox.model.Instrument;
import com.igniters.sm.sandbox.service.impl.JsonParserService;

import lombok.extern.slf4j.Slf4j;

//@RestController
@Slf4j
@Controller
public class InstrumentController {
    @Autowired
    private JsonParserService jsonParserService;

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        query = query.toUpperCase();
        List<Instrument> list = jsonParserService.getInstrumentsFromRedis(query);
        System.out.println("ist size if " + list.size());
        model.addAttribute("result", list);

        return "fragment/searchResult :: results";
    }

    @GetMapping("/getDetails/{key}")
    public String getDetails(@PathVariable String key, Model model) {
        Instrument instrument = jsonParserService.findByKey(key);
        model.addAttribute("instrument", instrument);
        System.out.println(instrument.toString());
        return "fragment/details :: details";
    }

}
