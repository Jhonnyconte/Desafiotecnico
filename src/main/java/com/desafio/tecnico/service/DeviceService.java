package com.desafio.tecnico.service;

import com.desafio.tecnico.dto.DeviceDTO;
import com.desafio.tecnico.entity.Device;
import com.desafio.tecnico.mapper.DeviceMapper;
import com.desafio.tecnico.repository.DeviceRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class DeviceService {
    private final DeviceRepository repository;
    private final DeviceMapper mapper;

    public DeviceService(DeviceRepository repository, DeviceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<DeviceDTO> getAllDevices(Pageable pageable) {
        return repository.findByRemovedFalse(pageable).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public DeviceDTO getDeviceById(UUID id) {
        return repository.findById(id)
                .filter(device -> !device.isRemoved())
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));
    }

    public DeviceDTO createDevice(DeviceDTO dto) {
        Device device = mapper.toEntity(dto);
        device.setCreatedAt(LocalDateTime.now());
        return mapper.toDTO(repository.save(device));
    }

    public DeviceDTO updateDevice(UUID id, DeviceDTO dto) {
        Device device = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));

        device.setName(dto.name());
        device.setType(dto.type());
        device.setSerialNumber(dto.serialNumber());
        device.setUpdatedAt(LocalDateTime.now());
        return mapper.toDTO(repository.save(device));
    }

    public void deleteDevice(UUID id) {
        Device device = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));

        device.setRemoved(true);
        repository.save(device);
    }
}
