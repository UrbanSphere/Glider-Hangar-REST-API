package edu.pja.sri.rest;

import edu.pja.sri.model.Glider;
import edu.pja.sri.dto.GliderDto;
import edu.pja.sri.repo.GliderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/gliders")
public class GliderController {
    private GliderRepository gliderRepository;
    private ModelMapper modelMapper;
    public GliderController(GliderRepository gliderRepository, ModelMapper modelMapper) {
        this.gliderRepository = gliderRepository;
        this.modelMapper = modelMapper;
    }
    private GliderDto convertToDto(Glider e) {
        return modelMapper.map(e, GliderDto.class);
    }
    private Glider convertToEntity(GliderDto dto) {
        return modelMapper.map(dto, Glider.class);
    }
    private Link createGliderLink(Long gliderId){
        Link linkSelf = linkTo(methodOn(GliderController.class).getGliderById(gliderId)).withSelfRel();
        return linkSelf;
    }
    @GetMapping
    public ResponseEntity<Collection<GliderDto>> getGliders() {
        List<Glider> allGliders = gliderRepository.findAll();
        List<GliderDto> result = allGliders.stream().map(this::convertToDto).collect(Collectors.toList());
        for (GliderDto dto: result){
            dto.add(createGliderLink(dto.getId()));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/{gliderId}")
    public ResponseEntity<GliderDto> getGliderById(@PathVariable Long gliderId) {
        Optional<Glider> glider = gliderRepository.findById(gliderId);
        if(glider.isPresent()) {
            GliderDto gliderDto = convertToDto(glider.get());
            gliderDto.add(createGliderLink(gliderId));
            return new ResponseEntity<>(gliderDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping
    public ResponseEntity saveNewGlider(@Valid @RequestBody GliderDto emp) {
        Glider entity = convertToEntity(emp);
        gliderRepository.save(entity);
        HttpHeaders headers = new HttpHeaders();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entity.getId()).toUri();
        headers.add("Location", location.toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }
    @PutMapping("/{empId}")
    public ResponseEntity updateGlider(@Valid @PathVariable Long empId, @RequestBody GliderDto gliderDto) {
        Optional<Glider> currentEmp = gliderRepository.findById(empId);
        if(currentEmp.isPresent()) {
            gliderDto.setId(empId);
            Glider entity = convertToEntity(gliderDto);
            gliderRepository.save(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{empId}")
    public ResponseEntity deleteGlider(@PathVariable Long empId)
    {
        gliderRepository.deleteById(empId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
