package com.desafio.tecnico.service;

import com.desafio.tecnico.dto.DeviceDTO;
import com.desafio.tecnico.dto.RabbitMQMessage;
import com.desafio.tecnico.entity.Device;
import com.desafio.tecnico.mapper.DeviceMapper;
import com.desafio.tecnico.repository.DeviceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final RabbitMQProducer rabbitMQProducer;
    private final ObjectMapper objectMapper;

    public DeviceService(DeviceRepository repository, DeviceMapper mapper, RabbitMQProducer rabbitMQProducer, ObjectMapper objectMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.rabbitMQProducer = rabbitMQProducer;
        this.objectMapper = objectMapper;
    }

    public List<DeviceDTO> getAllDevices(Pageable pageable) {
        return repository.findByRemovedFalse(pageable).stream()
                .filter(device -> !device.isRemoved())
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
            Device savedDevice = repository.save(device);
            DeviceDTO deviceDTO = mapper.toDTO(savedDevice);
            sendMessageToRabbitMQ("CREATE", deviceDTO);
            return deviceDTO;
    }

    public DeviceDTO updateDevice(UUID id, DeviceDTO dto) {
            Device device = repository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));

            device.setName(dto.name());
            device.setType(dto.type());
            device.setSerialNumber(dto.serialNumber());
            device.setUpdatedAt(LocalDateTime.now());
            Device save = repository.save(device);
            DeviceDTO deviceDTO = mapper.toDTO(save);
            sendMessageToRabbitMQ("UPDATE", deviceDTO);
            return deviceDTO;
    }

    public void deleteDevice(UUID id) {
        Device device = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));
        device.setRemoved(true);
        Device save = repository.save(device);
        DeviceDTO deviceDTO = mapper.toDTO(save);
        sendMessageToRabbitMQ("DELETE", deviceDTO);
    }

    private void sendMessageToRabbitMQ(String operation, DeviceDTO deviceDTO) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(new RabbitMQMessage(operation, deviceDTO));
            rabbitMQProducer.sendMessage(jsonMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter mensagem para JSON", e);
        }
    }
}
