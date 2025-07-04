package com.hotel.gerenciador.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Formatter {
    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.of("pt", "BR"));
    
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
        DecimalFormat formatter = new DecimalFormat("R$ #,##0.00;R$ -#,##0.00", symbols);
        return formatter.format(value);
    }

    public static String formatCurrency(BigDecimal value) {
        if (value == null) {
            return "R$ 0,00"; 
        }
        DecimalFormat formatter = new DecimalFormat("R$ #,##0.00;R$ -#,##0.00", symbols);
        return formatter.format(value);
    }

    public static String formatDecimal(double value, int decimalPlaces) {
        StringBuilder pattern = new StringBuilder("#,##0");
        if (decimalPlaces > 0) {
            pattern.append(".");
            for (int i = 0; i < decimalPlaces; i++) {
                pattern.append("0");
            }
        }
        DecimalFormat formatter = new DecimalFormat(pattern.toString(), symbols);
        formatter.setRoundingMode(RoundingMode.DOWN);
        return formatter.format(value);
    }
    
    public static String formatDecimal(BigDecimal value, int decimalPlaces) {
        if (value == null) {
            return "";
        }
        StringBuilder pattern = new StringBuilder("#,##0");
        if (decimalPlaces > 0) {
            pattern.append(".");
            for (int i = 0; i < decimalPlaces; i++) {
                pattern.append("0");
            }
        }
        DecimalFormat formatter = new DecimalFormat(pattern.toString(), symbols);
        formatter.setRoundingMode(RoundingMode.DOWN);
        return formatter.format(value);
    }

    public static String formatPhone(String phone) {
        if (phone == null || phone.isBlank()) return "";
    
        String digits = phone.replaceAll("\\D", "");
    
        if (digits.length() == 11) {
            return String.format("(%s) %s-%s", digits.substring(0, 2), digits.substring(2, 7), digits.substring(7));
        } else if (digits.length() == 10) {
            return String.format("(%s) %s-%s", digits.substring(0, 2), digits.substring(2, 6), digits.substring(6));
        }
    
        return phone;
    }
    
    public static String formatEmail(String email) {
        if (email == null || email.isBlank()) return "";
        return email.trim().toLowerCase();
    }

    public static String formatCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return (cpf == null) ? "" : cpf;
        }
        String digitsOnly = cpf.replaceAll("\\D", "");
        if (digitsOnly.length() != 11) {
             return (cpf == null) ? "" : cpf;
        }

        return String.format("%s.%s.%s-%s", 
                             digitsOnly.substring(0, 3), 
                             digitsOnly.substring(3, 6), 
                             digitsOnly.substring(6, 9), 
                             digitsOnly.substring(9));
    }
}