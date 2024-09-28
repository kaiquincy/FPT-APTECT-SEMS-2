package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String settingKey; // Đổi tên từ key thành settingKey
    private String value;

    public Setting() {
    }

    public Setting(Long id, String settingKey, String value) {
        this.id = id;
        this.settingKey = settingKey;
        this.value = value;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSettingKey() {
        return this.settingKey;
    }

    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Setting id(Long id) {
        setId(id);
        return this;
    }

    public Setting settingKey(String settingKey) {
        setSettingKey(settingKey);
        return this;
    }

    public Setting value(String value) {
        setValue(value);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Setting)) {
            return false;
        }
        Setting setting = (Setting) o;
        return Objects.equals(id, setting.id) && Objects.equals(settingKey, setting.settingKey) && Objects.equals(value, setting.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, settingKey, value);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", settingKey='" + getSettingKey() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }

    
}

