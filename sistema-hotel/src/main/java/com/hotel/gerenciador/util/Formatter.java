package com.hotel.gerenciador.util;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatter {
    public static String formatDate(LocalDate date) {
        if (date == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    public static String formatCurrency(double value) {
        DecimalFormat formatter = new DecimalFormat("R$ #,##0.00");
        return formatter.format(value);
    }

    public static String formatDecimal(double value, int decimalPlaces) {
        StringBuilder pattern = new StringBuilder("#");
        for (int i = 0; i < decimalPlaces; i++) {
            pattern.append(",0");
        }
        DecimalFormat formatter = new DecimalFormat(pattern.toString());
        return formatter.format(value);
    }

    public static String formatPhone(String phone) {
        if (phone == null || phone.length() != 11) return phone;
        return String.format("(%s) %s-%s", phone.substring(0, 2), phone.substring(2, 7), phone.substring(7));
    }
    public static String formatEmail(String email) {
        if (email == null) return email;
        return email.trim().toLowerCase();
    }
    public static String formatCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return String.format("%s.%s.%s-%s", 
                             cpf.substring(0, 3), 
                             cpf.substring(3, 6), 
                             cpf.substring(6, 9), 
                             cpf.substring(9));
    }
   
}
