package com.citadel.productservice.service;

import com.citadel.productservice.DTO.ProductRequestDTO;
import com.citadel.productservice.DTO.ProductResponseDTO;
import com.citadel.productservice.DTO.UploadImageRequestDTO;
import com.citadel.productservice.DTO.UploadImageResponseDTO;
import com.citadel.productservice.Repo.CategoryRepo;
import com.citadel.productservice.Repo.ProductRepo;
import com.citadel.productservice.config.AWSS3BucketConfig;
import com.citadel.productservice.exception.ResourceNotFoundException;
import com.citadel.productservice.model.Category;
import com.citadel.productservice.model.Product;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.ArrayList;
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


    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
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

    @Transactional
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    public ProductResponseDTO createProduct(ProductRequestDTO request,String creatorEmail){
        Product newProduct = new Product();
        newProduct.setName(request.getName());
        newProduct.setDescription(request.getDescription());
        newProduct.setPrice(request.getPrice());
        newProduct.setSku(request.getSku());
        newProduct.setCreatorEmail(creatorEmail);

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
        return convertToDTO(savedProduct);
    }

    public Page<ProductResponseDTO> getAllProducts(int page, int size, String [] sort,String searchText){
        Pageable pageable = null;
        if (sort.length!=0){
            Sort sortObj = parseSortParams(sort);
            pageable = PageRequest.of(page, size, sortObj);
        }
        else{
            pageable = PageRequest.of(page, size);
        }
        Page<Product> productPage = null;
        if(searchText !=null && !searchText.trim().isEmpty()){
            String processedText = searchText.trim().replace(" ", " & ");
            productPage = productRepo.searchByText(processedText,pageable);
        }
        else {
            productPage = productRepo.findAll(pageable);
        }
        return productPage.map(this::convertToDTO);

    }

    public ProductResponseDTO getProduct(UUID productId){
        Product product = productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product not found with id: " + productId));
        return  convertToDTO(product);
    }

    public ProductResponseDTO getProductBySku(String sku){
        List<Product> items = productRepo.findBySku(sku);
        if(items.isEmpty()) {
            throw  new ResourceNotFoundException("Product not found with sku "+sku);
        }
        if(items.size()>1){
            throw  new RuntimeException("Multiple products found for sku "+sku);
        }
        return convertToDTO(items.get(0));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @productSecurity.isOwner(authentication, #productId)")
    public ProductResponseDTO updateProduct(UUID productId, ProductRequestDTO requestDTO) {
        Product existingProduct = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if(requestDTO.getName()!=null) existingProduct.setName(requestDTO.getName());
        if(requestDTO.getDescription()!=null)existingProduct.setDescription(requestDTO.getDescription());
        if(requestDTO.getPrice()!=null) existingProduct.setPrice(requestDTO.getPrice());
        Product savedProduct = productRepo.save(existingProduct);
        return convertToDTO(savedProduct);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @productSecurity.isOwner(authentication, #productId)")
    public void deleteProduct(UUID productId) {
        if(!productRepo.existsById(productId)){
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        productRepo.deleteById(productId);
    }


    private ProductResponseDTO convertToDTO(Product product){
        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setId(product.getId());
        responseDTO.setName(product.getName());
        responseDTO.setDescription(product.getDescription());
        responseDTO.setCategoryName(product.getCategory().getName());
        responseDTO.setPrice(product.getPrice());
        responseDTO.setImageUrls(product.getImageUrls());
        responseDTO.setSku(product.getSku());
        responseDTO.setCreatorEmail(product.getCreatorEmail());
        responseDTO.setActive(product.isActive());
        return  responseDTO;
    }

    private Sort parseSortParams(String[] sortParams) {
        List<Sort.Order> orders = new ArrayList<>();

        for (String param : sortParams) {
            String[] parts = param.split(",");
            String field = parts[0];
            Sort.Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            orders.add(new Sort.Order(direction, field));
        }

        return Sort.by(orders);
    }


}
