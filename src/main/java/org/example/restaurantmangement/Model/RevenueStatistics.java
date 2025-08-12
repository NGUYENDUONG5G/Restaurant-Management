package org.example.restaurantmangement.Model;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RevenueStatistics {
    private static HashMap<LocalDate, List<Invoice>> invoicesByDate = new HashMap<>();
    private static HashMap<String, Invoice> invoiceList = new HashMap<>();
    private static PreparedStatement insertInvoice, deleteInvoice, insertFood, deleteFood, selectInvoice, selectFood;
    private static String insertQueryinvoice = "INSERT INTO bill_statistic (id, time, value) VALUES(?, ?, ?)";
    private static String insertQueryFood = "INSERT INTO foods_of_bill (id, id_food, size, total) VALUES(?, ?, ?, ?)";
    private static String deleteQueryinvoice = "DELETE FROM bill_statistic WHERE id = ?";
    private static String deleteQueryFood = "DELETE FROM foods_of_bill WHERE id = ?";
    private static String selectQueryInvoice = "SELECT * FROM bill_statistic";
    private static String selectQueryFood = "SELECT * FROM foods_of_bill WHERE id = ?";

    static {
        Connection connection = ConnectionDB.getConnection();
        try {
            insertInvoice = connection.prepareStatement(insertQueryinvoice);
            deleteInvoice = connection.prepareStatement(deleteQueryinvoice);
            insertFood = connection.prepareStatement(insertQueryFood);
            deleteFood = connection.prepareStatement(deleteQueryFood);
            selectInvoice = connection.prepareStatement(selectQueryInvoice);
            selectFood = connection.prepareStatement(selectQueryFood);
            setSelectInvoices();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static int getMinYear() {
        int minYear = Integer.MAX_VALUE;
        for (LocalDate date : invoicesByDate.keySet()) {
            if (date.getYear() < minYear) {
                minYear = date.getYear();
            }
        }
        return minYear;
    }

    public static HashMap<LocalDate, List<Invoice>> getInvoicesByDate() {
        return invoicesByDate;
    }

    public static HashMap<String, Invoice> getInvoiceList() {
        return invoiceList;
    }

    public static void setAddinvoice(Invoice invoice) {
        try {

            LocalDate invoiceDate = invoice.getDateInvoice().toLocalDate();
            invoicesByDate.putIfAbsent(invoiceDate, new ArrayList<>());
            invoicesByDate.get(invoiceDate).add(invoice);
            invoiceList.put(invoice.getIdInvoice(), invoice);


            insertInvoice.setString(1, invoice.getIdInvoice());
            insertInvoice.setTimestamp(2, Timestamp.valueOf(invoice.getDateInvoice()));
            insertInvoice.setLong(3, invoice.getValueInvoice());
            insertInvoice.executeUpdate();
            for (String id : invoice.getFoodList().keySet()) {
                int[] totalFood = invoice.getFoodList().get(id);
                if (totalFood[0] != 0) {
                    insertFood.setString(1, invoice.getIdInvoice());
                    insertFood.setString(2, id);
                    insertFood.setString(3, "M");
                    insertFood.setLong(4, totalFood[0]);
                    insertFood.executeUpdate();
                }
                if (totalFood[1] != 0) {
                    insertFood.setString(1, invoice.getIdInvoice());
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

    public static void setRemoveinvoice(Invoice invoice) {
        List<Invoice> invoices = invoicesByDate.get(invoice.getDateInvoice().toLocalDate());

        if (invoices != null) {
            invoices.remove(invoice);
            if (invoices.isEmpty()) {
                invoicesByDate.remove(invoice.getDateInvoice().toLocalDate());
            }
            try {
                deleteInvoice.setString(1, invoice.getIdInvoice());
                deleteFood.setString(1, invoice.getIdInvoice());
                deleteInvoice.executeUpdate();
                deleteFood.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        invoiceList.remove(invoice.getIdInvoice());

    }

    private static void setSelectInvoices() {
        try (ResultSet resultSet = selectInvoice.executeQuery()) {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                LocalDateTime localDateTime = resultSet.getTimestamp("time").toLocalDateTime();
                long value = resultSet.getLong("value");


                selectFood.setString(1, id);
                ResultSet resultSet1 = selectFood.executeQuery();
                HashMap<String, int[]> totals = new HashMap<>();

                while (resultSet1.next()) {
                    String idFood = resultSet1.getString("id_food");
                    String size = resultSet1.getString("size");
                    int total = resultSet1.getInt("total");
                    totals.putIfAbsent(idFood, new int[]{0, 0});
                    int[] item = totals.get(idFood);
                    if (size.equals("M")) {
                        item[0] = total;
                    } else if (size.equals("L")) {
                        item[1] = total;
                    }
                }

                Invoice invoice = new Invoice(totals, id, localDateTime, value);
                invoicesByDate.putIfAbsent(localDateTime.toLocalDate(), new ArrayList<>());
                invoicesByDate.get(localDateTime.toLocalDate()).add(invoice);
                invoiceList.put(id, invoice);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
