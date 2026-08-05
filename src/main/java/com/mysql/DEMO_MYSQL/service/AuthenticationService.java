package com.mysql.DEMO_MYSQL.service;

import com.mysql.DEMO_MYSQL.dto.request.authent.AuthenticationRequest;
import com.mysql.DEMO_MYSQL.dto.request.authent.IntroSpectTokenRequest;
import com.mysql.DEMO_MYSQL.dto.request.authent.LogoutRequest;
import com.mysql.DEMO_MYSQL.dto.request.authent.RefreshRequest;
import com.mysql.DEMO_MYSQL.dto.response.authent.AuthenticationResponse;
import com.mysql.DEMO_MYSQL.dto.response.authent.IntroSpectTokenResponse;
import com.mysql.DEMO_MYSQL.dto.response.authent.RefreshTokenResponse;
import com.mysql.DEMO_MYSQL.entity.InvalidatedToken;
import com.mysql.DEMO_MYSQL.entity.User;
import com.mysql.DEMO_MYSQL.exception.AppException;
import com.mysql.DEMO_MYSQL.exception.ErrorCode;
import com.mysql.DEMO_MYSQL.repository.InvalidatedTokenRepository;
import com.mysql.DEMO_MYSQL.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    public long REFRESHABLE_DURATION;

    static final Logger log =
            LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassWord(), user.getPassWord());


        if (!authenticated) {
            throw new AppException(ErrorCode.UN_AUTHENTICATED);
        }
        String token = generateToken(user, false);
        String refreshToken = generateToken(user, true);
        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(token)
                .refreshToken(refreshToken)
                .build();

    }

    private String generateToken(User user, boolean isRefreshToken) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        long duration = isRefreshToken ? REFRESHABLE_DURATION : VALID_DURATION;
        ChronoUnit durationUnit = isRefreshToken ? ChronoUnit.DAYS : ChronoUnit.HOURS;
        JWTClaimsSet jwtClaimsSet =
                new JWTClaimsSet.Builder().subject(user.getUserName()).issuer("dev.com").issueTime(new Date())
                        .expirationTime(new Date(
                                Instant.now().plus(duration, durationUnit).toEpochMilli()))
                        .jwtID(UUID.randomUUID().toString())
                        .claim("token_type", isRefreshToken ? "refresh" : "access")
                        .claim("scope", buildScope(user))
                        .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();

        } catch (JOSEException e) {
            log.error("Can not crate token: ", e);
            throw new RuntimeException(e);
        }

    }

    public IntroSpectTokenResponse introSpectToken(IntroSpectTokenRequest request) throws JOSEException,
            ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, "access");

        } catch (Exception e) {
            isValid = false;
        }
        return IntroSpectTokenResponse.builder().valid(isValid).build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), null);
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
            InvalidatedToken invalidatedToken = InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();
            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException exception) {
            log.error("Token already expired");

        }
    }

    public RefreshTokenResponse refreshToken(RefreshRequest request)
            throws ParseException, JOSEException {

        SignedJWT refreshToken = verifyToken(request.getRefreshToken(), "refresh");
        String username = refreshToken.getJWTClaimsSet().getSubject();

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String token = generateToken(user, false);

        return RefreshTokenResponse.builder()
                .token(token)
                .build();
    }

    private SignedJWT verifyToken(String token, String expectedTokenType) throws JOSEException, ParseException {

        validateTokenInput(token);
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expireDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        String tokenType = signedJWT.getJWTClaimsSet().getStringClaim("token_type");
        var verified = signedJWT.verify(jwsVerifier);
        boolean correctTokenType = expectedTokenType == null || expectedTokenType.equals(tokenType);
        if (!(verified && expireDate.after(new Date()) && correctTokenType)) {
            throw new AppException(ErrorCode.UN_AUTHENTICATED);
        }
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UN_AUTHENTICATED);
        }
        return signedJWT;
    }

    private void validateTokenInput(String token) {
        if (token == null || token.isBlank()) {
            throw new AppException(ErrorCode.UN_AUTHENTICATED);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add(role.getName());

            });
        }
        return stringJoiner.toString();
    }


}
