package edu.itmo.blps.filter;

import edu.itmo.blps.domain.SecurityUser;
import edu.itmo.blps.domain.SecurityUserSet;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import utils.JwtUtils;
import utils.XmlUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class TokenFilter extends OncePerRequestFilter {
    final Pattern patternId = Pattern.compile("^\\d+");
    final Pattern patternType = Pattern.compile("[a-zA-Z]+");
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if(!StringUtils.hasText(token)){
            filterChain.doFilter(request,response);
            return;
        }
        String userId;
        try{
            /**
             * get id
             * remember that here token is id+type
             * for example:
             * 1usr means that user is command user and his id in command user is 1
             * 2company means that user is a company and his id in company is 2
             */
            Claims claims = JwtUtils.parseToken(token);
            userId = claims.getSubject();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Illegal token\n");
        }
        Matcher matcherId = patternId.matcher(userId);
        Matcher matcherType = patternType.matcher(userId);
        Integer id = Integer.parseInt(matcherType.replaceAll(""));
        String type = matcherId.replaceAll("");
        /**
         * read from xml
         */
        SecurityUserSet users;
        try {
            users = XmlUtils.unmarshall();
        }catch (IOException| JAXBException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to read XML\n");
        }
        SecurityUser user = null;

        for(SecurityUser s : users.getUsers()){
            if(s.getId().equals(id) && Objects.equals(type,s.getType())){
                user = s;
            }
        }
        if(Objects.isNull(user)){
            throw new RuntimeException("Xml doesn't save this user");
        }
        /**
         * save to context
         */
        UsernamePasswordAuthenticationToken newToken =
                new UsernamePasswordAuthenticationToken(user,null, Arrays.asList(user.getType()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(newToken);
        /**
         * go on
         */
        filterChain.doFilter(request,response);
    }
}
