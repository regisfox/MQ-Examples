package br.gov.caixa.simtx.web.configuracao;


import java.util.Calendar;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTUtil {
	
	private static String chave = "DFAS63456FGSD567DFH45699HG";
    public static final String TOKEN_HEADER = "Authorization";
    
    public static String geraToken(String usuario, String grupo) {

        Calendar calendar = Calendar.getInstance(); 
    	calendar.add(Calendar.HOUR, 4);        
    	Date expiryDate = calendar.getTime();
    	
        return Jwts.builder().setId(grupo)
                .setSubject(usuario)
                .signWith(SignatureAlgorithm.HS512, chave)
                .setExpiration(expiryDate)
                .compact();
    }

    public static Jws<Claims> decodificaToken(String token){
        
        try {
        	return Jwts.parser().setSigningKey(chave).parseClaimsJws(token);
		} catch (Exception e) {
			return null;
		}
    }
    
    private JWTUtil(){}
}
