package com.api.recipemanagement;

import com.api.recipemanagement.filters.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RecipeManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeManagementApplication.class, args);
	}

	/* Add FilterRegistrationBean method to register custom filters for the application
	 * To authenticate requests  connect AuthFilter class
	 * */
	@Bean
	public FilterRegistrationBean<AuthFilter> filterRegistrationBean() {
		FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
		AuthFilter authFilter = new AuthFilter();
		registrationBean.setFilter(authFilter);
		registrationBean.addUrlPatterns("/api/recipes/*");
		return registrationBean;
	}

}
