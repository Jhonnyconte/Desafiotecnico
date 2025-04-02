package com.desafio.tecnico.dto;

import java.util.UUID;

public record DeviceDTO(UUID id, String name, String type, String serialNumber) {

}

