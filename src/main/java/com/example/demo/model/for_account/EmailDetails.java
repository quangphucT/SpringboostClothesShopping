package com.example.demo.model.for_account;

import com.example.demo.entity.for_account.Account;
import lombok.Data;

import java.util.Date;

@Data
public class EmailDetails {
    private Account receiver;      // hoặc String email nếu bạn không muốn dùng object
    private String subject;
    private String otpCode;
    private Date expiryTime;
}
