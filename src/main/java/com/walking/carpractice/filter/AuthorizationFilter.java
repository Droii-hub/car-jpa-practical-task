package com.walking.carpractice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AuthorizationFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if ("/login".equals(request.getServletPath())|"/registration".equals(request.getServletPath())
        |"/auth".equals(request.getServletPath())) {
            // Если запрос на логин, страницу входа или регистрацию - пускаем дальше по цепочке без дополнительных проверок
            chain.doFilter(request, response);
            return;
        }
        // Получаем объект сессии. Если сессии не существует - перенаправляем на страницу входа.
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("./login");
            return;
        }
        chain.doFilter(request,response);
    }
}
