package com.dnanh01.backend.service;

import com.dnanh01.backend.model.Brand;
import com.dnanh01.backend.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }
    
    public Brand createBrand(Brand brand) {
        return brandRepository.save(brand);
    }
    
    public Optional<Brand> getBrandById(Long id) {
        return brandRepository.findById(id);
    }
    
    public void deleteBrandById(Long id) {
        brandRepository.deleteById(id);
    }
    public Brand updateBrand(Brand brand) {
        return brandRepository.save(brand);
    }
}
