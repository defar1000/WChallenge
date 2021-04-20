package co.com.wolox.WChallenge.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class User {
    private int id;
    private String name;
    private String email;
    private Address address;
    private String phone;
    private String website;
    private Company company;

    @Data
    public class Address {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private Geo geo;

        @Data
        public class Geo {
            private String lat;
            private String lng;
        }
    }

    @Data
    public class Company {
        private String name;
        private String catchPhrase;
        private String bs;
    }
}
