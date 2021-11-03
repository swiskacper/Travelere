package swis.kacper.start.Security;

import io.jsonwebtoken.JwtBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import swis.kacper.start.Utilities.SecurityUtilities;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtDeliverer implements IJwtDeliverer{

    @Value("${app.jwt.secret}")
    private String JWT_SECRET;


    @Value("${app.jwt.expiration-in-ms}")
    private Long JWT_EXPIRATION_IN_MS;

    @Override
    public String generateToken(UserDetail userDetail) {

//        System.out.println("getUser: "+userDetail.getUser());
//        System.out.println("getAuthorities "+userDetail.getAuthorities());
//        System.out.println("getAuthorities  stream map collect "+userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
        String authorities=
                userDetail.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)//dla kazdego grantedAuthority getAuthority
                .collect(Collectors.joining(","));

        //claim is piece of information that describes given identity on some aspect.
        // take claim as name-value pair.
        // claims are held in authentication token that may have also signature so you can be sure that token is not tampered on its way from remote machine to your system.


       //The header contains info on how the JWT is encoded. The body is the meat of the token (where the claims live). The signature provides the security.

        System.out.println("CCCHECK1: " +Jwts.builder().setSubject(userDetail.getEmail()).compact());
        System.out.println("CCCHECK2: " +Jwts.builder().setSubject(userDetail.getEmail()).claim("roles", authorities).compact());
        System.out.println("CCCHECK3: " +Jwts.builder().setSubject(userDetail.getEmail()).claim("roles", authorities).claim("userId", userDetail.getNumber()).compact());
        System.out.println("CCCHECK4: " +Jwts.builder().setSubject(userDetail.getEmail()).claim("roles", authorities).
                claim("userId", userDetail.getNumber()).setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_IN_MS)).compact());

        System.out.println("CCCHECK5: " +Jwts.builder().setSubject(userDetail.getEmail()).claim("roles", authorities).
                claim("userId", userDetail.getNumber()).setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_IN_MS)).signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact());


        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(JWT_SECRET))
                .parseClaimsJws(Jwts.builder()
                        .setSubject(userDetail.getUsername())
                        .claim("roles", authorities)
                        .claim("userId", userDetail.getNumber())
                        .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_IN_MS))
                        .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                        .compact()).getBody();

//        System.out.println("CL: " + claims.toString());
//        System.out.println("Subject: "+claims.getSubject());
//        System.out.println("ID: "+claims.get());
//        System.out.println("ISSUEER: "+claims.getIssuer());
//        System.out.println("EXPIRATION: "+claims.getExpiration());

        //    Header – przechowuje on informacje na temat algorytmu szyfrowania oraz typie tokena.
        //    Payload – dowolny przekazywany ładunek. Najczęściej są w nim przechowywane informacje na temat roli i praw użytkownika, czy też długości życia dla tokena.
        //    Verify – podpis cyfrowy, który składa się z zaszyfrowanego Headera i Paylodu. Stanowi on sumę kontrolną.

        return Jwts.builder()
                .setSubject(userDetail.getUsername())
                .claim("roles", authorities)
                .claim("userId", userDetail.getNumber())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_IN_MS))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();

    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        Claims claims = extractClaims(request);

        if (claims == null)
        {
            return null;
        }

        String email = claims.getSubject();
        System.out.println("EMAIL: "+email);
        Integer  userNumber = claims.get("userNumber", Integer.class);
        System.out.println("USERNUMBER: " + userNumber);
        Set<GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
                .map(SecurityUtilities::convertToAuthority)
                .collect(Collectors.toSet());

        UserDetails userDetails = UserDetail.builder()
                .email(email)
                .authorities(authorities)
                .number(userNumber)
                .build();

        if (email == null)
        {
            return null;
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    @Override
    public boolean validateToken(HttpServletRequest request) {
        Claims claims = extractClaims(request);

        if (claims == null)
            return false;
        if(claims.getExpiration().before(new Date()))
            return false;
        return true;
    }




    private Claims extractClaims(HttpServletRequest request)
    {
        String token = SecurityUtilities.extractAuthTokenFromRequest(request);

        if (token == null)
        {
            return null;
        }

        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}

