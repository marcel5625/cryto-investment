package com.marcel.crypto.investment.controller;

import com.mongodb.lang.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Optional;

@ControllerAdvice
public class EmptyOptionalResponseBodyControllerAdvice implements ResponseBodyAdvice<Optional<?>> {

    @Override
    public boolean supports(MethodParameter returnType, @Nullable Class<? extends HttpMessageConverter<?>> converterType) {
        return Optional.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Optional<?> beforeBodyWrite(Optional<?> body, @Nullable MethodParameter returnType, @Nullable MediaType selectedContentType,
                                       @Nullable Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                       @Nullable ServerHttpRequest request, @Nullable ServerHttpResponse response) {
        if (body.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return body;
    }
}