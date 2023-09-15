package com.lxqq.tools.common.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author QinQiang
 * @date 2023/9/14
 * @copyright：Copyright ® 掌控网络. All right reserved.
 **/
@Data
@Accessors(chain = true)
public class User {
    private String username;
    private List<String> roles;
    private String accessToken;
    private String refreshToken;
    private String expires;
}
