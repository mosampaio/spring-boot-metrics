package mosampaio;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.codahale.metrics.servlet.InstrumentedFilter;
import com.codahale.metrics.servlet.InstrumentedFilterContextListener;
import com.codahale.metrics.servlets.AdminServlet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import com.ryantenney.metrics.spring.servlets.MetricsServletsContextListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;

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


        registerAll("gc", new GarbageCollectorMetricSet(), metricRegistry);
        registerAll("memory", new MemoryUsageGaugeSet(), metricRegistry);
        registerAll("threads", new ThreadStatesGaugeSet(), metricRegistry);
        registerAll("buffers", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()), metricRegistry);
    }

    private void registerAll(String prefix, MetricSet metrics, MetricRegistry metricRegistry) throws IllegalArgumentException {
        for (Map.Entry<String, Metric> entry : metrics.getMetrics().entrySet()) {
            if (entry.getValue() instanceof MetricSet) {
                registerAll(name(prefix, entry.getKey()), (MetricSet) entry.getValue(), metricRegistry);
            } else {
                metricRegistry.register(name(prefix, entry.getKey()), entry.getValue());
            }
        }
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        return new ServletRegistrationBean(new AdminServlet(),"/metrics/admin/*");
    }

    @Bean
    public MetricsServletsContextListener metricsServletsContextListener() {
        return new MetricsServletsContextListener();
    }

    @Bean
    public InstrumentedFilterContextListener instrumentedFilterContextListener(MetricRegistry metricRegistry) {
        return new InstrumentedFilterContextListener() {
            @Override
            protected MetricRegistry getMetricRegistry() {
                return metricRegistry;
            }
        };
    }

    @Bean
    public InstrumentedFilter instrumentedFilter() {
        return new InstrumentedFilter();
    }

    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(instrumentedFilter());
        registration.addUrlPatterns("/*");
        registration.setName("instrumentedFilter");
        registration.setOrder(1);
        return registration;
    }
}