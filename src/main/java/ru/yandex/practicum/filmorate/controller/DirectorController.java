package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.DirectorDTO;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

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
