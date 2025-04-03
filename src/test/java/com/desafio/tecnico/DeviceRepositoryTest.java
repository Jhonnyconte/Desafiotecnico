package com.desafio.tecnico;
import com.desafio.tecnico.entity.Device;
import com.desafio.tecnico.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeviceRepositoryTest {

    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    void deveSalvarERecuperarDispositivo() {
        Device device = new Device("Sensor de Temperatura", "sensor", "SR123456");
        Device savedDevice = deviceRepository.save(device);

        Optional<Device> foundDevice = deviceRepository.findById(savedDevice.getId());

        assertThat(foundDevice).isPresent();
        assertThat(foundDevice.get().getName()).isEqualTo("Sensor de Temperatura");
    }
    @Test
    void deveSalvarERecuperarDispositivoRemovedFalse() {
        Device device = new Device("Sensor de Temperatura", "sensor", "SR123456");
        deviceRepository.save(device);
        Pageable page = PageRequest.of(0, 10);
        List<Device> foundDevice = deviceRepository.findByRemovedFalse(page);

        assertThat(foundDevice).hasSize(1);
        assertThat(foundDevice.get(0).getSerialNumber()).isEqualTo("SR123456");
    }

    @Test
    void deveBuscarDispositivosPorStatus() {
        deviceRepository.save(new Device("Sensor 1", "Sensor", "SR987654"));
        deviceRepository.save(new Device("Sensor 2", "Sensor", "SR369852"));

        List<Device> activeDevices = deviceRepository.findAll();

        assertThat(activeDevices).hasSize(2);
        assertThat(activeDevices.get(0).getName()).isEqualTo("Sensor 1");
        assertThat(activeDevices.get(1).getName()).isEqualTo("Sensor 2");
    }
}