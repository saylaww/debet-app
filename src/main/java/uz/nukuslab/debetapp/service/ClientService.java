package uz.nukuslab.debetapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.nukuslab.debetapp.entity.*;
import uz.nukuslab.debetapp.entity.enums.RoleName;
import uz.nukuslab.debetapp.payload.ApiResponse;
import uz.nukuslab.debetapp.payload.ClientDto;
import uz.nukuslab.debetapp.repository.ClientRepository;
import uz.nukuslab.debetapp.repository.CompanyRepository;
import uz.nukuslab.debetapp.repository.ContractRepository;
import uz.nukuslab.debetapp.repository.UserRepository;

import java.util.*;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    UserRepository userRepository;

    public ApiResponse getAll() {
//        List<Client> all = clientRepository.findAll();
        List<Client> all = clientRepository.findAllByOrderByFirstName();
        return new ApiResponse("Clients", true, all);
    }

    public ApiResponse getMyAll(User user) {

//        List<Client> clients = clientRepository.findByCompany_Id(user.getCompany().getId());
//        List<Client> clients = clientRepository.findByCreatedBy(user.getId());
        List<Client> clients = clientRepository.findByCreatedByOrderByFirstName(user.getId());


//            List<Client> clients = clientRepository.findByCompany_Id(user.getCompany().getId());
            return new ApiResponse("My client list", true, clients);
    }

    public ApiResponse add(User user, ClientDto clientDto) {

//        boolean b = clientRepository.existsByPhone(clientDto.getPhone());
        boolean b = clientRepository.existsByPhoneAndCreatedBy(clientDto.getPhone(), user.getId());
        if (b){
            return new ApiResponse("Bunday telefon nomer bazada BAR", false);
        }

        Client client = new Client();

        if(user.getRole().getRoleName().name().equals(RoleName.SUPER_ADMIN.name())){
            Optional<Company> byId = companyRepository.findById(clientDto.getCompanyId());
            if (!byId.isPresent()){
                return new ApiResponse("Bunday company id bazada tabilmadi!!!", false);
            }
            Company company = byId.get();
            client.setCompany(company);

            client.setFirstName(clientDto.getFirstName());
            client.setLastName(clientDto.getLastName());
            client.setPhone(clientDto.getPhone());
//            client.setCompany(user.getCompany());

            clientRepository.save(client);
        }else {
            client.setFirstName(clientDto.getFirstName());
            client.setLastName(clientDto.getLastName());
            client.setPhone(clientDto.getPhone());
            client.setCompany(user.getCompany());

            clientRepository.save(client);
        }

        return new ApiResponse("Client saqlandi", true);
    }

    public ApiResponse getClientsByUserId(Long id) {
        Set<Client> clients = new HashSet<>();
        List<Contract> byWorkerId = contractRepository.findByWorkerId(id);
        for (Contract contract : byWorkerId) {
            clients.add(contract.getClient());
        }

//        Optional<User> byId = userRepository.findById(id);
//        if (!byId.isPresent()){
//            return new ApiResponse("Bunday id li user bazada tabilmadi!!", false);
//        }
//        User user = byId.get();
//
//        List<Client> clients = clientRepository.findByCompany_Id(user.getCompany().getId());
        return new ApiResponse("My client list", true, clients);

    }
}
