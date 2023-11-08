package com.app.global.config.web;

import com.app.global.interceptor.AdminAuthorizationInterceptor;
import com.app.global.interceptor.AuthenticationInterceptor;
import com.app.global.resolver.memberinfo.MemberInfoArgumentResolver;
import com.app.web.HtmlCharacterEscapes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.lucy.security.xss.servletfilter.XssEscapeFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final AuthenticationInterceptor authenticationInterceptor;
  private final AdminAuthorizationInterceptor adminAuthorizationInterceptor;
  private final MemberInfoArgumentResolver memberInfoArgumentResolver;
  private final ObjectMapper objectMapper;

  /**
   * CORS 설정: 프로토콜 + 호스트 + 포트 (http://localhost:8082) Same Origin Policy Bypass Configuration
   *
   * @param registry
   */
  @Override
  public void addCorsMappings(final CorsRegistry registry) {
    registry.addMapping("/api/**")
            .allowedOrigins("*")
            .allowedMethods(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name())
            .maxAge(3600);
  }

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    registry.addInterceptor(authenticationInterceptor)
            .order(1)
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/oauth/login",
                "/api/access-token/issue",
                "/api/logout",
                "/api/health");

    registry.addInterceptor(adminAuthorizationInterceptor)
            .order(2)
            .addPathPatterns("/api/admin/**");
  }

  @Override
  public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(memberInfoArgumentResolver);
  }

  @Override
  public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
    converters.add(jsonEscapeConverter());
  }

  @Bean
  public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
    final ObjectMapper copy = this.objectMapper.copy();
    copy.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
    return new MappingJackson2HttpMessageConverter(copy);
  }

  @Bean
  public FilterRegistrationBean<XssEscapeServletFilter> filterRegistrationBean() {
    FilterRegistrationBean<XssEscapeServletFilter> filterRegistration = new FilterRegistrationBean<>();
    filterRegistration.setFilter(new XssEscapeServletFilter());
    filterRegistration.setOrder(1);
    filterRegistration.addUrlPatterns("/*"); // xss 필터 적용 경로
    return filterRegistration;
  }

  public static class XssEscapeServletFilter implements Filter {

    private XssEscapeFilter xssEscapeFilter = XssEscapeFilter.getInstance();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
      chain.doFilter(new XssEscapeServletFilterWrapper(request, xssEscapeFilter), response);
    }

    @Override
    public void destroy() {
    }
  }

  public static class XssEscapeServletFilterWrapper extends HttpServletRequestWrapper {

    private XssEscapeFilter xssEscapeFilter;
    private String path = null;

    public XssEscapeServletFilterWrapper(ServletRequest request, XssEscapeFilter xssEscapeFilter) {
      super((HttpServletRequest) request);
      this.xssEscapeFilter = xssEscapeFilter;

      String contextPath = ((HttpServletRequest) request).getContextPath();
      this.path = ((HttpServletRequest) request).getRequestURI().substring(contextPath.length());
    }

    @Override
    public String getParameter(String paramName) {
      String value = super.getParameter(paramName);
      return doFilter(paramName, value);
    }

    @Override
    public String[] getParameterValues(String paramName) {
      String values[] = super.getParameterValues(paramName);
      if (values == null) {
        return values;
      }
      for (int index = 0; index < values.length; index++) {
        values[index] = doFilter(paramName, values[index]);
      }
      return values;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String[]> getParameterMap() {
      Map<String, String[]> paramMap = super.getParameterMap();
      Map<String, String[]> newFilteredParamMap = new HashMap<>();

      Set<Entry<String, String[]>> entries = paramMap.entrySet();
      for (Entry<String, String[]> entry : entries) {
        String paramName = entry.getKey();
        Object[] valueObj = (Object[]) entry.getValue();
        String[] filteredValue = new String[valueObj.length];
        for (int index = 0; index < valueObj.length; index++) {
          filteredValue[index] = doFilter(paramName, String.valueOf(valueObj[index]));
        }

        newFilteredParamMap.put(entry.getKey(), filteredValue);
      }

      return newFilteredParamMap;
    }

    /**
     * @param paramName String
     * @param value     String
     * @return String
     */
    private String doFilter(String paramName, String value) {
      return xssEscapeFilter.doFilter(path, paramName, value);
    }
  }
}
