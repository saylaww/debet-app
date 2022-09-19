package uz.nukuslab.debetapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.nukuslab.debetapp.entity.Company;
import uz.nukuslab.debetapp.entity.User;
import uz.nukuslab.debetapp.payload.ApiResponse;
import uz.nukuslab.debetapp.repository.CompanyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    public ApiResponse blockCompany(Long companyId) {
        Optional<Company> byId = companyRepository.findById(companyId);
        if (!byId.isPresent()){
            return new ApiResponse("Bunday id li company tabilmadi!!!", false);
        }
        Company company = byId.get();
        company.setActive(false);
        companyRepository.save(company);
        return new ApiResponse("Company bloklandi!!!", true);
    }


    public ApiResponse unBlockCompany(Long companyId) {
        Optional<Company> byId = companyRepository.findById(companyId);
        if (!byId.isPresent()){
            return new ApiResponse("Bunday id li company tabilmadi!!!", false);
        }
        Company company = byId.get();
        company.setActive(true);
        companyRepository.save(company);
        return new ApiResponse("Company blokltan SHIG'ARILDI!!!", true);
    }

    public ApiResponse getAllCompany() {
//        Iterable<Company> all = companyRepository.findAll();
        List<Company> all = companyRepository.findAllByOrderByName();

//        List<Company> companies = new ArrayList<>();
//        for (Company company : all) {
//            companies.add(company);
//        }

        return new ApiResponse("Companylar listi:", true, all);
    }

    public ApiResponse byId(Long id) {
        Optional<Company> byId = companyRepository.findById(id);
        if (!byId.isPresent()){
            return new ApiResponse("Bunday id li kompaniya tabilmadi", false);
        }
        Company company = byId.get();

        return new ApiResponse("Company : ", true, company);
    }
}
