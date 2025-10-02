package com.walking.carpractice.controller;

import com.walking.carpractice.dto.brand.BrandCreateDto;
import com.walking.carpractice.dto.brand.BrandUpdateDto;
import com.walking.carpractice.exception.ApplicationException;
import com.walking.carpractice.exception.ErrorCode;
import com.walking.carpractice.service.BrandService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/brand")
public class BrandServlet extends HttpServlet {
    private BrandService brandService;

    @Override
    public void init(){brandService=(BrandService) getServletContext().getAttribute("brandService");}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var answer=brandService.read();
        req.setAttribute("pojoResponseBody", answer);
        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        BrandCreateDto brandCreateDto=(BrandCreateDto) req.getAttribute("pojoRequestBody");
        if (brandCreateDto==null)
            throw new ApplicationException(ErrorCode.EMPTY_REQUEST);
        var answer=brandService.create(brandCreateDto);
        req.setAttribute("pojoResponseBody", answer);
        resp.setStatus(201);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        BrandUpdateDto brandUpdateDto=(BrandUpdateDto) req.getAttribute("pojoRequestBody");
        if (brandUpdateDto==null)
            throw new ApplicationException(ErrorCode.EMPTY_REQUEST);
        var answer=brandService.update(brandUpdateDto);
        req.setAttribute("pojoResponseBody", answer);
        resp.setStatus(200);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        var id=Long.parseLong(req.getParameter("id"));
        brandService.delete(id);
        resp.setStatus(200);
    }
}
