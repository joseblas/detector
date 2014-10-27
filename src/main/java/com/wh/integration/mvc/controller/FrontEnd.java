package com.wh.integration.mvc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FrontEnd {

	@RequestMapping("/hb")
    public String greeting() {
        return "OK";
    }
	
}
