package com.desafio.tecnico.controller;


import com.desafio.tecnico.dto.DeviceDTO;
import com.desafio.tecnico.service.DeviceService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    private final DeviceService service;

    public DeviceController(DeviceService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DeviceDTO>> getAllDevices(@PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(service.getAllDevices(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDTO> getDeviceById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getDeviceById(id));
    }

    @PostMapping
    public ResponseEntity<DeviceDTO> createDevice(@RequestBody DeviceDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createDevice(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceDTO> updateDevice(@PathVariable UUID id, @RequestBody DeviceDTO dto) {
        return ResponseEntity.ok(service.updateDevice(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID id) {
        service.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}