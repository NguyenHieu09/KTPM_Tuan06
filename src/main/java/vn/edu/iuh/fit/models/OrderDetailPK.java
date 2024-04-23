package vn.edu.iuh.fit.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class OrderDetailPK implements Serializable {
    private Order order;
    private Product product;
}
