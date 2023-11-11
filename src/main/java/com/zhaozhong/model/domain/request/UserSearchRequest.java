package com.zhaozhong.model.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchRequest implements Serializable {

    private static final long serialVersionUID = -588374729525212516L;

    private Long id;

    private String userAccount;

    private String userName;


}
