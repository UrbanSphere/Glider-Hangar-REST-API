package edu.pja.sri.dto.mapper;

import edu.pja.sri.dto.HangarDetailsDto;
import edu.pja.sri.dto.HangarDto;
import edu.pja.sri.model.Hangar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;


@Component
@RequiredArgsConstructor
public class HangarDtoMapper {
    private final ModelMapper modelMapper;
    public HangarDto convertToDto(Hangar e) {
        return modelMapper.map(e, HangarDto.class);
    }
    public HangarDetailsDto convertToDtoDetails(Hangar e) {
        return modelMapper.map(e, HangarDetailsDto.class);
    }
    public Hangar convertToEntity(HangarDto dto) {
        return modelMapper.map(dto, Hangar.class);
    }
}
