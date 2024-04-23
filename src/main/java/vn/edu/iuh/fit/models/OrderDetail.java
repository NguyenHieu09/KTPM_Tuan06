//package vn.edu.iuh.fit.models;
//
//import com.google.gson.Gson;
//import jakarta.persistence.*;
//import lombok.*;
//import org.apache.tomcat.util.codec.binary.Base64;
//
//import java.time.LocalDateTime;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString
//@Entity
//@Table(name = "OrderDetail")
//@IdClass(OrderDetailPK.class)
//public class OrderDetail {
//
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long id;
//
//    @Id
//    @ManyToOne
//    @JoinColumn(name = "product_order_id", nullable = false)
//    private Order order;
//
//    @Id
//    @ManyToOne
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;
//
//    @Column(name = "quantity", nullable = false)
//    private int quantity;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof OrderDetail that)) return false;
//
//        if (getOrder() != null ? !getOrder().equals(that.getOrder()) : that.getOrder() != null) return false;
//        return getProduct() != null ? getProduct().equals(that.getProduct()) : that.getProduct() == null;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = getOrder() != null ? getOrder().hashCode() : 0;
//        result = 31 * result + (getProduct() != null ? getProduct().hashCode() : 0);
//        return result;
//    }
//
//    public String toJsonString() {
//        Gson gson = new Gson();
//        return gson.toJson(this);
//    }
//
//    public String encodeToJsonString() {
//        // Convert object to JSON string
//        String jsonString = this.toJsonString();
//        // Encode the JSON string to Base64
//        byte[] encodedBytes = Base64.encodeBase64(jsonString.getBytes());
//        return new String(encodedBytes);
//    }
//}
package vn.edu.iuh.fit.models;

import com.google.gson.Gson;
import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "OrderDetail")
@IdClass(OrderDetailPK.class)
public class OrderDetail implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "product_order_id", nullable = false)
    private Order order;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    // Implementing equals and hashCode methods based on the composite key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderDetail that = (OrderDetail) o;

        if (getOrder() != null ? !getOrder().equals(that.getOrder()) : that.getOrder() != null) return false;
        return getProduct() != null ? getProduct().equals(that.getProduct()) : that.getProduct() == null;
    }

    @Override
    public int hashCode() {
        int result = getOrder() != null ? getOrder().hashCode() : 0;
        result = 31 * result + (getProduct() != null ? getProduct().hashCode() : 0);
        return result;
    }
}
