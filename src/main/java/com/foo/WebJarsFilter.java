package com.foo;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import static java.lang.String.format;

@ConditionalOnClass(name = "org.webjars.WebJarAssetLocator" )
@Component
public class WebJarsFilter implements Filter {

    private final static String WEBJARS_LOCATION = "META-INF/resources/webjars/";
    private final static String WEBJARS_PATH = "/webjars";

    private final static int WEBJARS_LOCATION_LENGTH = WEBJARS_LOCATION.length();
    private final static int WEBJARS_PATH_LENGTH = WEBJARS_PATH.length();

    protected final Log logger = LogFactory.getLog(getClass());
    
    private Object webJarAssetLocator;
    private Method getFullPathMethod;

    public WebJarsFilter() {
        try {
            Class<?> cl = Class.forName("org.webjars.WebJarAssetLocator");
            webJarAssetLocator = cl.newInstance();
            getFullPathMethod = cl.getMethod("getFullPath", String.class, String.class);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new IllegalStateException("Could not create org.webjars.WebJarAssetLocator", e);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Couldn't find getFullPath(String, String) on WebJarAssetLocator. Perhaps the API has changed.", e);
        }
    }

    @Override
    public void init (FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter (ServletRequest request,
                          ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
        } catch (final ClassCastException e) {
            throw new ServletException("non-HTTP request or response", e);
        }
    }
    
    private void doFilter(HttpServletRequest request, HttpServletResponse response,
                          FilterChain chain) throws IOException, ServletException {
        String expandedPath = expandedPath(request);
        if (Objects.equals(request.getRequestURI(), expandedPath)) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect(expandedPath);
        }
    }
    
    protected String expandedPath(HttpServletRequest request) {
        String webJarResourcePath = findWebJarResourcePath(
                request.getRequestURI().substring(WEBJARS_PATH_LENGTH));
        return format("%s/%s", WEBJARS_PATH, webJarResourcePath);
    }

    protected String findWebJarResourcePath(String path) {
        try {
            int startOffset = (path.startsWith("/") ? 1 : 0);
            int endOffset = path.indexOf("/", 1);
            if (endOffset != -1) {
                String webjar = path.substring(startOffset, endOffset);
                String partialPath = path.substring(endOffset);
                String webJarPath = (String) getFullPathMethod.invoke(webJarAssetLocator, webjar, partialPath);
                return webJarPath.substring(WEBJARS_LOCATION_LENGTH);
            }
        }
        catch (IllegalArgumentException ex) {
            if (logger.isTraceEnabled()) {
                logger.trace("No WebJar resource found for \"" + path + "\"");
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Problem using WebJarAssetLocator", e);
            }
        }
        return null;
    }

    @Override
    public void destroy () {

    }
}
