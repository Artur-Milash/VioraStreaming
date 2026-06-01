package org.viora.viorastreamingcore.configs.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

  private final JwtEncoder encoder;
  private final JwtDecoder decoder;

  public String generateToken(UserDetails userDetails) {
    Instant now = Instant.now();
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("self")
        .issuedAt(now)
        .expiresAt(now.plus(1, ChronoUnit.HOURS))
        .subject(userDetails.getUsername())
        .build();
    var encoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),
        claims);
    return this.encoder.encode(encoderParameters).getTokenValue();
  }

  public boolean isValidToken(String token) {
    try {
      Jwt jwt = decoder.decode(token);
      Instant exp = jwt.getExpiresAt();
      return exp.isAfter(Instant.now());
    } catch (JwtValidationException e) {
      return false;
    }
  }

  public String getUsernameFromToken(String token) {
    return decoder.decode(token).getSubject();
  }
}
