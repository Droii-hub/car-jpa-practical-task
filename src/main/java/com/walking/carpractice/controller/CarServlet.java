package com.walking.carpractice.controller;

import com.walking.carpractice.dto.car.CarCreateDto;
import com.walking.carpractice.dto.car.CarUpdateDto;
import com.walking.carpractice.exception.ApplicationException;
import com.walking.carpractice.exception.ErrorCode;
import com.walking.carpractice.service.CarService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/car")
public class CarServlet extends HttpServlet {
    private CarService carService;

    @Override
    public void init(){
        carService=(CarService) getServletContext().getAttribute("carService");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        CarCreateDto carCreateDto=(CarCreateDto)req.getAttribute("pojoRequestBody");
        if (carCreateDto==null)
            throw new ApplicationException(ErrorCode.EMPTY_REQUEST);

        var answer = carService.create(carCreateDto);
        req.setAttribute("pojoResponseBody", answer);
        resp.setStatus(201);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var id=Long.parseLong(req.getParameter("id"));
        var answer=carService.read(id);
        req.setAttribute("pojoResponseBody", answer);
        resp.setStatus(200);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        CarUpdateDto carUpdateDto=(CarUpdateDto)req.getAttribute("pojoRequestBody");
        if (carUpdateDto==null)
            throw new ApplicationException(ErrorCode.EMPTY_REQUEST);
        var answer = carService.update(carUpdateDto);
        req.setAttribute("pojoResponseBody", answer);
        resp.setStatus(200);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        var id=Long.parseLong(req.getParameter("id"));
        carService.delete(id);
        resp.setStatus(200);
    }
}
