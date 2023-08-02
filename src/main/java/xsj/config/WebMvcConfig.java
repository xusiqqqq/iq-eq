package xsj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xsj.interceptor.RequestInterceptor;

import java.util.Locale;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private RequestInterceptor requestInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(requestInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/user/toLogin",
                        "/static/**");
//        "/user/login",
//                "/user/logout",
//                "/user/code",
    }

    /**
     * 用来指定具体的 Bean Validator
     */
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean lvfb=new LocalValidatorFactoryBean();
        //设置属性
        lvfb.setValidationMessageSource(messageSource());

        //返回
        return lvfb;
    }
    /**
     * 用来指定验证时要读取的文件
     * @return
     */
    private MessageSource messageSource() {
        ResourceBundleMessageSource messageSource=new ResourceBundleMessageSource();
        //属性
        messageSource.setBasename("beanValidation");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(60);
        messageSource.setAlwaysUseMessageFormat(true);
        return messageSource;
    }
}
