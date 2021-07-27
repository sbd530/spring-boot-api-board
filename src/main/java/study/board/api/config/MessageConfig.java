package study.board.api.config;

import net.rakugakibox.util.YamlResourceBundle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Configuration
public class MessageConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        //기본 지역설정 KOREAN = 'ko'
        slr.setDefaultLocale(Locale.KOREAN);
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        //리퀘스트에 lang 을 지정하여 보내면 지역설정을 변경
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //위에서 빈 등록한 지역변경 인터셉터를 레지스트리에 추가
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public MessageSource messageSource(
            @Value("${spring.messages.basename}") String basename,
            @Value("${spring.messages.encoding}") String encoding) {
        // 작성한 yml 파일 내용을 참조하도록 설정
        YamlMessageSource yms = new YamlMessageSource();
        yms.setBasename(basename);
        yms.setDefaultEncoding(encoding);
        yms.setAlwaysUseMessageFormat(true);
        yms.setUseCodeAsDefaultMessage(true);
        yms.setFallbackToSystemLocale(true);
        return yms;
    }

    //local 에 따라 다른 yml 파일을 읽도록 처리
    private static class YamlMessageSource extends ResourceBundleMessageSource {
        @Override
        protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
            return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE);
        }
    }

}
