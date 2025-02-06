package pe.com.mibanco.library.util;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.com.mibanco.library.monitoring.LogsMonitoring;
import pe.com.mibanco.library.monitoring.structure.LogsRequestStructure;
import java.time.LocalDateTime;

@Dependent
public class LoggingUtil {
    @Context
    ContainerRequestContext request;
    @RestClient
    LogsMonitoring logsRestClient;
    private final Logger logger;

    @Inject
    public LoggingUtil(InjectionPoint injectionPoint) {
        Class<?> parentClass = injectionPoint.getMember().getDeclaringClass();
        this.logger = LoggerFactory.getLogger(parentClass);
    }

    public void info(String message) { logger.info("\n\n" + message); }

    public void info(String message, Object... var2) { logger.info("\n\n" + message, var2); }
    public void debug(String message, Object... var2) { logger.debug("\n\n" + message, var2); }

    public void info(String message, Throwable var2) { logger.info("\n\n" + message, var2); }

    public void warn(String message) { logger.warn("\n\n" + message); }



    public void error(String message) {
        logsRestClient.registerLog(new LogsRequestStructure(
                        request.getHeaderString("transaction-id"),
                        LocalDateTime.now().toString(),
                        request.getUriInfo().getPath(),
                        message

                ))

                .subscribe().with(
                        success -> logger.info("Se envió log correctamente"),
                        error -> logger.error("No envió log correctamente")
                );

        logger.error("\n\n" + message);
    }



    public void error(String var1, Throwable var2) {

        logger.error(var1, var2);

    }
}
