package edu.pja.sri.rest;

import edu.pja.sri.model.Glider;
import edu.pja.sri.dto.GliderDto;
import edu.pja.sri.repo.GliderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
public class tes {

    private GliderRepository gliderRepository;
    private ModelMapper modelMapper;
    private GliderDto convertToDto(Glider e) {
        return modelMapper.map(e, GliderDto.class);
    }
    private Glider convertToEntity(GliderDto dto) {
        return modelMapper.map(dto, Glider.class);
    }
    @GetMapping
    public ResponseEntity<Collection<GliderDto>> getGliders(){
        List<Glider> gliders = gliderRepository.findAll();
        if (gliders != null){
            List<GliderDto> result = gliders.stream().map(this::convertToDto).collect(Collectors.toList());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<GliderDto> getById(@PathVariable Long id){
        Optional<Glider> glider = gliderRepository.findById(id);
        if (glider.isPresent()){
            GliderDto result = convertToDto(glider.get());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping
    public ResponseEntity postGlider(@RequestBody GliderDto glider){
        Glider entity = convertToEntity(glider);
        gliderRepository.save(entity);
        HttpHeaders headers = new HttpHeaders();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entity.getId()).toUri();
        headers.add("Location", location.toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }
    @PutMapping
    public ResponseEntity updateGlider(@PathVariable Long id, @RequestBody GliderDto gliderDto){
        Optional<Glider> savedGlider = gliderRepository.findById(id);
        Glider entity = convertToEntity(gliderDto);
        if (savedGlider.isPresent()){
            entity.setId(id);
            gliderRepository.save(entity);
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
        } else {
            gliderRepository.save(entity);
            return new ResponseEntity(null, HttpStatus.CREATED);
        }
    }
    @DeleteMapping
    public ResponseEntity deleteGlider(@PathVariable Long id){
        Optional<Glider> glider = gliderRepository.findById(id);
        if (glider.isPresent()){
            gliderRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }


}