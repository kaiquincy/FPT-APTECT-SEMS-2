package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.SettingService;

@RestController
@RequestMapping("/api/admin/settings")
@CrossOrigin(origins = "*") // Đảm bảo rằng API có thể được gọi từ client
public class SettingController {

    @Autowired
    private SettingService settingService;

    // API lấy giá trị auto_accept
    @GetMapping
    public ResponseEntity<?> getSettings() {
        String autoAccept = settingService.getSetting("auto_accept");
        return ResponseEntity.ok(new ApiResponse(1000, new SettingResponse(autoAccept != null && autoAccept.equals("true"))));
    }

    // API cập nhật auto_accept
    @PostMapping("/auto_accept")
    public ResponseEntity<?> updateAutoAccept(@RequestBody AutoAcceptRequest request) {
        settingService.updateSetting("auto_accept", request.isAutoAccept() ? "true" : "false");
        return ResponseEntity.ok(new ApiResponse(1000, "Auto accept updated successfully"));
    }
    
    // Inner classes for request and response
    public static class AutoAcceptRequest {
        private boolean autoAccept;

        public boolean isAutoAccept() {
            return autoAccept;
        }

        public void setAutoAccept(boolean autoAccept) {
            this.autoAccept = autoAccept;
        }
    }

    public static class SettingResponse {
        private boolean auto_accept;

        public SettingResponse(boolean auto_accept) {
            this.auto_accept = auto_accept;
        }

        public boolean isAuto_accept() {
            return auto_accept;
        }

        public void setAuto_accept(boolean auto_accept) {
            this.auto_accept = auto_accept;
        }
    }

    public static class ApiResponse {
        private int code;
        private Object result;

        public ApiResponse(int code, Object result) {
            this.code = code;
            this.result = result;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }
    }
}