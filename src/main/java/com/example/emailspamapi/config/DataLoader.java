package com.example.emailspamapi.config;

import com.example.emailspamapi.model.SmsMessage;
import com.example.emailspamapi.model.User;
import com.example.emailspamapi.model.UserRole; // Добавь этот импорт
import com.example.emailspamapi.repository.SmsMessageRepository;
import com.example.emailspamapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private SmsMessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Создаем тестовых пользователей
        createTestUsers();

        // Очищаем и загружаем тестовые данные сообщений
        loadTestMessages();
    }

    private void createTestUsers() {
        // Очищаем существующих пользователей (опционально)
        // userRepository.deleteAll();

        // Создаем администратора
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setRole(UserRole.ADMIN); // Используй enum вместо строки
            userRepository.save(admin);
            System.out.println("✅ Created admin user: admin / admin123");
        }

        // Создаем обычного пользователя
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEmail("user@example.com");
            user.setRole(UserRole.USER); // Используй enum вместо строки
            userRepository.save(user);
            System.out.println("✅ Created user: user / user123");
        }

        // Создаем тестового пользователя
        if (userRepository.findByUsername("test").isEmpty()) {
            User test = new User();
            test.setUsername("test");
            test.setPassword(passwordEncoder.encode("test123"));
            test.setEmail("test@example.com");
            test.setRole(UserRole.USER); // Используй enum вместо строки
            userRepository.save(test);
            System.out.println("✅ Created test user: test / test123");
        }

        // Создаем модератора (опционально)
        if (userRepository.findByUsername("moderator").isEmpty()) {
            User moderator = new User();
            moderator.setUsername("moderator");
            moderator.setPassword(passwordEncoder.encode("mod123"));
            moderator.setEmail("moderator@example.com");
            moderator.setRole(UserRole.MODERATOR);
            userRepository.save(moderator);
            System.out.println("✅ Created moderator: moderator / mod123");
        }

        System.out.println("✅ User setup completed");
    }

    private void loadTestMessages() {
        // Очищаем существующие сообщения
        messageRepository.deleteAll();

        List<SmsMessage> initialMessages = Arrays.asList(
                new SmsMessage("ham", "Go until jurong point, crazy.. Available only in bugis n great world la e buffet... Cine there got amore wat..."),
                new SmsMessage("ham", "Ok lar... Joking wif u oni..."),
                new SmsMessage("spam", "Free entry in 2 a wkty comp to win FA Cup final tkts 21st May 2005. Text FA to 87121 to receive entry question(std txt rate)T&C's apply 08452810075over18's"),
                new SmsMessage("ham", "U dun say so early hor... U c already then say..."),
                new SmsMessage("ham", "Nah I don't think he goes to usf, he lives around here though"),
                new SmsMessage("spam", "FreeMsg Hey there darling it's been 3 week's now and no word back! I'd like some fun you up for it still? Txt OK to let me know ur up for fun"),
                new SmsMessage("ham", "Even my brother is not like to speak with me. They treat me like aids patent."),
                new SmsMessage("ham", "As per your request 'Melle Melle (Oru Minnaminunginte Nurungu Vettam)' has been set as your callertune for all Callers. Press 9 to copy your friends Callertune"),
                new SmsMessage("spam", "WINNER!! As a valued network customer you have been selected to receivea £900 prize reward! To claim call 09061701461. Claim code KL341. Valid 12 hours"),
                new SmsMessage("spam", "Had your mobile 11 months or more? U R entitled to Update to the latest colour mobiles with camera for Free! Call The Mobile Update Co FREE on 08002986030"),
                new SmsMessage("ham", "I'm gonna be home soon and I don't want to talk about this stuff anymore tonight, k? I've cried enough today."),
                new SmsMessage("spam", "SIX chances to win CASH! From 100 to 20,000 pounds txt: CSH11 and send to 87575. Cost 150p/day, 6days, 16+ TsandCs apply Reply HL 4 info"),
                new SmsMessage("spam", "URGENT! You have won a 1 week FREE membership in our £100,000 Prize Jackpot! Txt the word: CLAIM to No: 81010 T&C www.dbuk.net LCCLTD POBOX 4403LDNW1A7RW18"),
                new SmsMessage("ham", "I've been searching for the right words to thank you for this breather. I promise I wont take your help for granted and will fulfil my promise. You have been wonderful and a blessing at all times."),
                new SmsMessage("ham", "I HAVE A DATE ON SUNDAY WITH WILL!!"),
                new SmsMessage("spam", "XXXMobileMovieClub: To use your credit, click the WAP link in the next txt message or click here>> http://wap. xxxmobilemovieclub.com?n=QJKGIGHJJGCBL"),
                new SmsMessage("ham", "Oh k...i'm watching here;)"),
                new SmsMessage("ham", "Eh u remember how 2 spell his name... Yes i did. He v naughty make until i v wet."),
                new SmsMessage("ham", "Fine if that's the way u feel. That's the way its gota b"),
                new SmsMessage("spam", "England v Macedonia - dont miss the goals/team news. Txt ur national team to 87077 eg ENGLAND to 87077 Try:WALES, SCOTLAND 4txt/ú1.20 POBOXox36504W45WQ 16+")
        );

        messageRepository.saveAll(initialMessages);
        System.out.println("✅ Loaded " + initialMessages.size() + " initial messages into database");
    }
}