package com.youpon.home1.bean;

import java.util.List;

/**
 * Created by liuyun on 2017/4/6.
 */
public class Query {

    /**
     * offset : 请求列表的偏移量
     * limit : 请求数量
     * filter : ["字段A","字段B"]
     * query : {"filed1":{"$in":["字段值","字段值"]},"filed3":{"$lt":"字段值"}}
     * order : {"filed1":"desc","filed2":"asc"}
     */

    private String offset;
    private String limit;
    /**
     * filed1 : {"$in":["字段值","字段值"]}
     * filed3 : {"$lt":"字段值"}
     */

    private QueryBean query;
    /**
     * filed1 : desc
     * filed2 : asc
     */

    private OrderBean order;
    private List<String> filter;

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public QueryBean getQuery() {
        return query;
    }

    public void setQuery(QueryBean query) {
        this.query = query;
    }

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public List<String> getFilter() {
        return filter;
    }

    public void setFilter(List<String> filter) {
        this.filter = filter;
    }

    public static class QueryBean {
        private Filed1Bean filed1;
        /**
         * $lt : 字段值
         */

        private Filed3Bean filed3;

        public Filed1Bean getFiled1() {
            return filed1;
        }

        public void setFiled1(Filed1Bean filed1) {
            this.filed1 = filed1;
        }

        public Filed3Bean getFiled3() {
            return filed3;
        }

        public void setFiled3(Filed3Bean filed3) {
            this.filed3 = filed3;
        }

        public static class Filed1Bean {
            private List<String> $in;

            public List<String> get$in() {
                return $in;
            }

            public void set$in(List<String> $in) {
                this.$in = $in;
            }
        }

        public static class Filed3Bean {
            private String $lt;

            public String get$lt() {
                return $lt;
            }

            public void set$lt(String $lt) {
                this.$lt = $lt;
            }
        }
    }

    public static class OrderBean {
        private String filed1;
        private String filed2;

        public String getFiled1() {
            return filed1;
        }

        public void setFiled1(String filed1) {
            this.filed1 = filed1;
        }

        public String getFiled2() {
            return filed2;
        }

        public void setFiled2(String filed2) {
            this.filed2 = filed2;
        }
    }
}
