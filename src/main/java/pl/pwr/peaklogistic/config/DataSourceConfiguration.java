//package pl.pwr.peaklogistic.config;
//
//
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class DataSourceConfiguration {
//    @Bean
//    public DataSource getDataSource(){
//        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
//        dataSourceBuilder.url("jdbc:mysql://localhost:3306/PeakLogistics");
//        dataSourceBuilder.username("root");
//        dataSourceBuilder.password("pass");
//        return dataSourceBuilder.build();
//    }
//}
