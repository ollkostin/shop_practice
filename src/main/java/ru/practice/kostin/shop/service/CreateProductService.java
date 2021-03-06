package ru.practice.kostin.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.practice.kostin.shop.persistence.entity.ProductEntity;
import ru.practice.kostin.shop.persistence.entity.ProductPhotoEntity;
import ru.practice.kostin.shop.persistence.repository.ProductPhotoRepository;
import ru.practice.kostin.shop.persistence.repository.ProductRepository;
import ru.practice.kostin.shop.service.dto.product.NewProductDTO;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

import static ru.practice.kostin.shop.util.PhotoFileUtil.*;

@Service
@RequiredArgsConstructor
public class CreateProductService {
    private static final int NAME_LENGTH = 255;
    private static final int DESCRIPTION_LENGTH = 1000;
    private final ProductRepository productRepository;
    private final ProductPhotoRepository productPhotoRepository;
    private final FileService fileService;


    /**
     * Saves product
     *
     * @param productDTO product information
     * @return map with errors
     */
    @Transactional
    public HashMap<String, List<String>> createProduct(NewProductDTO productDTO) {
        HashMap<String, List<String>> errors = validateNewProductDTO(productDTO);
        if (errors.isEmpty()) {
            ProductEntity product = new ProductEntity();
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(BigDecimal.valueOf(productDTO.getPrice()));
            product.setRemoved(false);
            productRepository.save(product);
            savePhotos(product, productDTO.getPhotos());
            productDTO.setId(product.getId());
        }
        return errors;
    }

    /**
     * Validates product information
     *
     * @param productDTO product DTO
     * @return map with errors
     */
    private HashMap<String, List<String>> validateNewProductDTO(NewProductDTO productDTO) {
        HashMap<String, List<String>> errors = new HashMap<>();
        if (productDTO.getName().isEmpty() || productDTO.getName().length() > NAME_LENGTH) {
            errors.put("name", Collections.singletonList("Name can not be empty or contain more than " + NAME_LENGTH + " characters"));
        }
        if (productDTO.getDescription().isEmpty() || productDTO.getDescription().length() > DESCRIPTION_LENGTH) {
            errors.put("description", Collections.singletonList("Description can not be empty or contain more than " + DESCRIPTION_LENGTH + " characters"));
        }
        if (productDTO.getPrice() == null || productDTO.getPrice() < 1) {
            errors.put("price", Collections.singletonList("Price can not be empty or less than one"));
        }
        if (productDTO.getPhotos() != null && productDTO.getPhotos().size() > 0) {
            for (MultipartFile photo : productDTO.getPhotos()) {
                List<String> fileErrors = new ArrayList<>();
                String fileName = photo.getOriginalFilename();
                if (!fileHasImageExtension(photo)) {
                    fileErrors.add("File \"" + fileName + "\" has not supported or not image extension " + Arrays.toString(IMAGE_EXTENSIONS));
                }
                if (!isSizeAllowed(photo)) {
                    fileErrors.add("File \"" + fileName + "\" is larger than allowed size : " + ALLOWED_SIZE_BYTE / (1024 * 1024) + " Mb");
                }
                if (!fileErrors.isEmpty()) {
                    errors.put(fileName, fileErrors);
                }
            }
        }
        return errors;
    }

    /**
     * Saves product photos in filesystem
     * and stores paths to them
     *
     * @param product product entity
     * @param photos  image files
     */
    private void savePhotos(ProductEntity product, Collection<MultipartFile> photos) {
        if (photos != null && photos.size() > 0) {
            photos.stream()
                  .map(photo -> fileService.saveFile(photo, product.getId()))
                  .map(path -> getProductPhoto(product, path))
                  .forEach(productPhotoRepository::save);
        }
    }

    private ProductPhotoEntity getProductPhoto(ProductEntity product, String path) {
        ProductPhotoEntity photoEntity = new ProductPhotoEntity();
        photoEntity.setProduct(product);
        photoEntity.setPath(path);
        return photoEntity;
    }
}
