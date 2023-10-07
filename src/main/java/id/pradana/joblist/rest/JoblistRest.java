package id.pradana.joblist.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.pradana.joblist.dao.UserModelDao;
import id.pradana.joblist.dto.LoginDto;
import id.pradana.joblist.dto.RegisterDto;
import id.pradana.joblist.model.UserModel;
import id.pradana.joblist.service.JoblistService;
import id.pradana.joblist.utils.JWTUtil;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class JoblistRest {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserModelDao userModelDao;

    @Autowired
    private JoblistService joblistService;

    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping("/")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok().body("Hello Mom");
    }

    @GetMapping("/joblist")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getJobList() {
        return joblistService.getJobs();
    }

    @GetMapping("/job/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getJobById(@PathVariable String id) {
        return joblistService.getJobById(id);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);

        Map<String, Object> body = new HashMap<>();
        body.put("token", jwt);

        return ResponseEntity.ok(body);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerRequest) {
        if (userModelDao.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }

        UserModel model = new UserModel();
        model.setUsername(registerRequest.getUsername());
        model.setPassword(encoder.encode(registerRequest.getPassword()));
        model.setRole("USER");

        userModelDao.save(model);

        return ResponseEntity.ok("User registered successfully!");
    }

}
