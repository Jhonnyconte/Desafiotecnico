package com.desafio.tecnico.mapper;

import com.desafio.tecnico.dto.DeviceDTO;
import com.desafio.tecnico.entity.Device;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    DeviceDTO toDTO(Device device);
    Device toEntity(DeviceDTO deviceDTO);
}
