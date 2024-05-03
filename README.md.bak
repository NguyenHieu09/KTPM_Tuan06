# KTPM_Tuan05
thực hành kiến trúc phần mềm tuần 5
### Danh sách các sản phẩm
![img.png](img.png)

## Giao điện giỏ hàng
Client thông qua ứng dụng (desktop/web) gửi thông tin đặt hàng sau khi chọn lựa.  Thông tin này được chuyển về dạng json và được mã hóa dạng Base64

![img_1.png](img_1.png)

### Dùng ActiveMQ

Khi Client click vào checkout.
Hệ thống sử dụng messaging service cho việc lắng nghe việc đặt hàng. Khi nhận được đơn hàng từ client, hệ thống sẽ kiểm tra số lượng trong kho có đủ không; sau đó quyết định đơn hàng có được xác nhận hay không.
* Nếu số lượng trong kho không đủ để thực hiện đơn hàng thì hệ thông sẽ thông báo: Sản phẩm có ID nào không đủ số lượng trong kho

>![img_3.png](img_3.png)

* Nếu số lượng trong kho đủ để thực hiện đơn hàng thì hệ thông sẽ thông báo: Stock is available for the order

>![img_2.png](img_2.png)




