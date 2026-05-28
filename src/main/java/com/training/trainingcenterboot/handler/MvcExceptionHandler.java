package com.training.trainingcenterboot.handler;

import com.training.trainingcenterboot.exception.DuplicateResourceException;
import com.training.trainingcenterboot.exception.ResourceNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.training.trainingcenterboot.controller.mvc")
public class MvcExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public String handleDuplicate(DuplicateResourceException e,
                                  Model model) {

        model.addAttribute("title", "Ошибка регистрации");

        model.addAttribute("message", e.getMessage());

        return "error/error-page";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException e,
                                 Model model) {

        model.addAttribute("title", "Ресурс не найден");

        model.addAttribute("message", e.getMessage());

        return "error/error-page";
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobal(Exception e,
                               Model model) {

        model.addAttribute("title", "Внутренняя ошибка");

        model.addAttribute("message", e.getMessage());

        return "error/error-page";
    }

    @ExceptionHandler(BindException.class)
    public String handleValidation(BindException e,
                                   Model model) {

        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Ошибка валидации");

        model.addAttribute("title", "Ошибка валидации");

        model.addAttribute("message", errorMessage);

        return "error/error-page";
    }
}