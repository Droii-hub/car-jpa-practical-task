package com.walking.carpractice.controller;

import com.walking.carpractice.dto.owner.OwnerCreateDto;
import com.walking.carpractice.exception.ApplicationException;
import com.walking.carpractice.exception.ErrorCode;
import com.walking.carpractice.service.OwnerService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    private OwnerService ownerService;

    @Override
    public void init(){
        ownerService =(OwnerService) getServletContext().getAttribute("ownerService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        OwnerCreateDto owner=(OwnerCreateDto) req.getAttribute("pojoRequestBody");
        if (owner==null){
            throw new ApplicationException(ErrorCode.EMPTY_REQUEST);
        }
        ownerService.create(owner);
        resp.setStatus(201);
    }
}
