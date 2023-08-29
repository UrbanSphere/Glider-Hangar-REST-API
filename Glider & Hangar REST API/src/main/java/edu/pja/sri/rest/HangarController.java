package edu.pja.sri.rest;

import edu.pja.sri.dto.GliderDto;
import edu.pja.sri.dto.HangarDetailsDto;
import edu.pja.sri.dto.HangarDto;
import edu.pja.sri.model.Glider;
import edu.pja.sri.model.Hangar;
import edu.pja.sri.repo.GliderRepository;
import edu.pja.sri.repo.HangarRepository;
import edu.pja.sri.dto.mapper.HangarDtoMapper;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.Link;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hangars")
public class HangarController {

    private HangarRepository hangarRepository;
    private GliderRepository gliderRepository;
    private ModelMapper modelMapper;
    private HangarDtoMapper hangarDtoMapper;
    private HangarDetailsDto hangarDetailsDto;

    public HangarController(HangarRepository hangarRepository, GliderRepository gliderRepository,
                            ModelMapper modelMapper, HangarDtoMapper hangarDtoMapper,
                            HangarDetailsDto hangarDetailsDto) {
        this.hangarRepository = hangarRepository;
        this.gliderRepository = gliderRepository;
        this.modelMapper = modelMapper;
        this.hangarDtoMapper = hangarDtoMapper;
        this.hangarDetailsDto = hangarDetailsDto;
    }
    private GliderDto convertToDto(Glider e) {return modelMapper.map(e, GliderDto.class);}
    private Link createHangarLink(Long hangarId){
        Link linkSelf = linkTo(methodOn(HangarController.class).getHangarById(hangarId)).withSelfRel();
        return linkSelf;
    }
    private Link createHangarGlidersLink(Long hangarId){
        Link linkSelf = linkTo(methodOn(HangarController.class).getGlidersByHangarId(hangarId)).withSelfRel();
        return linkSelf;
    }

    @GetMapping
    public ResponseEntity<Collection<HangarDto>> getHangars() {
        List<Hangar> allHangars = hangarRepository.findAll();
        List<HangarDto> result = allHangars.stream().map(hangarDtoMapper::convertToDto).collect(Collectors.toList());
        for(HangarDto dto: result){
            dto.add(createHangarLink(dto.getId()));
            dto.add(createHangarGlidersLink(dto.getId()));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
   @GetMapping("/{hangarId}")
    public ResponseEntity<HangarDetailsDto> getHangarById(@PathVariable Long hangarId) {
        Optional<Hangar> hangar = hangarRepository.findById(hangarId);
        if(hangar.isPresent()) {
            HangarDetailsDto hangarDetailsDto = hangarDtoMapper.convertToDtoDetails(hangar.get());
            hangarDetailsDto.add(createHangarLink(hangarId));
            hangarDetailsDto.add(createHangarGlidersLink(hangarId));
            return new ResponseEntity<>(hangarDetailsDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping
    public ResponseEntity saveNewHangar(@Valid @RequestBody HangarDto hangar) {
        Hangar entity = hangarDtoMapper.convertToEntity(hangar);
        hangarRepository.save(entity);
        HttpHeaders headers = new HttpHeaders();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entity.getId()).toUri();
        headers.add("Location", location.toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }
    @PutMapping("/{hangarId}")
    public ResponseEntity updateHangar(@Valid @PathVariable Long hangarId, @RequestBody HangarDto hangarDto) {
        Optional<Hangar> currentHangar = hangarRepository.findById(hangarId);
        if(currentHangar.isPresent()) {
            hangarDto.setId(hangarId);
            Hangar entity = hangarDtoMapper.convertToEntity(hangarDto);
            hangarRepository.save(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{hangarId}")
    public ResponseEntity deleteHangar(@PathVariable Long hangarId) {
        Optional<Hangar> optionalHangar = hangarRepository.findById(hangarId);
        if (optionalHangar.isPresent()){
            Hangar hangar = optionalHangar.get();
            for (Glider glider: hangar.getGliders()){
                glider.setHangar(null);
                gliderRepository.save(glider);
            }
            hangarRepository.deleteById(hangarId);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{hangarId}/gliders/{gliderId}")
    public ResponseEntity attachGliderToHangar(@PathVariable Long hangarId, @PathVariable Long gliderId) {
        Optional<Glider> optionalGlider = gliderRepository.findById(gliderId);
        Optional<Hangar> optionalHangar = hangarRepository.findById(hangarId);
        if (optionalGlider.isPresent() && optionalHangar.isPresent()){
            Glider glider = optionalGlider.get();
            Hangar hangar = optionalHangar.get();
            glider.setHangar(hangar);
            gliderRepository.save(glider);
            hangar.getGliders().add(glider);
            hangarRepository.save(hangar);
            return new ResponseEntity(null, HttpStatus.CREATED);
        } else {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{hangarId}/gliders/{gliderId}")
    public ResponseEntity detachGliderFromHangar(@PathVariable Long hangarId, @PathVariable Long gliderId) {
        Optional<Glider> optionalGlider = gliderRepository.findById(gliderId);
        Optional<Hangar> optionalHangar = hangarRepository.findById(hangarId);
        if (optionalGlider.isPresent() && optionalHangar.isPresent()){
            Glider glider = optionalGlider.get();
            Hangar hangar = optionalHangar.get();
            if (glider.getHangar() == hangar){
                glider.setHangar(null);
                gliderRepository.save(glider);
                hangar.getGliders().remove(glider);
                hangarRepository.save(hangar);
            }
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{hangarId}/gliders")
    public ResponseEntity<Collection<GliderDto>> getGlidersByHangarId(@PathVariable Long hangarId) {
        List<Glider> gliders = gliderRepository.findGlidersByHangarId(hangarId);
        List<GliderDto> result = gliders.stream().map(this::convertToDto).collect(Collectors.toList());
        for(GliderDto dto: result){
            dto.add(createHangarLink(hangarId));
            dto.add(createHangarGlidersLink(dto.getId()));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
