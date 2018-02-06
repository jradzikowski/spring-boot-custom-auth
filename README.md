# Spring Boot + Spring security + Maven + JPA + Custom Authentication + JSP

##What it is all about
This code brings web application based on Spring Boot. Main idea was to presence working framework for web application based on Spring Boot that connects to database using JPA to fetch user. 
In this project password is encrypted with BCrypth algorythm and is being kept in data base together with login (in plain text).

#Getting Started

##Preconditions
Application is configured to run against Postresql database. Before you start it is mandatory to have running PostgreSQL 

## You don't need to use application.properties
Each configuration is set programmatically. Project contains separate java classes where configurations are located.

### Data source configuration
```Java
@Configuration
@Component
public class DbConfiguration {

    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .username("postgres")
                .password("admin")
                .url("jdbc:postgresql://localhost:5432/spring")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(new String[] { "com.dzasek.springbootcustomauth" });

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        em.setJpaProperties(properties);

        return em;
    }
}
```

Data source bean with all properties is being held in DBConfiguration class. Change your username, password or url if data are different to existing one.

Line:
`
properties.setProperty("hibernate.hbm2ddl.auto", "update");
`

stands for hibernate behavior. If the property is set to "update", Spring Boot during initialization will create all tables for you.
Table definitions are stored in com.dzasek.springbootcustomauth.entity package

### Spring Web configuration
At MvcConfiguration class a web configuration is set up:

```java
@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/test").setViewName("start");
    }

    @Bean
    public InternalResourceViewResolver resolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
}
```

`
registry.addViewController("/test").setViewName("start");
`

This line adds view resolver for endpoint /test. Behavior is the same as when a controller is handling request for the endpoint and returns "start" at the end. If controler do nothing that just says what view should be displayed then there is no need to create controller. 

`
viewResolver.setPrefix("/WEB-INF/pages/");
viewResolver.setSuffix(".jsp");
`

And we need to point where our JSP files are located and what suffix spring has to take under consideration when resolving view. In this case when user hit /test endpoint, spring will show start.jsp file from /WEB-ING/pages/ directory

### Spring Security configuration

```java
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/pass").hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/home")
                .loginProcessingUrl("/doLogin")
                .defaultSuccessUrl("/home?message=success")
                .failureUrl("/home?message=failure")
                .usernameParameter("customname").passwordParameter("custompassword")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home?message=loggedout");
    }

}
```

Endpoint "/" is accessible for everyone, logged and not logged users. 
Endpoint "/pass" shows the same content as "/", but is available only for logged users in role "ROLE_USER"