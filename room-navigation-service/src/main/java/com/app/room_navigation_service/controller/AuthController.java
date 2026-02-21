package com.app.room_navigation_service.controller;
import com.app.room_navigation_service.DTO.AdminDTO;
import com.app.room_navigation_service.DTO.CmuEntraIDBasicInfoDTO;
import com.app.room_navigation_service.DTO.SignInResponseDTO;
import com.app.room_navigation_service.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${app.domain:localhost}")
    private String appDomain;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @PostMapping("/login")
    public ResponseEntity<SignInResponseDTO> signIn(@RequestBody Map<String, String> body, HttpServletResponse response) {
        // 1. Validation
        String authorizationCode = body.get("authorizationCode");

        if (authorizationCode == null || authorizationCode.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SignInResponseDTO(false, "Invalid authorization code"));
        }

        // 2. Get Access Token from EntraID
        String accessToken = authService.getEntraIDAccessToken(authorizationCode);
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SignInResponseDTO(false, "Cannot get EntraID access token"));
        }

        // 3. Get Basic Info
        CmuEntraIDBasicInfoDTO userInfo = authService.getCMUBasicInfo(accessToken);
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SignInResponseDTO(false, "Cannot get cmu basic info"));
        }

        // 4. Create JWT Token
        String token = generateJwtToken(userInfo);

        // 5. Create Cookie (HttpOnly)
        ResponseCookie cookie = ResponseCookie.from("cmu-entraid-example-token", token)
                .httpOnly(true)
                .secure("production".equals(activeProfile))
                .path("/")
                .maxAge(3600)
                .sameSite("Lax")
                .domain(appDomain)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new SignInResponseDTO(true, "Signed in successfully"));
    }

    @PostMapping("/logout")
    public ResponseEntity<SignInResponseDTO> signOut(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("cmu-entraid-example-token", "")
                .httpOnly(true)
                .secure("production".equals(activeProfile))
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .domain(appDomain)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new SignInResponseDTO(true, "Signed out successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> whoAmI(@CookieValue(name = "cmu-entraid-example-token", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("ok", false));
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();


            Map<String, Object> userData = new HashMap<>();
            userData.put(AdminDTO.Fields.fullNameEN, claims.get(CmuEntraIDBasicInfoDTO.Fields.firstname_EN) + " " + claims.get(CmuEntraIDBasicInfoDTO.Fields.lastname_EN));
            userData.put(AdminDTO.Fields.itAccount, claims.get(CmuEntraIDBasicInfoDTO.Fields.cmuitaccount));
            userData.put(AdminDTO.Fields.facultyId, claims.get(CmuEntraIDBasicInfoDTO.Fields.organization_code));
            userData.put(AdminDTO.Fields.facultyNameTH, claims.get(CmuEntraIDBasicInfoDTO.Fields.organization_name_TH));
            userData.put(AdminDTO.Fields.facultyNameEN, claims.get(CmuEntraIDBasicInfoDTO.Fields.organization_name_EN));

            return ResponseEntity.ok(Map.of("ok", true, "user", userData));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("ok", false));
        }
    }

    private String generateJwtToken(CmuEntraIDBasicInfoDTO info) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CmuEntraIDBasicInfoDTO.Fields.cmuitaccount_name, info.getCmuitaccount_name());
        claims.put(CmuEntraIDBasicInfoDTO.Fields.cmuitaccount, info.getCmuitaccount());
        claims.put(CmuEntraIDBasicInfoDTO.Fields.student_id, info.getStudent_id());
        claims.put(CmuEntraIDBasicInfoDTO.Fields.prename_id, info.getPrename_id());
        claims.put(CmuEntraIDBasicInfoDTO.Fields.prename_TH, info.getPrename_TH());
        claims.put(CmuEntraIDBasicInfoDTO.Fields.prename_EN, info.getPrename_EN());
        claims.put(CmuEntraIDBasicInfoDTO.Fields.firstname_TH, info.getFirstname_TH());
        claims.put(CmuEntraIDBasicInfoDTO.Fields.firstname_EN, info.getFirstname_EN());
        claims.put(CmuEntraIDBasicInfoDTO.Fields.lastname_TH, info.getLastname_TH());
        claims.put(CmuEntraIDBasicInfoDTO.Fields.lastname_EN, info.getLastname_EN());
        claims.put(CmuEntraIDBasicInfoDTO.Fields.organization_code, info.getOrganization_code());
        claims.put(CmuEntraIDBasicInfoDTO.Fields.organization_name_TH, info.getOrganization_name_TH());
        claims.put(CmuEntraIDBasicInfoDTO.Fields.organization_name_EN, info.getOrganization_name_EN());
        claims.put(CmuEntraIDBasicInfoDTO.Fields.itaccounttype_id, info.getItaccounttype_id());
        claims.put(CmuEntraIDBasicInfoDTO.Fields.itaccounttype_TH, info.getItaccounttype_TH());
        claims.put(CmuEntraIDBasicInfoDTO.Fields.itaccounttype_EN, info.getItaccounttype_EN());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
