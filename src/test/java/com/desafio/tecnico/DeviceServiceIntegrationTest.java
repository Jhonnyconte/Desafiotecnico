package com.desafio.tecnico;

import com.desafio.tecnico.dto.DeviceDTO;
import com.desafio.tecnico.repository.DeviceRepository;
import com.desafio.tecnico.service.DeviceService;
import com.desafio.tecnico.service.RabbitMQProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@ExtendWith(MockitoExtension.class)
@Transactional
class DeviceServiceIntegrationTest {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceRepository deviceRepository;

    @MockBean
    private RabbitMQProducer rabbitMQProducer;
    @BeforeEach
    void setUp() {
        deviceRepository.deleteAll();
    }

    @Test
    void testCreateDevice() {
        DeviceDTO dto = new DeviceDTO(null, "Sensor de Temperatura", "sensor", "SR1234561111");
        DeviceDTO created = deviceService.createDevice(dto);

        assertThat(created).isNotNull();
        assertThat(created.id()).isNotNull();
        assertThat(created.name()).isEqualTo("Sensor de Temperatura");

        verify(rabbitMQProducer, times(1)).sendMessage(anyString());
    }

    @Test
    void testGetDeviceById() {
        DeviceDTO dto = new DeviceDTO(null, "Sensor de Temperatura", "sensor", "SR123456111");
        DeviceDTO created = deviceService.createDevice(dto);
        UUID id = created.id();

        DeviceDTO found = deviceService.getDeviceById(id);
        assertThat(found).isNotNull();
        assertThat(found.name()).isEqualTo("Sensor de Temperatura");
    }

    @Test
    void testUpdateDevice() {
        DeviceDTO dto = new DeviceDTO(null, "Sensor de Temperatura", "sensor", "SR12345611");
        DeviceDTO created = deviceService.createDevice(dto);
        UUID id = created.id();

        DeviceDTO updatedDTO = new DeviceDTO(id, "Sensor de Pressão", "sensor", "SR123456111111");
        DeviceDTO updated = deviceService.updateDevice(id, updatedDTO);

        assertThat(updated.name()).isEqualTo("Sensor de Pressão");
        verify(rabbitMQProducer, times(2)).sendMessage(anyString());
    }

    @Test
    void testDeleteDevice() {
        DeviceDTO dto = new DeviceDTO(null, "Sensor de Temperatura", "sensor", "SR12345611111");
        DeviceDTO created = deviceService.createDevice(dto);
        UUID id = created.id();

        deviceService.deleteDevice(id);
        assertThrows(ResponseStatusException.class, () -> deviceService.getDeviceById(id));
        verify(rabbitMQProducer, times(2)).sendMessage(anyString());
    }

    @Test
    void testGetAllDevices() {
        DeviceDTO dto1 = new DeviceDTO(null, "Sensor de Temperatura", "sensor", "SR123456rsr");
        DeviceDTO dto2 = new DeviceDTO(null, "Sensor de Pressão", "sensor", "fsfdsfds");

        deviceService.createDevice(dto1);
        deviceService.createDevice(dto2);

        List<DeviceDTO> list = deviceService.getAllDevices(PageRequest.of(0, 10));
        assertThat(list).hasSize(2);
    }
}