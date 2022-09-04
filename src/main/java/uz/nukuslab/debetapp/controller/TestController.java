package uz.nukuslab.debetapp.controller;

import org.springframework.web.bind.annotation.*;
import uz.nukuslab.debetapp.entity.*;
import uz.nukuslab.debetapp.payload.Data;
import uz.nukuslab.debetapp.payload.ResponseData;
import uz.nukuslab.debetapp.repository.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class TestController {

    private final
    UserRepository userRepository;
    private final
    ProductRepository productRepository;
    private final
    ClientRepository clientRepository;
    private final DebetRepository debetRepository;
    private final ContractRepository contractRepository;


    public TestController(UserRepository userRepository, ProductRepository productRepository, ClientRepository clientRepository, DebetRepository debetRepository, ContractRepository contractRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.debetRepository = debetRepository;
        this.contractRepository = contractRepository;
    }


    @GetMapping("/test")
    public String test(){
        return "TEST VALUE11";
    }
//
//    @GetMapping("/")
//    public String index(){
//        Optional<User> byId = userRepository.findById(1);
//        User user = byId.get();
//
//        Optional<Product> byProduct = productRepository.findById(1);
//        Product product = byProduct.get();
//
//        Optional<Client> byClient = clientRepository.findById(1);
//        Client client = byClient.get();
//
//        int part = 5;
//        double price = 1500000;
//
//        Contract contract = new Contract(
//                product,
//                user,
//                price,
//                client,
//                6,
//                part
//        );
//
//        contractRepository.save(contract);
//
//        List<Debet> debetList = new ArrayList<>();
//        for (int i = 0; i < part; i++) {
//            debetList.add(new Debet(
//                    "month",
////                    product.getModel(),
//                    price / 100 * 6,
//                    contract
////                    true
//            ));
//        }
//
//        debetRepository.saveAll(debetList);
//
//        return "HOME page";
//    }
//
//    @GetMapping("/date")
//    public String month(){
//        Date date = new Date(System.currentTimeMillis());
//        date.setMonth(date.getMonth() + 4);
//        int month1 = date.getMonth();
//        return String.valueOf(date.getTime());
//    }
//
//    @GetMapping("/findUserByCompany/{id}")
//    public List<User> users(@PathVariable Integer id){
//        List<User> users = userRepository.findByCompanyId(id);
//        return users;
//    }
//
//    @GetMapping("/calc")
//    public Object calc(@RequestBody(required = false) Data data){
//        ResponseData resData = new ResponseData(
//                data.getPart(),
//                data.getSumma() / 100 * 6 * data.getPart(),
//                data.getSumma() / 100 * 6 + data.getSumma() / data.getPart(),
//                data.getSumma() + data.getSumma() / 100 * 6 * data.getPart(),
//                data.getSumma() / 100 * 6
//        );
//
////        double san = data.getSumma() / 100 * 6;
//        return resData;
//    }

}
