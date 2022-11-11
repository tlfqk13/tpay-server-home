package com.tpay.commons.custom;

import org.springframework.stereotype.Service;

@Service
public class RefundRateCondition {
    public String refundRate(int price){
        if (price < 30000) {
            return "0";
        } else if (price < 50000) {
            return "1500";
        } else if (price < 75000) {
            return "3000";
        } else if (price < 100000) {
            return "5000";
        } else if (price < 125000) {
            return "6000";
        } else if (price < 150000) {
            return "7500";
        } else if (price < 175000) {
            return "9000";
        } else if (price < 200000) {
            return "10000";
        } else if (price < 225000) {
            return "12000";
        } else if (price < 250000) {
            return "13000";
        } else if (price < 275000) {
            return "15000";
        } else if (price < 300000) {
            return "17000";
        } else if (price < 325000) {
            return "19000";
        } else if (price < 350000) {
            return "21000";
        } else if (price < 375000) {
            return "23000";
        } else if (price < 400000) {
            return "24500";
        } else if (price < 425000) {
            return "26000";
        } else if (price < 450000) {
            return "28000";
        } else if (price < 475000) {
            return "30000";
        } else if (price < 500000) {
            return "32000";
        } else if (price < 550000) {
            return "35000";
        } else if (price < 600000) {
            return "37000";
        } else if (price < 650000) {
            return "41000";
        } else if (price < 700000) {
            return "45000";
        } else if (price < 750000) {
            return "50000";
        } else if (price < 800000) {
            return "53000";
        } else if (price < 850000) {
            return "57000";
        } else if (price < 900000) {
            return "60000";
        } else if (price < 950000) {
            return "65000";
        } else if (price < 1000000) {
            return "68000";
        } else if (price < 1100000) {
            return "74000";
        } else if (price < 1200000) {
            return "80000";
        } else if (price < 1300000) {
            return "90000";
        } else if (price < 1400000) {
            return "95000";
        } else if (price < 1500000) {
            return "104000";
        } else if (price < 1600000) {
            return "110000";
        } else if (price < 1700000) {
            return "115000";
        } else if (price < 1800000) {
            return "127000";
        } else if (price < 1900000) {
            return "134000";
        } else if (price < 2000000) {
            return "140000";
        } else if (price < 2100000) {
            return "149000";
        } else if (price < 2200000) {
            return "155000";
        } else if (price < 2300000) {
            return "160000";
        } else if (price < 2400000) {
            return "170000";
        } else if (price < 2500000) {
            return "177000";
        } else if (price < 2600000) {
            return "185000";
        } else if (price < 2700000) {
            return "190000";
        } else if (price < 2800000) {
            return "200000";
        } else if (price < 2900000) {
            return "208000";
        } else if (price < 3000000) {
            return "215000";
        } else if (price < 3100000) {
            return "223000";
        } else if (price < 3200000) {
            return "230000";
        } else if (price < 3300000) {
            return "235000";
        } else if (price < 3400000) {
            return "240000";
        } else if (price < 3500000) {
            return "250000";
        } else if (price < 3600000) {
            return "260000";
        } else if (price < 3700000) {
            return "270000";
        } else if (price < 3800000) {
            return "278000";
        } else if (price < 3900000) {
            return "285000";
        } else if (price < 4000000) {
            return "290000";
        } else if (price < 4100000) {
            return "300000";
        } else if (price < 4200000) {
            return "310000";
        } else if (price < 4300000) {
            return "315000";
        } else if (price < 4400000) {
            return "320000";
        } else if (price < 4500000) {
            return "333000";
        } else if (price < 4600000) {
            return "340000";
        } else if (price < 4700000) {
            return "350000";
        } else if (price < 4800000) {
            return "360000";
        } else if (price < 4900000) {
            return "370000";
        } else if (price < 5000000) {
            return "380000";
        } else if (price < 5100000) {
            return "390000";
        } else if (price < 5200000) {
            return "400000";
        } else if (price < 5300000) {
            return "410000";
        } else if (price < 5400000) {
            return "420000";
        } else if (price < 5500000) {
            return "430000";
        } else if (price < 5600000) {
            return "440000";
        } else if (price < 5700000) {
            return "450000";
        } else if (price < 5800000) {
            return "460000";
        } else if (price < 5900000) {
            return "470000";
        } else if (price < 6000000) {
            return "480000";
        } else {
            int totalRefundResult = (int) Math.ceil(((price / 11) * 0.9) / 100) * 100;
            return Integer.toString(totalRefundResult);
        }
    }
}
