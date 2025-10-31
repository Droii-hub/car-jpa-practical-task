package com.walking.carpractice.controller;

import com.walking.carpractice.service.OwnerService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/statistic/carbyowner")
public class CarByOwnerServlet extends HttpServlet {
    private OwnerService ownerService;

    @Override
    public void init(){ownerService=(OwnerService) getServletContext().getAttribute("ownerService");}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        var answer=ownerService.carsByOwner();
        req.setAttribute("pojoResponseBody", answer);
        resp.setStatus(200);
    }
}
