package com.flatrock.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderSellersData {
    private Long orderId;
    private List<SellerItemData> sellerItems = new ArrayList<>();

    public void addSellerItemData(SellerItemData sellerItemData) {
        sellerItems.add(sellerItemData);
    }
}
