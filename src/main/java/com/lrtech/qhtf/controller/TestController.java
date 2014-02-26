package com.lrtech.qhtf.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 14-2-25.
 *
 * @author leizhimin 14-2-25 上午10:14
 */
public class TestController implements Controller{
    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        System.out.println("Hello,Spring MVC!");
        return new ModelAndView("/test");
    }
}
