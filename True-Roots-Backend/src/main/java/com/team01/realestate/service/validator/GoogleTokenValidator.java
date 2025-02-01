package com.team01.realestate.service.validator;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class GoogleTokenValidator {

    private static final String GOOGLE_JWKS_URL = "https://www.googleapis.com/oauth2/v3/certs";
    private static final String GOOGLE_ISSUER = "https://accounts.google.com";
    private static final String CLIENT_ID = "730087485718-otopggb12q7aq8h4mkq0q75v9icm7nld.apps.googleusercontent.com"; // Replace with your Google Client ID

    public static boolean validateIdToken(String idToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(idToken);
            System.out.println("Signed JWT: " + signedJWT);
            JWTClaimsSet claimsSet = validateToken(signedJWT);
            System.out.println("Claims Set: " + claimsSet);

            String issuer = claimsSet.getIssuer();
            if (!GOOGLE_ISSUER.equals(issuer)) {
                throw new IllegalArgumentException("Invalid issuer: " + issuer);
            }

            List<String> audience = claimsSet.getAudience();
            if (audience == null || audience.isEmpty() || !audience.contains(CLIENT_ID)) {
                throw new IllegalArgumentException("Invalid audience: " + audience);
            }

            Date expirationTime = claimsSet.getExpirationTime();
            if (expirationTime == null || expirationTime.before(Calendar.getInstance().getTime())) {
                throw new IllegalArgumentException("Expired token");
            }
            //Check email verified
            boolean emailVerified = claimsSet.getBooleanClaim("email_verified");
            if (!emailVerified) {
                throw new IllegalArgumentException("Unverified email");
            }
            return true;

        } catch (ParseException | JOSEException | BadJOSEException | MalformedURLException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return false;
        }
    }



    private static JWTClaimsSet validateToken(SignedJWT signedJWT) throws ParseException, JOSEException, BadJOSEException, MalformedURLException {
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        JWKSource<SecurityContext> keySource = new RemoteJWKSet<>(new URL(GOOGLE_JWKS_URL));
        JWSAlgorithm expectedJWSAlg = JWSAlgorithm.RS256; // Check the actual algorithm in your tokens. Usually RS256 for Google.
        JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(expectedJWSAlg, keySource);
        jwtProcessor.setJWSKeySelector(keySelector);


        SecurityContext ctx = null; // You can add a security context here if needed.
        return jwtProcessor.process(signedJWT, ctx);

    }

}
