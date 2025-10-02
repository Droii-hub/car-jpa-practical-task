package com.walking.carpractice.controller;

import com.walking.carpractice.PasswordProvider;
import com.walking.carpractice.dto.owner.OwnerCreateDto;
import com.walking.carpractice.exception.ApplicationException;
import com.walking.carpractice.exception.ErrorCode;
import com.walking.carpractice.service.OwnerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/auth")
public class LoginServlet extends HttpServlet {
    private OwnerService ownerService;

    @Override
    public void init(){
        ownerService =(OwnerService) getServletContext().getAttribute("ownerService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        OwnerCreateDto credentials=null;
        if ("application/json".equals(req.getContentType())) {
            credentials = (OwnerCreateDto) req.getAttribute("pojoRequestBody");
        } else if ("application/x-www-form-urlencoded".equals(req.getContentType())) {
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            if (email != null & password != null)
                credentials = new OwnerCreateDto(email, password);
        }
        if (credentials==null)
            throw new ApplicationException(ErrorCode.EMPTY_REQUEST);

        var userData = ownerService.read(credentials.getEmail());
        if (!PasswordProvider.checkPassword(credentials.getPassword(), userData.getPassword()))
            throw new ApplicationException(ErrorCode.WRONG_PASSWORD);

        HttpSession session=req.getSession();
        session.setAttribute("email",userData.getEmail());
        session.setAttribute("id", userData.getId());
        resp.setStatus(200);
    }
}
