package app.utils.aop;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestValidationAspect {

    @Before("execution(* app.controllers.rest..*Controller.*(..)) && args(page, size, ..)")
    public void validatePageAndSize(Integer page, Integer size) {

        if ((page != null && page < 0) || (size != null && size < 1)) {
            throw new IllegalArgumentException("Invalid page or size");
        }
    }
}
