package filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Provider
public class LoggingFilter  implements ContainerRequestFilter, ContainerResponseFilter {

    Logger LOGGER;

    public LoggingFilter() throws IOException {
        //TODO zmiana ścieżki
        FileHandler handler = new FileHandler("C:\\Users\\48502\\Desktop\\semestr\\rsi\\projekt rest\\kinoService\\default.log", true);
        LOGGER = Logger.getLogger(LoggingFilter.class.getName());
        LOGGER.addHandler(handler);
        handler.setFormatter(new SimpleFormatter());
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        LOGGER.info("Request headers: " + String.valueOf(containerRequestContext.getHeaders()));
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        LOGGER.info("Response headers: " + String.valueOf(containerResponseContext.getHeaders()));
    }
}
