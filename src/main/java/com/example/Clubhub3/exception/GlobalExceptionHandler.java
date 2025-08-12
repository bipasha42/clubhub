package com.example.Clubhub3.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice("com.yourproject.controller")
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e, Model model, RedirectAttributes redirectAttributes) {
        logger.error("Runtime exception occurred: ", e);
        
        // For AJAX requests or API calls, you might want to return JSON
        // For regular form submissions, redirect with error message
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/uniadmin/clubs";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e, Model model, RedirectAttributes redirectAttributes) {
        logger.error("General exception occurred: ", e);
        
        redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again.");
        return "redirect:/uniadmin/clubs";
    }
}


