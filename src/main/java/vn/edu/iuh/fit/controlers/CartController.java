package vn.edu.iuh.fit.controlers;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.models.OrderDetail;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @GetMapping("/cart")
    public String viewCart(HttpSession session) {
        // Lấy danh sách sản phẩm trong giỏ hàng từ session
        List<OrderDetail> cart = (List<OrderDetail>) session.getAttribute("cart");

        // Chuyển danh sách thành chuỗi JSON
        Gson gson = new Gson();
        String cartJson = gson.toJson(cart);

        return cartJson;
    }
}
