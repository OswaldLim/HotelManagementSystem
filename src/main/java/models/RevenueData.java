package models;

public class RevenueData {
    private final String month;
    private final Double totalAmount;
    private final Double occupancyRate;
    private final String paymentType;
    private final Integer transactionAmount;

    public RevenueData(String paymentType, double totalAmount, int transactionAmount) {
        this.paymentType = paymentType;
        this.totalAmount = totalAmount;
        this.transactionAmount = transactionAmount;

        this.month = null;
        this.occupancyRate=null;
    }

    public RevenueData(String month, double totalAmount, double occupancyRate){
        this.month = month;
        this.totalAmount = totalAmount;
        this.occupancyRate = occupancyRate;

        this.paymentType = null;
        this.transactionAmount = null;
    }

    public String getMonth() {
        return month;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public Double getOccupancyRate() {
        return occupancyRate;
    }

    public Integer getTransactionAmount() {
        return transactionAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }
}