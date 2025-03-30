package com.myproject.community.api.post.dto;

import java.util.Locale;

public enum PeriodType {

        DAILY, WEEKLY, MONTHLY;

        public static PeriodType from(String value) {
                try {
                        return PeriodType.valueOf(value.toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException | NullPointerException e) {
                        throw new IllegalArgumentException("올바르지 않은 period 값입니다: " + value);
                }
        }

}
