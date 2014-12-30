package main.pkg.name.config.spring;

import main.pkg.name.config.database.HibernateConfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ HibernateConfig.class })
// Exclude MVC controllers to prevent duplicate connection pools
// TODO Update package
@ComponentScan(basePackages = "main.pkg.name", excludeFilters = @ComponentScan.Filter(value = org.springframework.stereotype.Controller.class, type = FilterType.ANNOTATION))
public class AppContext {

}
