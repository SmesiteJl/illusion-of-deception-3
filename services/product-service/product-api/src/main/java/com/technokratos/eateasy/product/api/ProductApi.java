package com.technokratos.eateasy.product.api;

import com.technokratos.eateasy.product.dto.product.ProductRequest;
import com.technokratos.eateasy.product.dto.product.ProductResponse;
import com.technokratos.eateasy.product.dto.product.ProductUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Products", description = "Product management (food and grocery items)")
@RequestMapping("/api/v1/products")
public interface ProductApi {

    @Operation(summary = "Get a product by its ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ProductResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    ProductResponse getById(
            @Parameter(
                    description = "Product UUID",
                    example = "c7e2f6b4-98b8-4f98-89b2-8295e8d25b5a",
                    required = true)
            @PathVariable("id")
            UUID productId);

    @Operation(summary = "Create a new product", parameters = {
            @Parameter(
                    name = "product",
                    description = "JSON-string with product data",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductRequest.class),
                            examples = @ExampleObject(
                                    name = "Example",
                                    value = """
                    {
                      "title": "Gala Apple",
                      "description": "Fresh and juicy Gala apples",
                      "price": 1.99,
                      "categories": ["c7e2f6b4-98b8-4f98-89b2-8295e8d25b5a"],
                      "quantity": 200
                    }"""
                            )
                    )
            )
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    ProductResponse create(
            @RequestParam("product") @Valid String product,
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile
    );

    @Operation(summary = "Update the quantity of a product (add or subtract the provided value)")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Product quantity updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid quantity data"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            })
    @PatchMapping("/{id}/count")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    void updateQuantity(
            @Parameter(
                    description = "Product UUID",
                    example = "c7e2f6b4-98b8-4f98-89b2-8295e8d25b5a",
                    required = true)
            @PathVariable("id")
            UUID productId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Quantity change (positive to increase, negative to decrease)",
                    required = true,
                    content = @Content(schema = @Schema(type = "integer", example = "5")))
            @org.springframework.web.bind.annotation.RequestBody
            @Min(value = -10000)
            @Max(value = 10000)
            Integer quantity);

    @Operation(summary = "Update a product's details", parameters = {
            @Parameter(
                    name = "product",
                    description = "JSON-строка с обновляемыми данными",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductUpdateRequest.class),
                            examples = @ExampleObject(
                                    name = "Example",
                                    value = """
                    {
                      "title": "Updated Apple",
                      "description": "Premium quality apples",
                      "price": 2.49,
                      "categories": ["d8f3g7c5-12a9-4e76-91c3-8654e9d42f1b"],
                      "quantity": 150
                    }"""
                            )
                    )
            )
    })
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    void update(
            @PathVariable("id") UUID productId,
            @RequestParam("product") @Valid String product,
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile);

    @Operation(summary = "Delete a product by its ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    void delete(
            @Parameter(
                    description = "Product UUID",
                    example = "c7e2f6b4-98b8-4f98-89b2-8295e8d25b5a",
                    required = true)
            @PathVariable("id")
            UUID productId);
}