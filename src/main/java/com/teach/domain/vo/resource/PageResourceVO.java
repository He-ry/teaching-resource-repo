package com.teach.domain.vo.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceVO {
    /**
     * 分页情况
     */
    private Pagination pagination;
    /**
     * 资源
     */
    private List<DataResources> resources;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pagination {
        /**
         * 当前页数
         */
        private String page;
        /**
         * 当前每页
         */
        private String perPage;
        /**
         * 共多少页
         */
        private String total;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataResources {
        /**
         * 资源数量
         */
        private long count;
        /**
         * 资源库描述
         */
        private String description;
        /**
         * 图标标识，仅支持：mdi--file-document，mdi--lightbulb-on，mdi--heart-multiple，di--tools。
         * 对应的就是：制度规范库，研究成果库，思政资源库，实用方法库
         */
        private String icon;
        /**
         * 标签页
         */
        private List<Tabs> tabs;
        /**
         * 资源库名称
         */
        private String title;
        /**
         * 资源库类型标识
         */
        private String value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tabs {
        /**
         * 子类
         */
        private List<Children> children;
        /**
         * 标签标识
         */
        private String key;
        /**
         * 标签名称
         */
        private String title;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Children {
        /**
         * 子标签标识
         */
        private String key;
        /**
         * 资源
         */
        private List<ChildrenResources> resources;
        /**
         * 子标签名称
         */
        private String title;
    }

    /**
     * 资源
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChildrenResources {
        /**
         * 资源描述
         */
        private String description;
        /**
         * 资源ID
         */
        private long id;
        /**
         * 资源名称
         */
        private String name;
        /**
         * 子子类型
         */
        private String subSubType;
        /**
         * 子类型
         */
        private String subType;
        /**
         * 资源类型
         */
        private String type;
        /**
         * 浏览次数
         */
        private long viewCount;
    }


}
