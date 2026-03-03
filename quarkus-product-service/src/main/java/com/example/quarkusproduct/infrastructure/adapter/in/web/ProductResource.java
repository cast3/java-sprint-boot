package com.example.quarkusproduct.infrastructure.adapter.in.web;

import com.example.quarkusproduct.domain.port.in.ProductUseCase;
import com.example.quarkusproduct.infrastructure.adapter.in.web.dto.ProductMapper;
import com.example.quarkusproduct.infrastructure.adapter.in.web.dto.ProductRequest;
import com.example.quarkusproduct.infrastructure.adapter.in.web.dto.ProductResponse;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

/**
 * Adaptador de entrada (JAX-RS): expone la API REST para el recurso Product.
 *
 * DIFERENCIA vs Spring Boot:
 *   - Spring Boot: @RestController + @RequestMapping + @GetMapping, @PostMapping, etc.
 *   - Quarkus:     @Path + @GET, @POST, @PUT, @DELETE (JAX-RS estándar de Jakarta EE)
 *
 *   - Spring Boot: @Autowired / @RequiredArgsConstructor
 *   - Quarkus:     @Inject (CDI — Jakarta EE)
 *
 *   - Spring Boot: ResponseEntity<T>
 *   - Quarkus:     Response (JAX-RS) o directamente el tipo de retorno
 */
@Path("/api/v1/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Products", description = "CRUD de productos")
public class ProductResource {

    @Inject
    ProductUseCase productUseCase;

    @Inject
    ProductMapper productMapper;

    @GET
    @Operation(summary = "Listar todos los productos")
    public List<ProductResponse> findAll() {
        return productUseCase.findAll().stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Obtener un producto por ID")
    public ProductResponse findById(@PathParam("id") Long id) {
        return productMapper.toResponse(productUseCase.findById(id));
    }

    @POST
    @Operation(summary = "Crear un nuevo producto")
    public Response create(@Valid ProductRequest request) {
        ProductResponse created = productMapper.toResponse(
                productUseCase.create(productMapper.toDomain(request))
        );
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualizar un producto existente")
    public ProductResponse update(@PathParam("id") Long id, @Valid ProductRequest request) {
        return productMapper.toResponse(
                productUseCase.update(id, productMapper.toDomain(request))
        );
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Eliminar un producto")
    public Response delete(@PathParam("id") Long id) {
        productUseCase.delete(id);
        return Response.noContent().build();
    }
}
