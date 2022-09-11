package uz.nukuslab.debetapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.nukuslab.debetapp.entity.Client;
import uz.nukuslab.debetapp.entity.Company;
import uz.nukuslab.debetapp.entity.Contract;
import uz.nukuslab.debetapp.entity.User;
import uz.nukuslab.debetapp.entity.enums.RoleName;
import uz.nukuslab.debetapp.payload.ApiResponse;
import uz.nukuslab.debetapp.payload.ClientDto;
import uz.nukuslab.debetapp.repository.ClientRepository;
import uz.nukuslab.debetapp.repository.CompanyRepository;
import uz.nukuslab.debetapp.repository.ContractRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ContractRepository contractRepository;

    public ApiResponse getAll() {
        List<Client> all = clientRepository.findAll();
        return new ApiResponse("Clients", true, all);
    }

    public ApiResponse getMyAll(User user) {
        if (user.getRole().getRoleName().name().equals("USER") ||
                user.getRole().getRoleName().name().equals("ADMIN")) {

            List<Client> clientList = new ArrayList<>();

            List<Contract> byWorker = contractRepository.findByWorkerId(user.getId());
            for (Contract contract : byWorker) {
                clientList.add(contract.getClient());
            }


//            List<Client> clients = clientRepository.findByCompany_Id(user.getCompany().getId());
            return new ApiResponse("My client list", true, clientList);
        }
        return new ApiResponse("",false);
    }

    public ApiResponse add(User user, ClientDto clientDto) {

        boolean b = clientRepository.existsByPhone(clientDto.getPhone());
        if (b){
            return new ApiResponse("Bunday telefon nomer bazada BAR", false);
        }

        Client client = new Client();

        if(user.getRole().getRoleName().name().equals(RoleName.SUPER_ADMIN)){
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
}
