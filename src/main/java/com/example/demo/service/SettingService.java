package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Setting;
import com.example.demo.repository.SettingRepository;

import jakarta.annotation.PostConstruct;

import java.util.Optional;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;


    @PostConstruct
    public void initDefaultSettings() {
        if (settingRepository.findBySettingKey("auto_accept").isEmpty()) {
            // Nếu chưa có key 'auto_accept' trong cơ sở dữ liệu thì tạo mới với giá trị mặc định là 'false'
            settingRepository.save(new Setting(null, "auto_accept", "false"));
        }
    }


    public String getSetting(String key) {
        Optional<Setting> setting = settingRepository.findBySettingKey(key); // Sử dụng settingKey
        return setting.map(Setting::getValue).orElse(null);
    }

    public void updateSetting(String key, String value) {
        Optional<Setting> existingSetting = settingRepository.findBySettingKey(key); // Sử dụng settingKey
        if (existingSetting.isPresent()) {
            Setting setting = existingSetting.get();
            setting.setValue(value);
            settingRepository.save(setting);
        } else {
            Setting newSetting = new Setting(null, key, value);
            settingRepository.save(newSetting);
        }
    }
}
