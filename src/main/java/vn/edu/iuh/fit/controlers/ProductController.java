package vn.edu.iuh.fit.controlers;

import com.google.gson.Gson;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.iuh.fit.config.GsonConfig;
import vn.edu.iuh.fit.models.Customer;
import vn.edu.iuh.fit.models.Order;
import vn.edu.iuh.fit.models.OrderDetail;
import vn.edu.iuh.fit.models.Product;
import vn.edu.iuh.fit.repositories.CustomerRepository;
import vn.edu.iuh.fit.repositories.OrderDetailRepository;
import vn.edu.iuh.fit.repositories.OrderRepository;
import vn.edu.iuh.fit.repositories.ProductRepository;
import vn.edu.iuh.fit.services.ProductService;
import vn.edu.iuh.fit.services.SendOrderServices;
import vn.edu.iuh.fit.utils.Base64EncodingText;
import vn.edu.iuh.fit.utils.EncodingText;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
//@RequestMapping("/")
public class ProductController {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ProductService productServices;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SendOrderServices sendOrderServices;

    @GetMapping("/product")
    public String productListing(Model model,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Page<Product> productPage = productServices.findAll(currentPage - 1, pageSize, "id", "asc");

        model.addAttribute("productPage", productPage);
        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute(("pageNumbers"), pageNumbers);
        }
        return "product";
    }



    @PostMapping("/cart")
    public String addToCart(@RequestParam long productId, HttpSession session) {
        // Lấy thông tin sản phẩm từ cơ sở dữ liệu
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get(); // Truy cập giá trị thực của Optional
            // Lấy danh sách các chi tiết đơn hàng từ session
            List<OrderDetail> cart = (List<OrderDetail>) session.getAttribute("cart");
            if (cart == null) {
                cart = new ArrayList<>();
                session.setAttribute("cart", cart);
            }
            // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa
            Optional<OrderDetail> existingItem = cart.stream()
                    .filter(item -> item.getProduct().getId() == productId)
                    .findFirst();
            if (existingItem.isPresent()) {
                // Nếu sản phẩm đã tồn tại trong giỏ hàng, tăng số lượng lên
                existingItem.get().setQuantity(existingItem.get().getQuantity() + 1);
            } else {
                // Nếu sản phẩm chưa có trong giỏ hàng, tạo mới một chi tiết đơn hàng
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProduct(product);
                orderDetail.setQuantity(1); // Mặc định số lượng là 1
                cart.add(orderDetail);
            }
            // Cập nhật lại giỏ hàng trong session
            session.setAttribute("cart", cart);
        }
        // Chuyển hướng đến trang giỏ hàng
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session,
                           @RequestParam Map<String, String> quantities,
                           Model model) throws Exception {
        // Lấy thông tin giỏ hàng từ session
        List<OrderDetail> cart = (List<OrderDetail>) session.getAttribute("cart");

        if (cart != null && !cart.isEmpty()) {
            // Tạo một đối tượng Order mới
            Order order = new Order();

            // Tạo một đối tượng Customer mới
            Customer customer = new Customer();
            customer.setName("Nguyễn Thị Trung Hiếu");
            customer.setEmail("hieu@gmail.com");
            customer.setPhone("09384953456");
            customer.setAddress("12 Nguyễn Văn Bảo, phường 4, Quận Gò Vấp");

            // Lưu khách hàng vào cơ sở dữ liệu
            customerRepository.save(customer);

            // Thiết lập thông tin khách hàng cho đơn hàng
            order.setCustomer(customer);

            // Thiết lập thời điểm đặt hàng
            order.setOrderDate(LocalDateTime.now());

            // Lưu đơn hàng vào cơ sở dữ liệu
            order = orderRepository.save(order);

            // Lưu từng chi tiết đơn hàng vào cơ sở dữ liệu
            for (OrderDetail orderDetail : cart) {
                Optional<Product> productOptional = productRepository.findById(orderDetail.getProduct().getId());
                if (productOptional.isPresent()) {
                    Product product = productOptional.get();
                    // Thiết lập sản phẩm cho chi tiết đơn hàng
                    orderDetail.setProduct(product);
                    // Thiết lập đơn hàng cho chi tiết đơn hàng
                    orderDetail.setOrder(order);
                    // Lấy số lượng của sản phẩm từ map
                    String productId = String.valueOf(product.getId());
                    String quantityStr = quantities.get("quantity_" + productId);
                    if (quantityStr != null) {
                        int quantity = Integer.parseInt(quantityStr);
                        orderDetail.setQuantity(quantity);
                    }
                    // Lưu chi tiết đơn hàng vào cơ sở dữ liệu
                    orderDetailRepository.save(orderDetail);
                }
            }

            // Sử dụng Gson để chuyển đổi giỏ hàng thành chuỗi JSON
            Gson gson = GsonConfig.getGson();
            String cartJson = gson.toJson(cart);

            // Gửi đơn hàng đến dịch vụ xử lý đơn hàng
            sendOrderServices.sendOrder(cartJson);

            // Xóa giỏ hàng sau khi đã đặt hàng thành công
            session.removeAttribute("cart");

            // Chuyển hướng đến trang thông báo đặt hàng thành công hoặc trang chính
            return "/success"; // hoặc return "redirect:/";
        } else {
            // Trường hợp giỏ hàng trống, chuyển hướng đến trang giỏ hàng
            return "redirect:/cart";
        }
    }









    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        // Lấy danh sách sản phẩm trong giỏ hàng từ session và gửi nó đến view
        List<OrderDetail> cart = (List<OrderDetail>) session.getAttribute("cart");
        model.addAttribute("cart", cart);
        return "cart"; // Trả về view "cart.html" hoặc tên của trang bạn muốn hiển thị giỏ hàng trên đó
    }







}





