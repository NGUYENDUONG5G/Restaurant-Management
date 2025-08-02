package org.example.restaurantmangement.Model;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RevenueStatistics {
    private static HashMap<LocalDate, List<Bill>> billsByDate = new HashMap<>();
    private static HashMap<String, Bill> billList = new HashMap<>();
    private static PreparedStatement insertBill, deleteBill, insertFood, deleteFood, selectBill, selectFood;
    private static String insertQueryBill = "INSERT INTO bill_statistic (id, time, value) VALUES(?, ?, ?)";
    private static String insertQueryFood = "INSERT INTO foods_of_bill (id, id_food, size, total) VALUES(?, ?, ?, ?)";
    private static String deleteQueryBill = "DELETE FROM bill_statistic WHERE id = ?";
    private static String deleteQueryFood = "DELETE FROM foods_of_bill WHERE id = ?";
    private static String selectQueryBill = "SELECT * FROM bill_statistic";
    private static String selectQueryFood = "SELECT * FROM foods_of_bill WHERE id = ?";

    static {
        Connection connection = ConnectionDB.getConnection();
        try {
            insertBill = connection.prepareStatement(insertQueryBill);
            deleteBill = connection.prepareStatement(deleteQueryBill);
            insertFood = connection.prepareStatement(insertQueryFood);
            deleteFood = connection.prepareStatement(deleteQueryFood);
            selectBill = connection.prepareStatement(selectQueryBill);
            selectFood = connection.prepareStatement(selectQueryFood);
            setSelectBills();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static HashMap<LocalDate, List<Bill>> getBillsByDate() {
        return billsByDate;
    }

    public static HashMap<String, Bill> getBillList() {
        return billList;
    }

    public static void setAddBill(Bill bill) {
        try {

            LocalDate billDate = bill.getDateBill().toLocalDate();
            billsByDate.putIfAbsent(billDate, new ArrayList<>());
            billsByDate.get(billDate).add(bill);
            billList.put(bill.getIdBill(), bill);


            insertBill.setString(1, bill.getIdBill());
            insertBill.setTimestamp(2, Timestamp.valueOf(bill.getDateBill()));
            insertBill.setLong(3, bill.getValueBill());
            insertBill.executeUpdate();
            for (String id : bill.getFoodList().keySet()) {
                int[] totalFood = bill.getFoodList().get(id);
                if (totalFood[0] != 0) {
                    insertFood.setString(1, bill.getIdBill());
                    insertFood.setString(2, id);
                    insertFood.setString(3, "M");
                    insertFood.setLong(4, totalFood[0]);
                    insertFood.executeUpdate();
                }
                if (totalFood[1] != 0) {
                    insertFood.setString(1, bill.getIdBill());
                    insertFood.setString(2, id);
                    insertFood.setString(3, "L");
                    insertFood.setLong(4, totalFood[1]);
                    insertFood.executeUpdate();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setRemoveBill(Bill bill) {
        List<Bill> bills = billsByDate.get(bill.getDateBill().toLocalDate());

        if (bills != null) {
            bills.remove(bill);
            if (bills.isEmpty()) {
                billsByDate.remove(bill.getDateBill().toLocalDate());
            }
            try {
                deleteBill.setString(1, bill.getIdBill());
                deleteFood.setString(1, bill.getIdBill());
                deleteBill.executeUpdate();
                deleteFood.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        billList.remove(bill.getIdBill());

    }

    private static void setSelectBills() {
        try (ResultSet resultSet = selectBill.executeQuery()) {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                LocalDateTime localDateTime = resultSet.getTimestamp("time").toLocalDateTime();
                long value = resultSet.getLong("value");


                selectFood.setString(1, id);
                ResultSet resultSet1 = selectFood.executeQuery();
                HashMap<String, int[]> totals = new HashMap<>();
                int[] item = new int[2];
                item[0] = 0;
                item[1] = 0;
                while (resultSet1.next()) {
                    String idFood = resultSet1.getString("id_food");
                    String size = resultSet1.getString("size");
                    int total = resultSet1.getInt("total");
                    if (size.equals("S")) {
                        item[0] = total;

                    } else {
                        item[1] = total;
                    }
                }
                totals.put(id, item);
                Bill bill = new Bill(totals, id, localDateTime, value);
                billsByDate.putIfAbsent(localDateTime.toLocalDate(), new ArrayList<>());
                billsByDate.get(localDateTime.toLocalDate()).add(bill);
                billList.put(id,bill);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
