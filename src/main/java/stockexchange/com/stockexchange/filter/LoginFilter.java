package stockexchange.com.stockexchange.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import stockexchange.com.stockexchange.config.ApplicationContextProvider;
import stockexchange.com.stockexchange.exceptions.UserAuthenticationException;
import stockexchange.com.stockexchange.info.Info;
import stockexchange.com.stockexchange.service.authentication.TokenAuthenticationService;
import stockexchange.com.stockexchange.service.authentication.UserAuthentication;
import stockexchange.com.stockexchange.service.authentication.UserToken;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    public LoginFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
        tokenAuthenticationService= (TokenAuthenticationService) ApplicationContextProvider.getApplicationContext()
                .getBean("tokenAuthenticationServiceImpl");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        UserAuthentication auth = (UserAuthentication) tokenAuthenticationService.getAuthenticationForLogin(request,response);
        if(!auth.isAuthenticated()){
            throw new UserAuthenticationException("Authentication failed", auth);
        }
        return auth;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserToken token = null;
        try {
            UserAuthentication authResultObject = (UserAuthentication) authResult;
            token = tokenAuthenticationService.addAuthentication(response, authResultObject);
            System.out.println("Authentication SUCCESS. "+"UserId: " + authResultObject.getUser().getId()+ " token: "+ token.getToken());

            SecurityContextHolder.getContext().setAuthentication(authResult);

            ObjectMapper mapper = new ObjectMapper();
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(mapper.writeValueAsString(authResultObject.getInfo()));
        } catch (Exception ex) {
            Info i = new Info();
            i.setHttpStatusCode(103L);
            i.setDesc("AUTHENTICATION_EXCEPTION_RELOGIN_NEEDED");

            System.out.println("Authentication EXCEPTION. " + "Invalid token: " + token +"\n");
            ex.printStackTrace();
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(i));
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        UserAuthenticationException authException = (UserAuthenticationException) failed;
        Info info = authException.getAuthentication().getInfo();

        response.setHeader("X-AUTH-ERR-DESC", info.getHttpStatusCode() + "-" + info.getDesc());

        ObjectMapper mapper = new ObjectMapper();
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(authException.getAuthentication().getInfo()));
        System.out.println("Autentication FAIL. " + "Username" + request.getHeader("username") + ". Additional info: "+info);
    }
}
