package tech.erubin.annyeong_eat.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.repository.CafeRepository;
import tech.erubin.annyeong_eat.service.serviceInterface.CafeService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CafeServiceImpl implements CafeService {
    private final CafeRepository repository;

    @Override
    public Set<String> getAllCity() {
        List<Cafe> cafes = repository.findAll();
        return cafes.stream().map(Cafe::getCity).collect(Collectors.toSet());
    }

    @Override
    public Cafe getCafeByName(String name) {
        return repository.findCafeByName(name);
    }

    @Override
    public Cafe getCafeById(Integer id) {
        return repository.findById(id).get();
    }

    @Override
    public List<String> getCafeNameByCity(String city) {
        return repository.findAllByCity(city).stream()
                .map(Cafe::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getNamesByCafeId(List<Integer> cafeId) {
        return cafeId.stream()
                .map(this::getCafeById)
                .map(Cafe::getName)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Cafe cafe) {
        repository.save(cafe);
    }

    @Override
    public void delete(Cafe cafe) {
        repository.delete(cafe);
    }

    @Override
    public Cafe create() {
        return new Cafe();
    }
}
