package com.example.emailspamapi.config;

import com.example.emailspamapi.model.SmsMessage;
import com.example.emailspamapi.model.User;
import com.example.emailspamapi.model.UserRole;
import com.example.emailspamapi.repository.SmsMessageRepository;
import com.example.emailspamapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SmsMessageRepository messageRepository;
    private final PasswordEncoder passwordEncoder;

    // Изменено: Используем конструктор вместо @Autowired
    public DataInitializer(UserRepository userRepository,
                           SmsMessageRepository messageRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        createDefaultUsers();
        createDemoMessages();
    }

    private void createDefaultUsers() {
        // Администратор
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User("admin", "admin@emailspam.com",
                    passwordEncoder.encode("admin123"), UserRole.ADMIN);
            userRepository.save(admin);
            System.out.println("✅ Created admin: admin / admin123");
        }

        // Пользователь
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User("user", "user@emailspam.com",
                    passwordEncoder.encode("user123"), UserRole.USER);
            userRepository.save(user);
            System.out.println("✅ Created user: user / user123");
        }
    }

    private void createDemoMessages() {
        if (messageRepository.count() == 0) {
            messageRepository.saveAll(Arrays.asList(
                    new SmsMessage("ham", "Hello, how are you doing today?"),
                    new SmsMessage("spam", "FREE iPhone! Click NOW to claim your prize!"),
                    new SmsMessage("ham", "Meeting scheduled for tomorrow at 10 AM"),
                    new SmsMessage("spam", "URGENT: Your account needs verification!!!"),
                    new SmsMessage("ham", "Thanks for your email, I'll respond soon"),
                    new SmsMessage("spam", "You've won $1000 cash prize! Claim now!"),
                    new SmsMessage("ham", "Can we reschedule our meeting to 2 PM?"),
                    new SmsMessage("spam", "Limited time offer: 50% discount on all products"),
                    new SmsMessage("ham", "Happy birthday! Hope you have a great day"),
                    new SmsMessage("spam", "Congratulations! You're selected for a free gift")
            ));
            System.out.println("✅ Loaded 10 demo messages");
        }
    }
}