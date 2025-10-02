package com.walking.carpractice.controller;

import com.walking.carpractice.service.ModelService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/model/byBrand")
public class ModelByBrandServlet extends HttpServlet {
    private ModelService modelService;

    @Override
    public void init(){modelService=(ModelService) getServletContext().getAttribute("modelService");}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var brandId=Long.parseLong(req.getParameter("id"));
        var answer=modelService.readByBrand(brandId);
        req.setAttribute("pojoResponseBody", answer);
        resp.setStatus(200);
    }
}
