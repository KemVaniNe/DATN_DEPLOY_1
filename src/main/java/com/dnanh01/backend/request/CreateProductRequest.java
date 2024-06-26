package com.dnanh01.backend.request;

import java.util.HashSet;
import java.util.Set;

public class CreateProductRequest {

    private String title;
    private String description;
    private int price;
    private int warehousePrice;
    private int discountedPrice;
    private int discountPersent;
    private int quantity;
    private String color ;
    private BrandRequest brand;
    private String imageUrl;
	public CreateProductRequest(String title, String description, int price, int warehousePrice, int discountedPrice,
			int discountPersent, int quantity, String color, BrandRequest brand, String imageUrl) {
		super();
		this.title = title;
		this.description = description;
		this.price = price;
		this.warehousePrice = warehousePrice;
		this.discountedPrice = discountedPrice;
		this.discountPersent = discountPersent;
		this.quantity = quantity;
		this.color = color;
		this.brand = brand;
		this.imageUrl = imageUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getWarehousePrice() {
		return warehousePrice;
	}
	public void setWarehousePrice(int warehousePrice) {
		this.warehousePrice = warehousePrice;
	}
	public int getDiscountedPrice() {
		return discountedPrice;
	}
	public void setDiscountedPrice(int discountedPrice) {
		this.discountedPrice = discountedPrice;
	}
	public int getDiscountPersent() {
		return discountPersent;
	}
	public void setDiscountPersent(int discountPersent) {
		this.discountPersent = discountPersent;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public BrandRequest getBrand() {
		return brand;
	}
	public void setBrand(BrandRequest brand) {
		this.brand = brand;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

    


}
