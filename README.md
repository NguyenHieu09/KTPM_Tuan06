# KTPM_Tuan06
thực hành kiến trúc phần mềm tuần 6
## Danh sách các sản phẩm
![Screenshot 2024-05-03 165208.jpg](img%2FScreenshot%202024-05-03%20165208.jpg)

## Giao điện giỏ hàng
![Screenshot 2024-05-03 165251.jpg](img%2FScreenshot%202024-05-03%20165251.jpg)

## Dùng ActiveMQ
![Screenshot 2024-05-03 165142.jpg](img%2FScreenshot%202024-05-03%20165142.jpg)
Client thông qua ứng dụng (desktop/web) gửi thông tin đặt hàng sau khi chọn lựa.  Thông tin này được chuyển về dạng json và được mã hóa dạng Base64
![Screenshot 2024-05-03 165836.jpg](img%2FScreenshot%202024-05-03%20165836.jpg)

Khi Client click vào checkout.
Hệ thống sử dụng messaging service cho việc lắng nghe việc đặt hàng. 
Khi nhận được đơn hàng từ client, hệ thống sẽ giải mã về dạng json, sau đó kiểm tra số lượng trong kho có đủ không; sau đó quyết định đơn hàng có được xác nhận hay không.
![img_4.png](img/img_4.png)
* Nếu số lượng trong kho không đủ để thực hiện đơn hàng thì hệ thông sẽ gửi email thông báo:
![Screenshot 2024-05-03 165028.jpg](img%2FScreenshot%202024-05-03%20165028.jpg)
* Nếu số lượng trong kho đủ để thực hiện đơn hàng thì hệ thống sẽ cập nhật lại số lượng trong kho và gửi email thông báo
  ![Screenshot 2024-05-03 164718.jpg](img%2FScreenshot%202024-05-03%20164718.jpg)

  ![Screenshot 2024-05-03 165320.jpg](img%2FScreenshot%202024-05-03%20165320.jpg)





