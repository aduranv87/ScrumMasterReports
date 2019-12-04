package virtualscrum.report;

public enum VelocityStatusEnum {
    BE_IN_PROD("Supposed to be in Prod"),
    BE_IN_RC("Supposed to be in RC/Prod"),
    PENDING_REVIEW("Pending Code Review > 1 day"),
    PEDING_BACKLOG("Pending in backlog > 1 day");

    public String getValue() {
        return value;
    }

    private String value;

    VelocityStatusEnum(String value) {
        this.value = value;
    }
}
