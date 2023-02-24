package com.flatrock.common.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

@ConfigurationProperties(
    prefix = "application",
    ignoreUnknownFields = false
)
public class AppProperties {
    private final Security security = new Security();

    private final CorsConfiguration cors = new CorsConfiguration();

    private final ClientApp clientApp = new ClientApp();
    private final Topic topic = new Topic();

    private final Services services = new Services();


    public AppProperties() {
    }

    public Security getSecurity() {
        return this.security;
    }

    public CorsConfiguration getCors() {
        return this.cors;
    }

    public ClientApp getClientApp() {
        return this.clientApp;
    }

    public Topic getTopic() {
        return topic;
    }

    public Services getServices() {
        return services;
    }

    public static class Security {
        private final Authentication authentication = new Authentication();

        public Security() {
        }

        public Authentication getAuthentication() {
            return authentication;
        }

        public static class Authentication {
            private final Jwt jwt = new Jwt();

            public Authentication() {
            }

            public Jwt getJwt() {
                return this.jwt;
            }

            public static class Jwt {
                private String secret;
                private String base64Secret;
                private long tokenValidityInSeconds;

                public Jwt() {
                    this.tokenValidityInSeconds = 1800L;
                }

                public String getSecret() {
                    return this.secret;
                }

                public void setSecret(String secret) {
                    this.secret = secret;
                }

                public String getBase64Secret() {
                    return this.base64Secret;
                }

                public void setBase64Secret(String base64Secret) {
                    this.base64Secret = base64Secret;
                }

                public long getTokenValidityInSeconds() {
                    return this.tokenValidityInSeconds;
                }

                public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
                    this.tokenValidityInSeconds = tokenValidityInSeconds;
                }
            }
        }
    }

    public static class ClientApp {
        private String name = "FlatRockApp";

        public ClientApp() {
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Topic {
        private String orderCreated;

        private String customerNotification;

        private String sellerNotification;

        private String orderCanceled;

        private String orderReversal;

        private String reserveFailed;
        public Topic() {
        }

        public void setOrderCreated(String orderCreated) {
            this.orderCreated = orderCreated;
        }

        public String getOrderCreated() {
            return orderCreated;
        }

        public String getCustomerNotification() {
            return customerNotification;
        }

        public void setCustomerNotification(String customerNotification) {
            this.customerNotification = customerNotification;
        }

        public String getSellerNotification() {
            return sellerNotification;
        }

        public void setSellerNotification(String sellerNotification) {
            this.sellerNotification = sellerNotification;
        }

        public String getOrderCanceled() {
            return orderCanceled;
        }

        public void setOrderCanceled(String orderCanceled) {
            this.orderCanceled = orderCanceled;
        }

        public String getOrderReversal() {
            return orderReversal;
        }

        public void setOrderReversal(String orderReversal) {
            this.orderReversal = orderReversal;
        }

        public String getReserveFailed() {
            return reserveFailed;
        }

        public void setReserveFailed(String reserveFailed) {
            this.reserveFailed = reserveFailed;
        }
    }

    public static class Services {
        private String user;
        private String product;
        private String notification;

        private String delivery;

        private String order;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getNotification() {
            return notification;
        }

        public void setNotification(String notification) {
            this.notification = notification;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public Services() {
        }
    }
}
