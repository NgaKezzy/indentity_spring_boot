package com.mysql.DEMO_MYSQL.service;

import com.mysql.DEMO_MYSQL.dto.request.AuthenticationRequest;
import com.mysql.DEMO_MYSQL.dto.request.IntroSpectTokenRequest;
import com.mysql.DEMO_MYSQL.dto.request.LogoutRequest;
import com.mysql.DEMO_MYSQL.dto.response.AuthenticationResponse;
import com.mysql.DEMO_MYSQL.dto.response.IntroSpectTokenResponse;
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
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassWord(), user.getPassWord());


        if (!authenticated) {
            throw new AppException(ErrorCode.UN_AUTHENTICATED);
        }
        var token = generateToken(user);
        return AuthenticationResponse.builder().authenticated(authenticated).token(token).build();

    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet =
                new JWTClaimsSet.Builder().subject(user.getUserName()).issuer("dev.com").issueTime(new Date())
                        .expirationTime(new Date(
                                Instant.now().plus(30, ChronoUnit.DAYS).toEpochMilli()))
                        .jwtID(UUID.randomUUID().toString())
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
            var jwtToken = verifyToken(token);

        } catch (Exception e) {
            isValid = false;
        }
        return IntroSpectTokenResponse.builder().valid(isValid).build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {

        var signToken = verifyToken(request.getToken());
        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        var expireDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(jwsVerifier);
        if (!(verified && expireDate.after(new Date()))) {
            throw new AppException(ErrorCode.UN_AUTHENTICATED);
        }
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UN_AUTHENTICATED);
        }
        return signedJWT;
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
