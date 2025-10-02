package com.walking.carpractice.controller;

import com.walking.carpractice.dto.model.ModelCreateDto;
import com.walking.carpractice.dto.model.ModelUpdateDto;
import com.walking.carpractice.exception.ApplicationException;
import com.walking.carpractice.exception.ErrorCode;
import com.walking.carpractice.service.ModelService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/model")
public class ModelServlet extends HttpServlet {
    private ModelService modelService;

    @Override
    public void init(){modelService=(ModelService) getServletContext().getAttribute("modelService");}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var modelId=Long.parseLong(req.getParameter("id"));
        var answer=modelService.readById(modelId);
        req.setAttribute("pojoResponseBody", answer);
        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        ModelCreateDto modelCreateDto=(ModelCreateDto) req.getAttribute("pojoRequestBody");
        if (modelCreateDto==null)
            throw new ApplicationException(ErrorCode.EMPTY_REQUEST);
        var answer=modelService.create(modelCreateDto);
        req.setAttribute("pojoResponseBody", answer);
        resp.setStatus(201);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        ModelUpdateDto modelUpdateDto=(ModelUpdateDto) req.getAttribute("pojoRequestBody");
        if(modelUpdateDto==null)
            throw new ApplicationException(ErrorCode.EMPTY_REQUEST);
        var answer=modelService.update(modelUpdateDto);
        req.setAttribute("pojoResponseBody", answer);
        resp.setStatus(201);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        long modelId=Long.parseLong(req.getParameter("id"));
        modelService.delete(modelId);
        resp.setStatus(200);
    }
}
