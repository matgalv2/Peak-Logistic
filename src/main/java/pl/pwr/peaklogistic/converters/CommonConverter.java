package pl.pwr.peaklogistic.converters;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CommonConverter {

    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }
}
