package com.citadel.productservice.service;

import com.citadel.productservice.DTO.ProductRequestDTO;
import com.citadel.productservice.DTO.ProductResponseDTO;
import com.citadel.productservice.DTO.UploadImageRequestDTO;
import com.citadel.productservice.DTO.UploadImageResponseDTO;
import com.citadel.productservice.Repo.CategoryRepo;
import com.citadel.productservice.Repo.ProductRepo;
import com.citadel.productservice.config.AWSS3BucketConfig;
import com.citadel.productservice.model.Category;
import com.citadel.productservice.model.Product;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private AWSS3BucketConfig awss3BucketConfig;

    @Autowired
    private S3Presigner s3Presigner;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductRepo productRepo;


    public UploadImageResponseDTO generatePresignedUrlForUpload(UploadImageRequestDTO request){

        String imageKey = UUID.randomUUID().toString() + "-" + request.getFileName();

        PutObjectRequest objectRequest = PutObjectRequest.builder().
                bucket(awss3BucketConfig.getBucketName()).key(imageKey).contentType(request.getContentType()).build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)).
                putObjectRequest(objectRequest).build();

        String signedURL = s3Presigner.presignPutObject(presignRequest).url().toString();

        return new UploadImageResponseDTO(signedURL,imageKey);
    }

    public Product createProduct(ProductRequestDTO request){
        Product newProduct = new Product();
        newProduct.setName(request.getName());
        newProduct.setDescription(request.getDescription());
        newProduct.setPrice(request.getPrice());
        newProduct.setSku(request.getSku());

        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        newProduct.setCategory(category);

        if (request.getImageKeys() != null) {
            List<String> permanentUrls = request.getImageKeys().stream()
                    .map(key -> "https://"+awss3BucketConfig.getBucketName()+".s3."+awss3BucketConfig.getRegion()+".amazonaws.com/"+ key)
                    .collect(Collectors.toList());
            newProduct.setImageUrls(permanentUrls);
        }

        Product savedProduct = productRepo.save(newProduct);
        return savedProduct;
    }

    public List<ProductResponseDTO> getAllProducts(){
        List<Product> productsList = productRepo.findAll();
        return productsList.stream().map(this::convertToDTO).collect(Collectors.toList());

    }

    public ProductResponseDTO convertToDTO(Product product){
        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setId(product.getId());
        responseDTO.setName(product.getName());
        responseDTO.setDescription(product.getDescription());
        responseDTO.setCategoryName(product.getCategory().getName());
        responseDTO.setPrice(product.getPrice());
        responseDTO.setImageUrls(product.getImageUrls());
        responseDTO.setSku(product.getSku());
        responseDTO.setActive(product.isActive());
        return  responseDTO;
    }
}
