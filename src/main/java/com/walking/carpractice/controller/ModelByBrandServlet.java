package com.walking.carpractice.controller;

import com.walking.carpractice.service.BrandService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/model/byBrand")
public class ModelByBrandServlet extends HttpServlet {
    private BrandService brandService;

    @Override
    public void init(){
        brandService =(BrandService) getServletContext().getAttribute("brandService");}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var brandId=Long.parseLong(req.getParameter("id"));
        var answer= brandService.getModels(brandId);
        req.setAttribute("pojoResponseBody", answer);
        resp.setStatus(200);
    }
}
