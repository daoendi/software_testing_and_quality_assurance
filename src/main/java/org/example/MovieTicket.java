package org.example;

public class MovieTicket {

    public static int tinhGiaVe(int tuoi, String ngay, boolean laHocSinh, boolean laThanhVien) {
        final int GIA_GOC = 100000;

        if (tuoi < 0 || tuoi > 120) {
            throw new IllegalArgumentException("Tuổi không hợp lệ: " + tuoi);
        }

        if (ngay == null) {
            throw new IllegalArgumentException("Ngày không hợp lệ: null");
        }
        String day = ngay.trim();
        if (!day.equalsIgnoreCase("Mon") &&
                !day.equalsIgnoreCase("Tue") &&
                !day.equalsIgnoreCase("Wed") &&
                !day.equalsIgnoreCase("Thu") &&
                !day.equalsIgnoreCase("Fri") &&
                !day.equalsIgnoreCase("Sat") &&
                !day.equalsIgnoreCase("Sun")) {
            throw new IllegalArgumentException("Ngày không hợp lệ: " + ngay);
        }

        if (tuoi < 6 || tuoi >= 60) {
            return 0;
        }

        double multiplier = 1.0;

        if (tuoi < 12) {
            multiplier -= 0.5;
        }

        if (laHocSinh) {
            multiplier -= 0.3;
        }

        if (laThanhVien) {
            multiplier -= 0.1;
        }

        if (day.equalsIgnoreCase("Tue")) {
            multiplier -= 0.2;
        }

        if (day.equalsIgnoreCase("Sat")) {
            multiplier += 0.2;
        }

        if (multiplier <= 0.0) {
            return 0;
        }

        return (int) Math.round(GIA_GOC * multiplier);
    }

}
