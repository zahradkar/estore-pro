package eu.martin.store.products;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository repo;
    @Autowired
    private CategoryRepository catRepo;


    /**
     * Method under test: {@link ProductRepository#findAll(Specification, Pageable)}
     */
    @DisplayName("findAll by specific name")
    @Test
    void findAll1() {
        prepareProducts();
        var specification = new ArrayList<Specification<Product>>(1);
        specification.add(ProductSpecifications.hasName("duct"));
        var pageNumber = 0;
        var pageSize = 5;

        var result = repo.findAll(Specification.allOf(specification), PageRequest.of(pageNumber, pageSize, Sort.unsorted()));

        assertEquals(1, result.getTotalElements());
        assertEquals("product name", result.getContent().getFirst().getName());
    }

    @DisplayName("findAll by price (product's price is grater than or equal to given one)")
    @Test
    void findAll2() {
        prepareProducts();
        var specification = new ArrayList<Specification<Product>>(1);
        specification.add(ProductSpecifications.hasPriceGreaterThanOrEqualTo(BigDecimal.valueOf(32.5)));
        var pageNumber = 0;
        var pageSize = 5;

        var result = repo.findAll(Specification.allOf(specification), PageRequest.of(pageNumber, pageSize, Sort.unsorted()));

        assertEquals(1, result.getTotalElements());
        assertEquals(BigDecimal.valueOf(45.32), result.getContent().getFirst().getSellPrice());
    }

    @DisplayName("findAll by price (product's price is less than or equal to given one)")
    @Test
    void findAll3() {
        prepareProducts();
        var specification = new ArrayList<Specification<Product>>(1);
        specification.add(ProductSpecifications.hasPriceLessThanOrEqualTo(BigDecimal.valueOf(32.5)));
        var pageNumber = 0;
        var pageSize = 5;

        var result = repo.findAll(Specification.allOf(specification), PageRequest.of(pageNumber, pageSize, Sort.unsorted()));

        assertEquals(1, result.getTotalElements());
        assertEquals(BigDecimal.valueOf(12.95), result.getContent().getFirst().getSellPrice());
    }

    @DisplayName("findAll by specific attribute name")
    @Test
    void findAll4() {
        prepareProductsWithAttribute();

        var specification = new ArrayList<Specification<Product>>(1);
        specification.add(ProductSpecifications.hasAttribute("serial"));
        var pageNumber = 0;
        var pageSize = 5;

        var result = repo.findAll(Specification.allOf(specification), PageRequest.of(pageNumber, pageSize, Sort.unsorted()));

        assertEquals(1, result.getTotalElements());
        assertEquals("CYKY-J 3x4", result.getContent().getFirst().getName());
        assertEquals("kjd98349jf9", result.getContent().getFirst().getAttributes().stream().toList().getFirst().getValue());
    }

    @DisplayName("findAll by specific category name")
    @Test
    void findAll5() {
        prepareProductsWithCategory();

        var specification = new ArrayList<Specification<Product>>(1);
        specification.add(ProductSpecifications.hasCategory("o"));
        var pageNumber = 0;
        var pageSize = 5;

        var result = repo.findAll(Specification.allOf(specification), PageRequest.of(pageNumber, pageSize, Sort.unsorted()));

        assertEquals(1, result.getTotalElements());
        assertEquals("WAGO 221", result.getContent().getFirst().getName());
        assertEquals("svorka", result.getContent().getFirst().getCategories().stream().toList().getFirst().getName());
    }

    @DisplayName("findAll - first & second page")
    @Test
    void findAll6() {
        prepareProducts();

        var specification = new ArrayList<Specification<Product>>(0);

        var result = repo.findAll(Specification.allOf(specification), PageRequest.of(0, 2, Sort.unsorted()));

        assertEquals(2, result.getContent().size());


        result = repo.findAll(Specification.allOf(specification), PageRequest.of(1, 2, Sort.unsorted()));

        assertEquals(0, result.getContent().size());
    }

    private void prepareProducts() {
        var product1 = new Product();
        product1.setName("product name");
        product1.setQuantity(432);
        product1.setMeasureUnit(MeasureUnit.UNIT);
        product1.setSellPrice(BigDecimal.valueOf(45.32));

        var product2 = new Product();
        product2.setName("name 2");
        product2.setQuantity(87);
        product2.setMeasureUnit(MeasureUnit.PACKAGE);
        product2.setSellPrice(BigDecimal.valueOf(12.95));

        repo.saveAll(List.of(product1, product2));
    }

    private void prepareProductsWithAttribute() {
        var attribute = new Attribute();
        attribute.setName("serial number");
        attribute.setType(Attribute.DataType.STRING);
        attribute.setMandatory(true);
        attribute.setValue("kjd98349jf9");

        var product1 = new Product();
        product1.setName("laminatova podlaha");
        product1.setQuantity(9);
        product1.setMeasureUnit(MeasureUnit.KG);
        product1.setSellPrice(BigDecimal.valueOf(90.42));

        var product2 = new Product();
        product2.setName("CYKY-J 3x4");
        product2.setQuantity(11);
        product2.setMeasureUnit(MeasureUnit.M);
        product2.setSellPrice(BigDecimal.valueOf(71.21));
        product2.getAttributes().add(attribute);

        repo.saveAll(List.of(product1, product2));
    }

    private void prepareProductsWithCategory() {
        var savedCat = catRepo.save(Category.of("svorka"));

        var product1 = new Product();
        product1.setName("WAGO 221");
        product1.setQuantity(4);
        product1.setMeasureUnit(MeasureUnit.PACKAGE);
        product1.setSellPrice(BigDecimal.valueOf(5.07));
        product1.getCategories().add(savedCat);

        var product2 = new Product();
        product2.setName("CYKY-J 3x2.5");
        product2.setQuantity(2_500);
        product2.setMeasureUnit(MeasureUnit.M);
        product2.setSellPrice(BigDecimal.valueOf(49.99));

        repo.saveAll(List.of(product1, product2));
    }
}