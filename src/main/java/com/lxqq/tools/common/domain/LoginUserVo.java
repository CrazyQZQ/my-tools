package com.lxqq.tools.common.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author QinQiang
 * @date 2023/9/14
 **/
@Data
@Accessors(chain = true)
public class LoginUserVo {
    private String username;
    private String password;
    private List<String> roles;
    private String accessToken;
    private String refreshToken;
    private String expires;
}
