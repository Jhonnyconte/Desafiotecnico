package com.desafio.tecnico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record DeviceDTO(UUID id,
                        @NotBlank(message = "O nome é obrigatório")
                        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
                        String name,
                        @NotBlank(message = "O tipo é obrigatório")
                        @Size(max = 50, message = "O tipo deve ter no máximo 50 caracteres")
                        String type,
                        @NotBlank(message = "O número de série é obrigatório")
                        @Size(max = 50, message = "O número de série deve ter no máximo 50 caracteres")
                        String serialNumber) {

}

