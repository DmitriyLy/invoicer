package io.dmly.invoicer.model.enumaration;

import java.util.Locale;

public enum VerificationUrlType {

    ACCOUNT("ACCOUNT"),
    PASSWORD("PASSWORD");

    private final String type;

    VerificationUrlType(String type) {
        this.type = type;
    }

    public String getType() {
        return type.toLowerCase(Locale.getDefault());
    }
}
