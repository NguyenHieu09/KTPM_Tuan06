package vn.edu.iuh.fit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.models.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}