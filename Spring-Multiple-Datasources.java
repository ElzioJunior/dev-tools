private static Map<String, Object> defaultSpringConfig = new HashMap<String, Object>() {{
    put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
    put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
    put("hibernate.id.new_generator_mappings", GenerationType.IDENTITY);
}};

...
@Primary
@Bean(name = "dataSourceManagerFactory")
public LocalContainerEntityManagerFactoryBean dataSourceManagerFactory(
    final EntityManagerFactoryBuilder builder, final @Qualifier("YoursDataSource") DataSource dataSource) {
    return builder
        .dataSource(dataSource)
        .packages(package to the entityes)
        .persistenceUnit("bla")
        .properties(defaultSpringConfig)
        .build();
}