package ru.yandex.practicum.filmorate.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.DirectorDTO;
import ru.yandex.practicum.filmorate.service.DirectorService;

@RestController
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public List<DirectorDTO> findAll() {
        return directorService.getAll();
    }

    @GetMapping("/{id}")
    public DirectorDTO findById(@PathVariable Long id) {
        return directorService.findById(id);
    }

    @PostMapping
    public DirectorDTO save(@RequestBody DirectorDTO directorDTO) {
        return directorService.create(directorDTO);
    }

    @PutMapping
    public DirectorDTO update(@RequestBody DirectorDTO directorDTO) {
        return directorService.update(directorDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        directorService.delete(id);
    }

}
