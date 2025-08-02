package org.example.restaurantmangement.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Bill {
    private HashMap<String, int[]> foodList;
    private String idBill;
    private LocalDateTime dateBill;
    private long valueBill;


    public Bill(HashMap<String, int[]> foodList, int sequenceNumberOfDay) {
        this.foodList = foodList;
        this.dateBill = LocalDateTime.now();
        this.idBill = setIdBill(sequenceNumberOfDay);
        setValueBill();
    }

    public Bill(HashMap<String, int[]> foodList, String idBill, LocalDateTime dateBill, long valueBill) {
        this.foodList = foodList;
        this.idBill = idBill;
        this.dateBill = dateBill;
        this.valueBill = valueBill;
    }

    public static String setIdBill(int sequenceNumberOfDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String ngay = LocalDate.now().format(formatter);
        return "HD" + ngay + "-" + sequenceNumberOfDay;
    }

    public HashMap<String, int[]> getFoodList() {
        return foodList;
    }

    public void setValueBill() {
        for (String id : foodList.keySet()) {
            this.valueBill += Menu.getFoods().get(id).getPrice("M") * foodList.get(id)[0];
            this.valueBill += Menu.getFoods().get(id).getPrice("L") * foodList.get(id)[1];
        }
    }

    public long getValueBill() {
        return valueBill;
    }

    public LocalDateTime getDateBill() {
        return dateBill;
    }

    public String getIdBill() {
        return idBill;
    }
}
