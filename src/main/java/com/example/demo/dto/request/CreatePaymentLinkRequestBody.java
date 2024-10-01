package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreatePaymentLinkRequestBody {
  private String productId;
  private String description;
  private String returnUrl;
  private int price;
  private String cancelUrl;

}