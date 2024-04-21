package com.metehan.authenticationApi;

import com.metehan.authenticationApi.dto.AuthenticationRequest;
import com.metehan.authenticationApi.dto.AuthenticationResponse;
import com.metehan.authenticationApi.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class EntrypointController {

    private final AuthenticationService authenticationService;

    @PostMapping("/api/v1/authentication")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok()
                .header("Set-Cookie", "token=" + authenticationService.authenticate(request).getToken() + "; HTTPOnly")
                .body(null);
    }

    @PostMapping("/api/v1/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok()
                .header("Set-Cookie", "token=" + authenticationService.register(request).getToken() + "; HTTPOnly")
                .body(null);
    }

}
