package gm.inventoryproject.dto;

public class InventoryMovementDto {

    private Long id;
    private Integer quantity;
    private String type; // IN / OUT
    private String date; // string ISO
    private Long productId;

    public InventoryMovementDto() {}

    public InventoryMovementDto(Long id, Integer quantity, String type, String date, Long productId) {
        this.id = id;
        this.quantity = quantity;
        this.type = type;
        this.date = date;
        this.productId = productId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
}

