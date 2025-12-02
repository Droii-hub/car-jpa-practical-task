package com.walking.carpractice.controller;

import com.walking.carpractice.service.CarService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/car/find")
public class CarSearchServlet extends HttpServlet {
    private CarService carService;

    @Override
    public void init(){
        carService=(CarService) getServletContext().getAttribute("carService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var color=req.getParameter("color");
        var model=req.getParameter("model");
        var answer=carService.find(color, null, model);
        req.setAttribute("pojoResponseBody", answer);
        resp.setStatus(200);
    }
}
