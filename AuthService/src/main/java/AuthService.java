import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(CredentialsRepository credentialsRepository, PasswordEncoder passwordEncoder) {
        this.credentialsRepository = credentialsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> registerUser(String username, String password) {
        Credentials newCredentials = new Credentials();
        newCredentials.setUsername(username);
        newCredentials.setPassword(password);

        credentialsRepository.save(newCredentials);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    public ResponseEntity<String> loginUser(String username, String password) {
        Credentials credentials = credentialsRepository.findUserByUsername(username).orElse(null);

        if (credentials == null) {
            return null;
        }
        boolean isPasswordMatch = passwordEncoder.matches(password, credentials.getPassword());
        if (isPasswordMatch) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}
