package uz.zafar.botmaker.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    @Column(unique = true)
    private Long chatId;
    private String phone;
    private String role;
    private Integer page;
    private String eventCode;
}
