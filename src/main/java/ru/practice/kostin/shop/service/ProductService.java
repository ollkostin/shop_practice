package ru.practice.kostin.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practice.kostin.shop.exception.NotFoundException;
import ru.practice.kostin.shop.persistence.entity.ProductEntity;
import ru.practice.kostin.shop.persistence.repository.CartRepository;
import ru.practice.kostin.shop.persistence.repository.ProductRepository;
import ru.practice.kostin.shop.service.dto.product.ProductFullDTO;
import ru.practice.kostin.shop.service.dto.product.ProductShortDTO;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    /**
     * Returns list of products
     *
     * @return list of products
     */
    @Transactional(readOnly = true)
    public Page<ProductShortDTO> getProducts(Pageable pageable) {
        return productRepository.findAll(this::notRemovedProductPredicate, pageable)
                                .map(ProductShortDTO::new);
    }


    /**
     * Returns list of all products
     *
     * @return list of all products
     */
    @Transactional(readOnly = true)
    public Page<ProductShortDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                                .map(ProductShortDTO::new);
    }


    /**
     * Returns product by id
     *
     * @param productId id of the product
     * @return product
     * @throws NotFoundException no product with specified id
     */
    @Transactional(readOnly = true)
    public ProductFullDTO getProduct(Integer productId) {
        ProductEntity productEntity = productRepository.getOne(productId);
        if (productEntity.getRemoved()) {
            throw new NotFoundException(getNotFoundMessage(productId));
        }
        return new ProductFullDTO(productEntity);
    }

    /**
     * Deletes product by id
     *
     * @param productId product id
     * @throws NotFoundException if product was not found
     */
    @Transactional
    public void deleteProduct(Integer productId) {
        ProductEntity productEntity = productRepository
                .findById(productId)
                .orElseThrow(() -> new NotFoundException(getNotFoundMessage(productId)));
        productEntity.setRemoved(true);
        productRepository.save(productEntity);
        cartRepository.deleteRemovedFromCart(productId);
    }

    private String getNotFoundMessage(Integer productId) {
        return String.format("Product with id:%d does not exist", productId);
    }

    private Predicate notRemovedProductPredicate(Root<ProductEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get("removed"), false);
    }

    @Transactional
    public void restoreProduct(Integer productId) {
        ProductEntity productEntity = productRepository
                .findById(productId)
                .orElseThrow(() -> new NotFoundException(getNotFoundMessage(productId)));
        productEntity.setRemoved(false);
        productRepository.save(productEntity);

    }
}
