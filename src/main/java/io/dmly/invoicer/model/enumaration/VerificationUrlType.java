package io.dmly.invoicer.model.enumaration;

public enum VerificationUrlType {

    ACCOUNT("ACCOUNT"),
    PASSWORD("PASSWORD");

    private final String type;

    VerificationUrlType(String type) {
        this.type = type;
    }

    public String getType() {
        return type.toLowerCase();
    }
}
