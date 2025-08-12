package org.example.restaurantmangement.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Invoice {
    private HashMap<String, int[]> foodList;
    private String idInvoice;
    private LocalDateTime dateInvoice;
    private long valueInvoice;


    public Invoice(HashMap<String, int[]> foodList, int sequenceNumberOfDay) {
        this.foodList = foodList;
        this.dateInvoice = LocalDateTime.now();
        this.idInvoice = setIdinvoice(sequenceNumberOfDay);
        setValueinvoice();
    }

    public Invoice(HashMap<String, int[]> foodList, String idinvoice, LocalDateTime dateinvoice, long valueinvoice) {
        this.foodList = foodList;
        this.idInvoice = idinvoice;
        this.dateInvoice = dateinvoice;
        this.valueInvoice = valueinvoice;
    }

    public static String setIdinvoice(int sequenceNumberOfDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String ngay = LocalDate.now().format(formatter);
        return "HD" + ngay + "-" + sequenceNumberOfDay;
    }

    public HashMap<String, int[]> getFoodList() {
        return foodList;
    }

    public void setValueinvoice() {
        for (String id : foodList.keySet()) {
            this.valueInvoice += Menu.getFoods().get(id).getPrice("M") * foodList.get(id)[0];
            this.valueInvoice += Menu.getFoods().get(id).getPrice("L") * foodList.get(id)[1];
        }
    }

    public long getValueInvoice() {
        return valueInvoice;
    }

    public LocalDateTime getDateInvoice() {
        return dateInvoice;
    }

    public String getIdInvoice() {
        return idInvoice;
    }
}
