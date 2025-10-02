package com.walking.carpractice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walking.carpractice.exception.ApplicationException;
import com.walking.carpractice.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class SerializationFilter extends HttpFilter {
    private ObjectMapper objectMapper;

    @Override
    public void init(){
        objectMapper =(ObjectMapper) getFilterConfig().getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        // Сначала выполняем следующие фильтры и логику сервлета
        chain.doFilter(req, res);

        // Получаем объект будущего тела ответа. Он должен был быть установлен в сервлете
        Object pojoBody = req.getAttribute("pojoResponseBody");

        // Если атрибута нет - вероятно, запрос возвращает не JSON. Так или иначе, сериализовать нечего
        if (pojoBody == null) {
            return;
        }

        try {
            // Сериализуем объект в JSON, представленный в виден массива байт
            byte[] jsonBody = objectMapper.writeValueAsBytes(pojoBody);

            // Можно сбросить состояние ответа. Но вдруг предыдущие фильтры или сам сервлет в нашей системе записывают
            // какие-то специфические заголовки?

            // Устанавливаем тип ответа
            res.setContentType("application/json");
            // Устанавливаем размер ответа. Больше для демонстрации, обычно это происходит автоматически по факту коммита ответа
            res.setContentLength(jsonBody.length);
            // Записываем JSON в тело ответа
            res.getOutputStream().write(jsonBody);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SERIALIZATION_ERROR, e);
        }
    }
}
