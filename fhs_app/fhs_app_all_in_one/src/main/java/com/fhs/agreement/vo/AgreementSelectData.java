package com.fhs.agreement.vo;

import lombok.Data;
import java.util.List;

@Data
public class AgreementSelectData {

    private List<Goodspecification> goodspecifications;

    @Data
    public static class Goodspecification{
        private int id;
        private String title;
    }
}
