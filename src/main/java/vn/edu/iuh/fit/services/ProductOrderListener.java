package vn.edu.iuh.fit.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import vn.edu.iuh.fit.models.Product;
import vn.edu.iuh.fit.repositories.ProductRepository;
import vn.edu.iuh.fit.utils.Base64EncodingText;
import vn.edu.iuh.fit.utils.EncodingText;

import java.util.Optional;

@Component
public class ProductOrderListener {
    private final ProductRepository productRepository;

    public ProductOrderListener(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @JmsListener(destination = "order_products")
    public void receiveMessage(final Message jsonMessage) throws JMSException {
        if (jsonMessage instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) jsonMessage;
            String encodedCartJson = textMessage.getText();

            // Giải mã tin nhắn từ Base64
            EncodingText encodingText = new Base64EncodingText();
            try {
                String cartJson = encodingText.decrypt(encodedCartJson);

                // Kiểm tra số lượng hàng tồn kho
                if (!checkStockAvailability(cartJson)) {
                    // Xử lý đơn hàng nếu hàng tồn kho đủ
                    System.out.println("Stock is available for the order");
                    // Trừ đi số lượng sản phẩm trong kho và cập nhật lại
                    updateStock(cartJson);
                    // Tiếp tục xử lý đơn hàng ở đây
                } else {
                    // Xử lý khi hàng tồn kho không đủ
                    System.out.println("Insufficient stock for the order");
                    // Xử lý thông báo hoặc thực hiện hành động khi hàng tồn kho không đủ
                }
            } catch (Exception e) {
                // Xử lý nếu có lỗi khi giải mã
                e.printStackTrace();
            }
        }
    }

    private boolean checkStockAvailability(String cartJson) {
        try {

            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(cartJson, JsonArray.class);

            for (JsonElement element : jsonArray) {
                JsonObject item = element.getAsJsonObject();
                JsonObject productObject = item.getAsJsonObject("product");

                long productId = Long.parseLong(productObject.get("id").getAsString());
                int quantity = item.get("quantity").getAsInt();

                // Kiểm tra số lượng sản phẩm có sẵn trong kho
                if (isStockAvailable(productId, quantity)) {
                    return false; // Trả về false nếu số lượng sản phẩm không đủ
                }
            }

            return true; // Trả về true nếu số lượng sản phẩm đủ trong kho cho tất cả các sản phẩm trong đơn hàng
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra khi phân tích chuỗi JSON
        }
    }

    public boolean isStockAvailable(long productId, int requiredQuantity) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            // Nếu sản phẩm tồn tại, kiểm tra số lượng có đủ hay không
            Product product = optionalProduct.get();
            if (product.getQuantity() >= requiredQuantity) {
                return true; // Trả về true nếu số lượng sản phẩm đủ
            } else {
                // In ra thông báo nếu số lượng không đủ
                System.out.println("Sản phẩm có ID " + productId + " không đủ số lượng trong kho.");
                return false;
            }
        } else {
            // Nếu không tìm thấy sản phẩm trong kho
            System.out.println("Sản phẩm có ID " + productId + " không tồn tại trong kho.");
            return false;
        }
    }

    private void updateStock(String cartJson) {
        try {

            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(cartJson, JsonArray.class);

            for (JsonElement element : jsonArray) {
                JsonObject item = element.getAsJsonObject();
                JsonObject productObject = item.getAsJsonObject("product");

                long productId = Long.parseLong(productObject.get("id").getAsString());
                int quantity = item.get("quantity").getAsInt();
                System.out.println(quantity);

                // Lấy sản phẩm từ cơ sở dữ liệu
                Optional<Product> optionalProduct = productRepository.findById(productId);
                if (optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();
                    int remainingQuantity = product.getQuantity() - quantity;
                    product.setQuantity(remainingQuantity);
                    // Cập nhật sản phẩm trong cơ sở dữ liệu
                    productRepository.save(product);
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
}
