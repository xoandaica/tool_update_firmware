package vn.vnpttech.ssdc.nms.model;

public enum ActionTypeEnum {

    LIST("List", 0),
    CREATE("Create", 1),
    UPDATE("Update", 2),
    DELETE("Delete", 3),
    RESET_PASSWORD("Reset Password", 4),
    UPLOAD("Upload", 5),
    CONFIG_SERVICE("configService", 6),
    RESTART_DEVICE("restartDevice", 7);

    private String typeName;
    private Integer type;

    private ActionTypeEnum(String s, int t) {
        typeName = s;
        type = t;
    }

    public String getName() {
        return typeName;
    }

    public Integer getType() {
        return type;
    }
}
