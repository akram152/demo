package com.example.demo.controllers;

import com.example.demo.entities.Notification;
import com.example.demo.services.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {
    private final INotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getMesNotifications() {
        return ResponseEntity.ok(notificationService.getMesNotifications());
    }

    @PutMapping("/{id}/lue")
    public ResponseEntity<Void> marquerCommeLue(@PathVariable Long id) {
        notificationService.marquerCommeLue(id);
        return ResponseEntity.ok().build();
    }
}
