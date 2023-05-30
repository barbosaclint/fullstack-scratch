package com.kurinto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/")
    public Greet greet() {
//        return "Hello";
        return new Greet(
                "tite",
                List.of("sadadsa", "asdadsad", "asdadasda"),
                new Person("Clintutan"));
    }

    record Person(String name) {}
    record Greet(
            String greet,
            List<String> favLanguages,
            Person person

    ){}

//    Same AS RECORD CLASS
//    class Greet{
//        private final String greet;
//
//        Greet(String greet) {
//            this.greet = greet;
//        }
//
//        public String getGreet() {
//            return greet;
//        }
//
//        @Override
//        public String toString() {
//            return "Greet{" +
//                    "greet='" + greet + '\'' +
//                    '}';
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            Greet greet1 = (Greet) o;
//            return Objects.equals(greet, greet1.greet);
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(greet);
//        }
//    }
}
