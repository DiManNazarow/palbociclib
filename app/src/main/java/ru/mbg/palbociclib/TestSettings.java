package ru.mbg.palbociclib;

class TestSettings implements Settings {
    private String fio;
    private int oak;
    private  boolean push;

    @Override
    public String getFio() {
        return fio;
    }

    @Override
    public void setFio(String fio) {
        this.fio = fio;
    }

    @Override
    public int getOakReadyDays() {
        return oak;
    }

    @Override
    public void setOakReadyDays(int days) {
        oak = days;
    }

    @Override
    public boolean isPushEnabled() {
        return push;
    }

    @Override
    public void setPushEnabled(boolean enabled) {
        push = enabled;
    }
}
