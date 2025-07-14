import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("123456");
        System.out.println("BCrypt hash for 123456: " + hash);
        
        String existingHash = "$2a$10$c4Zel2k/yLs7DS7CozZ0Ueb7XIPnVzYDoUB69zCaBcbCi5UBGyWS6";
        boolean matches123456 = encoder.matches("123456", existingHash);
        boolean matchesAdmin123 = encoder.matches("admin123", existingHash);
        
        System.out.println("Current hash matches '123456': " + matches123456);
        System.out.println("Current hash matches 'admin123': " + matchesAdmin123);
    }
}