package study.board.api.advice;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import study.board.api.advice.exception.CNotOwnerException;
import study.board.api.advice.exception.CResourceNotExistException;
import study.board.api.model.response.CommonResult;
import study.board.api.service.ResponseService;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {

    private final ResponseService responseService;
    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")),
                getMessage("unKnown.msg") + "(" + e.getMessage() + ")");
    }

    @ExceptionHandler(CNotOwnerException.class)
    @ResponseStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
    public CommonResult notOwnerException(HttpServletRequest request, CNotOwnerException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("notOwner.code")),
                getMessage("notOwner.msg"));
    }

    @ExceptionHandler(CResourceNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult resourceNotExistException(HttpServletRequest request, CNotOwnerException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("resourceNotExist.code")),
                getMessage("resourceNotExist.msg"));
    }

    private String getMessage(String code) {
        return getMessage(code, null);
    }

    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
