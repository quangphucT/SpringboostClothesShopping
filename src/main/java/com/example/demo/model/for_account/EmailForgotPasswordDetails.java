package com.example.demo.model.for_account;

import com.example.demo.entity.for_account.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailForgotPasswordDetails {
    private Account receiver;      // hoặc String email nếu bạn không muốn dùng object
    private String subject;
    private String body;
}
