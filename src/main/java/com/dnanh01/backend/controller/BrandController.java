package com.dnanh01.backend.controller;

import com.dnanh01.backend.exception.BrandException;
import com.dnanh01.backend.exception.UserException;
import com.dnanh01.backend.model.Brand;
import com.dnanh01.backend.model.User;
import com.dnanh01.backend.service.BrandService;
import com.dnanh01.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class BrandController {
	@Autowired
	private BrandService brandService;

	@Autowired
	private UserService userService;

	@GetMapping("/api/brand/")
	public List<Brand> getAllBrands() {
		return brandService.getAllBrands();
	}

	@PostMapping("/api/admin/brand/create")
	public ResponseEntity<Brand> createBrand(@RequestBody Brand brand, @RequestHeader("Authorization") String jwt)
			throws UserException, BrandException {
		User user = userService.findUserProfileByJwt(jwt);
		Brand createdBrand = brandService.createBrand(brand);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdBrand);
	}

	@DeleteMapping("/api/admin/brand/delete/{id}")
	public ResponseEntity<Void> deleteBrand(@PathVariable Long id, @RequestHeader("Authorization") String jwt)
			throws UserException, BrandException {

		Optional<Brand> brandOptional = brandService.getBrandById(id);
		if (!brandOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		brandService.deleteBrandById(id);
		return ResponseEntity.noContent().build();
	}
	
	
	 @PutMapping("/api/admin/brand/update/{id}")
	    public ResponseEntity<Brand> updateBrand(@PathVariable Long id, @RequestBody Brand updatedBrand,
	            @RequestHeader("Authorization") String jwt) throws UserException, BrandException {
	        // Kiểm tra xem brand có tồn tại không
	        Optional<Brand> brandOptional = brandService.getBrandById(id);
	        if (!brandOptional.isPresent()) {
	            return ResponseEntity.notFound().build();
	        }

	        // Lấy thông tin user từ token JWT
	        User user = userService.findUserProfileByJwt(jwt);

	        // Cập nhật thông tin của brand
	        Brand existingBrand = brandOptional.get();
	        existingBrand.setName(updatedBrand.getName());
	        existingBrand.setImageUrl(updatedBrand.getImageUrl());

	        // Lưu lại thông tin brand đã cập nhật
	        Brand updated = brandService.updateBrand(existingBrand);

	        return ResponseEntity.ok(updated);
	    }
}
