package com.walking.carpractice.controller;

import com.walking.carpractice.exception.ApplicationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet("/error")
public class ExceptionHandlingServlet extends HttpServlet {
    public static final String ERROR_ATTRIBUTE_KEY = "jakarta.servlet.error.exception";
    public static final String ERROR_MESSAGE_ATTRIBUTE_KEY = "jakarta.servlet.error.message";
    public static final String STATUS_CODE_ATTRIBUTE_KEY = "jakarta.servlet.error.status_code";
    public static final String REQUEST_URI_ATTRIBUTE_KEY = "jakarta.servlet.error.request_uri";

    private static final Logger log = LogManager.getLogger(ExceptionHandlingServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var httpCode=(int)req.getAttribute(STATUS_CODE_ATTRIBUTE_KEY);

        if (httpCode==404){
            log.error("Ресурс не найден. Адрес запроса: {}", req.getAttribute(REQUEST_URI_ATTRIBUTE_KEY));
            req.getRequestDispatcher("/notFound").forward(req,resp);
            return;
        }

        var exception=(Throwable)req.getAttribute(ERROR_ATTRIBUTE_KEY);


        if (!(exception instanceof ApplicationException applicationException)){
            log.error("Неизвестная ошибка", exception);
            req.getRequestDispatcher("/internalError").forward(req,resp);
            return;
        }

        log.error(applicationException.getErrorCode().getMessage(), applicationException.getCause());
        req.setAttribute(ERROR_MESSAGE_ATTRIBUTE_KEY, applicationException.getErrorCode().getMessage());



        if (applicationException.getErrorCode().getHttpCode()>=400&
                applicationException.getErrorCode().getHttpCode()<500){
            req.getRequestDispatcher("/requestError").forward(req,resp);
            return;
        }
        req.getRequestDispatcher("/internalError").forward(req,resp);
    }

}
