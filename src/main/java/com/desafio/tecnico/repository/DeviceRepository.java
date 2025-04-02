package com.desafio.tecnico.repository;

import com.desafio.tecnico.entity.Device;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {

    List<Device> findByRemovedFalse(Pageable pageable);
}
