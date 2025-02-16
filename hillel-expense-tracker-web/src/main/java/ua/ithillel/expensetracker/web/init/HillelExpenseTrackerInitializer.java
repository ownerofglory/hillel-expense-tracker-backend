package ua.ithillel.expensetracker.web.init;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Slf4j
public class HillelExpenseTrackerInitializer implements WebApplicationInitializer {
    private static final String TMP_FOLDER = "/tmp";
    private static final int MAX_UPLOAD_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        log.info("Staring app context");
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.scan("ua.ithillel.expensetracker");

        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        ServletRegistration.Dynamic register = servletContext.addServlet("dispatcherServlet", dispatcherServlet);

        register.setAsyncSupported(true);
        register.setLoadOnStartup(1);
        register.addMapping("/api/*");

        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(TMP_FOLDER,
                MAX_UPLOAD_SIZE, MAX_UPLOAD_SIZE * 2L, MAX_UPLOAD_SIZE / 2);

        register.setMultipartConfig(multipartConfigElement);

        log.info("App context initialized");
    }
}
