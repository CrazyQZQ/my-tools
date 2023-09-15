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
public class Route {

    private String path;
    private String name;
    private MetaInfo meta;
    List<Route> children;

    @Data
    public static class MetaInfo {
        private String title;
        private String icon;
        private Integer rank;
        private List<String> roles;
        private List<String> auths;
    }
}
