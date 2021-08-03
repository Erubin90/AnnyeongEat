package tech.erubin.annyeong_eat.telegramBot.service.entityServises;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.Cafe;
import tech.erubin.annyeong_eat.telegramBot.repository.CafeRepository;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.serviceInterface.CafeService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CafeServiceImpl implements CafeService {
    private final CafeRepository repository;

    @Override
    public List<Cafe> getAllCafe() {
        return repository.findAll();
    }

    @Override
    public Set<String> getAllCity() {
        List<Cafe> cafes = repository.findAll();
        return cafes.stream().map(Cafe::getCity).collect(Collectors.toSet());
    }

    @Override
    public Cafe getCafeById(int id) {
        return repository.getById(id);
    }

    @Override
    public Cafe getCafeByName(String name) {
        return repository.findCafeByName(name);
    }

    @Override
    public List<String> getAllCafeNames() {
        return repository.findAll().stream()
                .map(Cafe::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getCafeNameByCity(String city) {
        return repository.findAllByCity(city).stream()
                .map(Cafe::getName)
                .collect(Collectors.toList());
    }

    @Override
    public void saveCafe(Cafe cafe) {
        repository.save(cafe);
    }

    @Override
    public void deleteCafe(Cafe cafe) {
        repository.delete(cafe);
    }

    @Override
    public Cafe createCafe() {
        return new Cafe();
    }
}
