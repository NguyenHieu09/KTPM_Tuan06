package vn.edu.iuh.fit;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import vn.edu.iuh.fit.models.Product;
import vn.edu.iuh.fit.repositories.ProductRepository;

@SpringBootApplication
public class KtpmTuan04Application {

    public static void main(String[] args) {
        SpringApplication.run(KtpmTuan04Application.class, args);
    }

    @Autowired
    private ProductRepository productRepository;

//    @Bean
//    CommandLineRunner init(){
//        return args -> {
//            Faker faker = new Faker();
//            for (int i=1; i<40; i++){
//                Product product = new Product(faker.commerce().productName(),  faker.number().randomDouble(2, 1, 1000),faker.number().numberBetween(1, 100),"https://www.lottemart.vn/media/catalog/product/cache/0x0/8/9/8934673576390.jpg.webp");
//                productRepository.save(product);
//
//            }
//        };
//    }
}
