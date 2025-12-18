package com.do_issac.hotel_manage.jwt;

import com.do_issac.hotel_manage.entity.QrKhoaPhong;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;

@Component
public class QrJwtProvider {

    @Value("${app.qr.jwt.secret}")
    private String secret;

    public String generateQrToken(QrKhoaPhong qr) {
        return Jwts.builder()
                .setSubject("booking:" + qr.getDatPhong().getId())
                .claim("bookingId", qr.getDatPhong().getId())
                .claim("phongId", qr.getDatPhong().getPhong().getId())
                .setIssuedAt(new Date())
                .setExpiration(
                        Date.from(qr.getHieuLucDen()
                                .atZone(ZoneId.systemDefault())
                                .toInstant())
                )
                .setId(qr.getId().toString())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseQrToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
