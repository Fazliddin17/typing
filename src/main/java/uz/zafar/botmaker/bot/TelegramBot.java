package uz.zafar.botmaker.bot;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import uz.zafar.botmaker.domain.User;
import uz.zafar.botmaker.dto.ResponseDto;
import uz.zafar.botmaker.service.UserService;

import java.util.ArrayList;
import java.util.List;
@Service
@Log4j2
public class TelegramBot extends TelegramLongPollingBot {


    @Autowired
    private UserService userService;

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        log.info(update);
        User user;
        Long chatId;
        if (update.hasMessage()) {
            Message message = update.getMessage();
            chatId = update.getMessage().getChatId();
            ResponseDto<User> checkUser = userService.checkUser(chatId);
            if (checkUser.isSuccess()) {
                user = checkUser.getData();
            } else {
                user = new User();
                user.setFirstname(message.getFrom().getFirstName());
                user.setLastname(message.getFrom().getLastName());
                user.setUsername(message.getFrom().getUserName());
                user.setChatId(chatId);
                user.setRole("user");
                userService.save(user);
                user = userService.checkUser(chatId).getData();

            }
            if (user.getRole().equals("user")) userRole(update, user);
            else if (user.getRole().equals("admin")) {

                if (update.getMessage().getText().equals("user application")) {
                    user.setRole("user");
                    user.setRole("user");
                    userService.save(user);
                    return;
                }
                if (update.getMessage().getText().equals("count")) {
                    execute(
                            SendMessage
                                    .builder()
                                    .chatId(update.getMessage().getChatId())
                                    .text("Botdagi foydalanuvchilar soni: %d ta".formatted(userService.findAll().getData().size()))
                                    .build()
                    );
                    return;
                }
                if (update.getMessage().getText().equals("/start")) {
                    execute(
                            SendMessage
                                    .builder()
                                    .chatId(update.getMessage().getChatId())
                                    .text("reklamangizni yuboring")
                                    .build()
                    );
                    return;
                }
                String text = update.getMessage().getText();
                List<String> a = new ArrayList<>();
                String[] x = text.split(" ");
                for (String s : x) {
                    if (s.startsWith("https")) {
                        a.add(s);
                    }
                }

                InlineKeyboardButton button;
                List<List<InlineKeyboardButton>> rows = new ArrayList<>();
                for (String s : a) {
                    button = new InlineKeyboardButton();
                    button.setText(s);
                    button.setUrl(s);

                     List<InlineKeyboardButton> row = new ArrayList<>();
                    row.add(button);

                    rows.add(row);
                }

                InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().keyboard(rows).build();
                for (User user1 : userService.findAll().getData()) {
                    execute(
                            SendMessage
                                    .builder()
                                    .chatId(user1.getChatId())
                                    .text(text)
                                    .replyMarkup(markup)
                                    .build()
                    );
                }

            }
        }
    }

    @SneakyThrows
    private void userRole(Update update, User user) {
        if (update.getMessage().getText().equals("admin application")) {
            user.setRole("admin");
            user.setRole("admin");
            userService.save(user);
            return;
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("O'yinni boshlash");
        button.setWebApp(new WebAppInfo("https://monkeytype.com/"));
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row);
        markup.setKeyboard(rows);
        execute(
                SendMessage
                        .builder()
                        .chatId(update.getMessage().getChatId())
                        .replyMarkup(markup)
                        .text("O'yin o'ynash")
                        .build()
        );
    }

    @Override
    public String getBotUsername() {
        return "https://t.me/monkeytypee_bot";
    }

    @Override
    public String getBotToken() {
        return "7999217237:AAFxvIygO73O9bthR3-lA0V4CdEpBjFRYT0";
    }
}
