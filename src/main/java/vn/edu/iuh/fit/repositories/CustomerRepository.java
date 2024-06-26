package vn.edu.iuh.fit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}