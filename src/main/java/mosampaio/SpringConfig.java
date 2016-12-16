package mosampaio;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.AdminServlet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import com.ryantenney.metrics.spring.servlets.MetricsServletsContextListener;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableMetrics
public class SpringConfig extends MetricsConfigurerAdapter {

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        registerReporter(ConsoleReporter
                .forRegistry(metricRegistry)
                .build())
                .start(30, TimeUnit.SECONDS);

        registerReporter(JmxReporter.
                forRegistry(metricRegistry)
                .build())
                .start();
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
            return new ServletRegistrationBean(new AdminServlet(),"/metrics/admin/*");
    }

    @Bean
    public MetricsServletsContextListener metricsServletsContextListener() {
        return new MetricsServletsContextListener();
    }

}