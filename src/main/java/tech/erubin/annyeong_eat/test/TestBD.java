package tech.erubin.annyeong_eat.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import tech.erubin.annyeong_eat.dateBase.entity.Cheque;
import tech.erubin.annyeong_eat.dateBase.entity.Client;
import tech.erubin.annyeong_eat.dateBase.entity.Dish;
import tech.erubin.annyeong_eat.dateBase.entity.Order;
import tech.erubin.annyeong_eat.dateBase.service.Servis;

@Controller
@RequestMapping("/test")
public class TestBD {

    @Autowired
    private Servis servis;


    @RequestMapping("/cheque")
    public void createCheque(){
        Dish dish = servis.createDish("корн дог сырный", "закуски", 120.00);
        Client client1 = servis.createClient("Иван","Магомедов");
        Order order = servis.createOrder(client1, "тестовый заказ 3", "г. Махачкала, улица 222", "", "наличные");
        Cheque cheque = servis.createCheque(order, dish, 1);
        servis.saveCheque(cheque);
    }

//        Dish dish1 = null;
//
//        Client client = servis.createClient("Магомедамин","Магомедов");
//        Client client1 = servis.createClient("Маржана","Магомедова");
//        Client client2 = servis.createClient("Виталий","Ким");
//
//        Employee employee = servis.createEmployee("Виталий", "Ким", "Создатель");
//        Employee employee1 = servis.createEmployee("Егор", "", "Официант");
//        Employee employee2 = servis.createEmployee("Магомед", "Магомедов", "Доставщик");
//        Employee employee3 = servis.createEmployee("Валера", "Ким", "Повар");
//
//        servis.save(dish);



}
