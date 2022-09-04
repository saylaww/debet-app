package uz.nukuslab.debetapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.nukuslab.debetapp.entity.Company;
import uz.nukuslab.debetapp.payload.ApiResponse;
import uz.nukuslab.debetapp.repository.CompanyRepository;

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
}
