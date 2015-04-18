package com.chenxiaojie.lion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DivController {
    @RequestMapping("div/{path}/{filename}")
    public String div(HttpServletRequest request, @PathVariable String path, @PathVariable String filename, String[] params) {
        request.setAttribute("params", params);
        return "div/" + path + "/" + filename;
    }
}