package ru.mbg.palbociclib.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class BackgroundTherapy extends RealmObject {
    private int type = BackgroundTherapyType.none.rawValue;
    @Required
    private Date date;

    public BackgroundTherapyType getType() {
        return BackgroundTherapyType.fromRawValue(type);
    }

    public void setType(BackgroundTherapyType type) {
        this.type = type.rawValue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
