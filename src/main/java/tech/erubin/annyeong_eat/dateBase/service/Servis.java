package tech.erubin.annyeong_eat.dateBase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.dateBase.entity.*;
import tech.erubin.annyeong_eat.dateBase.repository.*;

@Service
public class Servis{

    @Autowired
    private ChequeRepository chequeRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private DishOptionallyRepository dishOptionallyRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

      public void saveClient(Client client){
        clientRepository.save(client);
    }

    public void saveDish(Dish dish){
        dishRepository.save(dish);
    }

    public void saveDishOptionally(DishOptionally dishOptionally){
        dishOptionallyRepository.save(dishOptionally);
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public void saveCheque(Cheque cheque) {
        chequeRepository.save(cheque);
    }

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public Client getClient(Integer id){
        return clientRepository.findById(id).get();
    }

    public Employee getEmployee(Integer id) {
        return employeeRepository.findById(id).get();
    }

    public Dish getDish(Integer id) {
        return dishRepository.getById(id);
    }

    public DishOptionally getDishOptionally(Integer id) {
        return dishOptionallyRepository.getById(id);
    }

    public Order getOrder(Integer id){
          return orderRepository.getById(id);
    }

    public Cheque getCheque(Integer id) {
          return chequeRepository.getById(id);
    }

    public Client createClient(String name, String surname) {
        return new Client(name, surname, null, null);
    }

    public Employee createEmployee(String name, String surname, String role) {
        return new Employee(name, surname, role);
    }

    public Dish createDish(String name, String type, Double price) {
        return new Dish(name, type, price);
    }

    public DishOptionally createDishOptionally(String name, String type, Double price) {
        return new DishOptionally(name, type, price);
    }

    public Order createOrder(Client client, String orderName, String address, String comment, String paymentMethod) {
        return new Order(client, orderName, address, comment, paymentMethod);
    }

    public Cheque createCheque(Order order, Dish dish, Integer countDishes) {
        return new Cheque(order, dish, countDishes);
    }
}
