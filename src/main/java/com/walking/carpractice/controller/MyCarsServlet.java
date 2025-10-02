package com.walking.carpractice.controller;

import com.walking.carpractice.service.OwnerService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/car/myCars")
public class MyCarsServlet extends HttpServlet {
    private OwnerService ownerService;

    @Override
    public void init(){
        ownerService=(OwnerService) getServletContext().getAttribute("ownerService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session=req.getSession(false);
        var id=(long)session.getAttribute("id");
        var answer=ownerService.getCars(id);
        req.setAttribute("pojoResponseBody", answer);
        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        HttpSession session=req.getSession(false);
        var ownerId=(long)session.getAttribute("id");
        var carId=Long.parseLong(req.getParameter("id"));
        ownerService.addCar(carId, ownerId);
        resp.setStatus(200);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp){
        HttpSession session=req.getSession(false);
        var ownerId=(long)session.getAttribute("id");
        var carId=Long.parseLong(req.getParameter("id"));
        ownerService.dropCar(carId, ownerId);
        resp.setStatus(200);
    }
}
