package ru.mbg.palbociclib;

public interface Settings {
    String getFio();
    void setFio(String fio);

    int getOakReadyDays();
    void setOakReadyDays(int days);

    boolean isPushEnabled();
    void setPushEnabled(boolean enabled);
}
