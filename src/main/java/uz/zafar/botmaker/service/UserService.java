package uz.zafar.botmaker.service;

import uz.zafar.botmaker.domain.User;
import uz.zafar.botmaker.dto.ResponseDto;

import java.util.List;

public interface UserService {
    ResponseDto<User> checkUser(Long chatId);

    ResponseDto<List<User>> findAll();

    ResponseDto<List<User>> findAllByRole(String role);

    ResponseDto<User> save(User user);
}
