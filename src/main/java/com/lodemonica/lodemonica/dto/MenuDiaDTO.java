package com.lodemonica.lodemonica.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class MenuDiaDTO {
    private Integer id;
    private LocalDate fecha;
    private Integer semanaId;
    private List<PlatoDTO> platos;
}