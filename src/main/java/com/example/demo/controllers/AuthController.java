package com.example.demo.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.Enum.Role;
import com.example.demo.entities.Collaborateur;
import com.example.demo.entities.Roles;
import com.example.demo.models.JwtResponse;
import com.example.demo.models.LoginRequest;
import com.example.demo.models.SignupRequest;
import com.example.demo.repository.ICollaborateurRepository;
import com.example.demo.repository.RolesRepository;
import com.example.demo.security.config.JwtUtils;
import com.example.demo.security.services.UserDetailsImpl;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final ICollaborateurRepository iCollaborateurRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    // Constructor injection
    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          ICollaborateurRepository iCollaborateurRepository,
                          RolesRepository rolesRepository,
                          PasswordEncoder encoder,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.iCollaborateurRepository = iCollaborateurRepository;
        this.rolesRepository = rolesRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (iCollaborateurRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already in use!");
        }

        // Create new user's account
        Collaborateur collaborateur = new Collaborateur();
        collaborateur.setUsername(signUpRequest.getUsername());
        collaborateur.setEmail(signUpRequest.getEmail());
        collaborateur.setPassword(encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Roles> roles = new HashSet<>();

        if (strRoles == null) {
            Roles userRole = rolesRepository.findByName(Role.RH)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "DAF":
                        Roles adminRole = rolesRepository.findByName(Role.DAF)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "RH":
                        Roles userRole = rolesRepository.findByName(Role.RH)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                        break;
                    default:
                        Roles devRole = rolesRepository.findByName(Role.DEV)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(devRole);
                }
            });
        }

        collaborateur.setRoles(roles);
        iCollaborateurRepository.save(collaborateur);

        return ResponseEntity.ok("Collaborateur registered successfully!");
    }
}
