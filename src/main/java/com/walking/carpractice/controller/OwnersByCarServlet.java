package com.walking.carpractice.controller;

import com.walking.carpractice.service.CarService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/car/carOwners")
public class OwnersByCarServlet extends HttpServlet {
    private CarService carService;

    @Override
    public void init(){
        carService=(CarService) getServletContext().getAttribute("carService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var id=Long.parseLong(req.getParameter("id"));
        var answer=carService.getOwners(id);
        req.setAttribute("pojoResponseBody", answer);
        resp.setStatus(200);
    }
}
