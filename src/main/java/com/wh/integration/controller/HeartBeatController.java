package com.wh.integration.controller;

import com.wh.integration.model.xml.TwitterMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by joseblas on 26/10/14.
 */
@RestController
public class HeartBeatController {

    @RequestMapping("/hb")
    public TwitterMessage heartbeat() {
        System.out.println("OK");
        return new TwitterMessage();
    }
}
