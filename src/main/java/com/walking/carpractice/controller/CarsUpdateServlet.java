package com.walking.carpractice.controller;

import com.walking.carpractice.exception.ApplicationException;
import com.walking.carpractice.exception.ErrorCode;
import com.walking.carpractice.service.CarService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/car/disableTechnicalInspectionByYear")
public class CarsUpdateServlet extends HttpServlet {
    private CarService carService;
    private static final Logger log = LogManager.getLogger(CarsUpdateServlet.class);

    @Override
    public void init(){carService=(CarService) getServletContext().getAttribute("carService");}

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) {
        String year=req.getParameter("year");
        if (year==null)
            throw new ApplicationException(ErrorCode.EMPTY_REQUEST);
        carService.disableTechnicalInspectionByYear(Integer.parseInt(year));
        resp.setStatus(200);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp){
        String method=req.getMethod();
        if (method.equals("PATCH"))
            this.doPatch(req,resp);
    }
}
