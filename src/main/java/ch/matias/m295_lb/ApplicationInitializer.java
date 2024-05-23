package ch.matias.m295_lb;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationInitializer
        implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext)
            throws ServletException {

        AnnotationConfigWebApplicationContext context
                = new AnnotationConfigWebApplicationContext();

        context.register(JpaConfig.class);
        context.register(RestConfig.class);

        servletContext.addListener(new ContextLoaderListener(context));
        servletContext.setInitParameter(
                "contextConfigLocation", "ch.matias.m295_lb");
    }
}