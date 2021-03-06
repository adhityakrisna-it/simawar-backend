package id.seringiskering.simawar.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;
import java.util.Objects;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth0.jwt.exceptions.TokenExpiredException;

import id.seringiskering.simawar.domain.HttpResponse;
import id.seringiskering.simawar.exception.domain.DataNotFoundException;
import id.seringiskering.simawar.exception.domain.EmailExistException;
import id.seringiskering.simawar.exception.domain.EmailNotFoundException;
import id.seringiskering.simawar.exception.domain.InvalidDataException;
import id.seringiskering.simawar.exception.domain.NotAnImageFileException;
import id.seringiskering.simawar.exception.domain.UnauthorizedException;
import id.seringiskering.simawar.exception.domain.UserNotFoundException;
import id.seringiskering.simawar.exception.domain.UsernameExistException;


@RestControllerAdvice
public class ExceptionHandling implements ErrorController {

	/**
	 *  handle all the exception
	 * * */
	
	private final Logger  LOGGER = LoggerFactory.getLogger(getClass());
	
    private static final String ACCOUNT_LOCKED = "Your account has been locked. Please contact administration";
    private static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed on this endpoint. Please send a '%s' request";
    private static final String INTERNAL_SERVER_ERROR_MSG = "An error occurred while processing the request";
    private static final String INCORRECT_CREDENTIALS = "Username / password incorrect. Please try again";
    private static final String ACCOUNT_DISABLED = "Your account has been disabled. If this is an error, please contact administration";
    private static final String ERROR_PROCESSING_FILE = "Error occurred while processing file";
    private static final String NOT_ENOUGH_PERMISSION = "You do not have enough permission";
    
    public  static final String ERROR_PATH = "/error";
    
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisabledException() {
    	return createHttpResponse(HttpStatus.BAD_REQUEST, ACCOUNT_DISABLED);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException() {
        return createHttpResponse(BAD_REQUEST, INCORRECT_CREDENTIALS);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException() {
        return createHttpResponse(FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> lockedException() {
        return createHttpResponse(UNAUTHORIZED, ACCOUNT_LOCKED);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException exception) {
        return createHttpResponse(UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<HttpResponse> emailExistException(EmailExistException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UsernameExistException.class)
    public ResponseEntity<HttpResponse> usernameExistException(UsernameExistException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
    }
    
	/*
	 * @ExceptionHandler(NoHandlerFoundException.class) public
	 * ResponseEntity<HttpResponse>
	 * methodNotSupportedException(NoHandlerFoundException exception) { return
	 * createHttpResponse(BAD_REQUEST, "This page was not found"); }
	 */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception) {
        LOGGER.error(exception.getMessage());
        exception.printStackTrace();
        return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }
    
    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse> notFoundException(NoResultException exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(NOT_FOUND, exception.getMessage());
    }
    
    @ExceptionHandler(NotAnImageFileException.class)
    public ResponseEntity<HttpResponse> NotAnImageFileException(NotAnImageFileException exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
        
    
    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpResponse> iOException(IOException exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }    

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<HttpResponse> notFound404()  {
        return createHttpResponse(NOT_FOUND, "There is no mapping for this url");
    }    
    
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<HttpResponse> dataNotFound404(DataNotFoundException exception)  {
        return createHttpResponse(NOT_FOUND, exception.getMessage());
    }        

    
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<HttpResponse> unAuthorizedException(UnauthorizedException exception) {
        return createHttpResponse(UNAUTHORIZED, exception.getMessage());
    }
    
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<HttpResponse> invalidDataException(InvalidDataException exception) {
    	return createHttpResponse(INTERNAL_SERVER_ERROR, exception.getMessage());
    }
    
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<HttpResponse> noHandlerFoundException(NoHandlerFoundException e) {
//      return createHttpResponse(BAD_REQUEST, "There is no mapping for this URL");
//    }    
//    
    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
    	/**HttpResponse httpResponse = new HttpResponse(
    									httpStatus.value(), 
    									httpStatus, 
    									httpStatus.getReasonPhrase().toUpperCase(), 
    									message.toUpperCase());
    	
    	*/
    	
    	//return new ResponseEntity<HttpResponse>(httpResponse, httpStatus);
    	
    	return new ResponseEntity<HttpResponse>(new HttpResponse(
				httpStatus.value(), 
				httpStatus, 
				httpStatus.getReasonPhrase().toUpperCase(), 
				message), httpStatus);
    	
    }

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return ERROR_PATH;
	}
	
}
