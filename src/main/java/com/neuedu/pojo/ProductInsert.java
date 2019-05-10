package com.neuedu.pojo;

import java.math.BigDecimal;

public class ProductInsert {
    private Integer id;

    private Integer categoryId;
     private String mainImage;
    private  Integer Status;
    private BigDecimal price;
    private Integer stock;



    @Override
    public String toString() {
        return "ProductInsert{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", Status=" + Status +
                ", price=" + price +
                ", stock=" + stock +
                ", name='" + name + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", mainImage='" + mainImage + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public ProductInsert(Integer categoryId, Integer status, BigDecimal price, String name, String subtitle, String detail, Integer stock,String mainImage) {
        this.categoryId = categoryId;
        Status = status;
        this.price = price;
        this.name = name;
        this.subtitle = subtitle;
        this.detail = detail;
        this.stock=stock;
        this.mainImage=mainImage;
    }

    private String name;

    private String subtitle;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


    private String detail;
}
