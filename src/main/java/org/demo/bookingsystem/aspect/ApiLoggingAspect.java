/**
 * Author: sthk
 * Created At: June 7, 2025
 * <p>
 * This class is an aspect for logging API requests and responses in the application.
 * It uses Spring AOP (Aspect-Oriented Programming) to intercept controller methods
 * and log the payloads of incoming requests and outgoing responses.
 */

package org.demo.bookingsystem.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Author: sthk
 * Created At: June 7, 2025
 * <p>
 * Aspect for logging API requests and responses for all methods in the controllers.
 */
@Aspect
@Component
public class ApiLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ApiLoggingAspect.class); // Logger instance
    private final ObjectMapper objectMapper; // ObjectMapper to convert objects to JSON strings

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Constructor to inject ObjectMapper dependency.
     *
     * @param objectMapper the ObjectMapper for JSON serialization.
     */
    public ApiLoggingAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Logs API details and request payloads before the execution of any method in the controller package.
     *
     * @param joinPoint provides details about the intercepted method call.
     */
    @Before("execution(* org.demo.bookingsystem.controller.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        // Log the API method being executed
        logger.info("Executing API: {}", joinPoint.getSignature().toShortString());

        // Log each argument passed to the API method
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                try {
                    // Convert the argument to a JSON string and log it
                    String jsonPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(arg);
                    logger.info("Request Payload: {}", jsonPayload);
                } catch (Exception e) {
                    // Log an error if JSON conversion fails
                    logger.error("Error logging request payload", e);
                }
            }
        }
    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Logs API details and response payloads after the successful execution of any method in the controller package.
     *
     * @param joinPoint provides details about the intercepted method call.
     * @param result    the response returned by the method.
     */
    @AfterReturning(pointcut = "execution(* org.demo.bookingsystem.controller.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        // Log the API method that was executed
        logger.info("API Executed: {}", joinPoint.getSignature().toShortString());

        try {
            // Convert the response to a JSON string and log it
            String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            logger.info("Response: {}", jsonResponse);
        } catch (Exception e) {
            // Log an error if JSON conversion fails
            logger.error("Error logging response", e);
        }
    }
}