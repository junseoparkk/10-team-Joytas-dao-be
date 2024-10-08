package com.example.daobe.user.domain;

import static com.example.daobe.user.exception.UserExceptionType.NOT_MATCH_REASON_TYPE;

import com.example.daobe.user.exception.UserException;
import java.util.Arrays;

public enum ReasonType {
    W0001("서비스에 대한 흥미를 잃었어요"),
    W0002("사용 빈도가 낮아요"),
    W0003("이용이 불편하고 장애가 많아요"),
    W0004("특별한 이유가 없어요"),
    W0005("기타"),
    ;

    private final String description;

    ReasonType(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

    public static ReasonType getReasonTypeByString(String stringValue) {
        return Arrays.stream(ReasonType.values())
                .filter(reasonType -> reasonType.name().equalsIgnoreCase(stringValue))
                .findFirst()
                .orElseThrow(() -> new UserException(NOT_MATCH_REASON_TYPE));
    }
}
