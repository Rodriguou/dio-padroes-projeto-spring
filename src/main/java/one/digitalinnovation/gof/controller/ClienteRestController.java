package one.digitalinnovation.gof.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

/**
 * Este {@link RestController} representa nossa <b>Facade</b>, pois abstrai toda a complexidade de integrações (Banco de Dados H2 e API ViaCEP) em uma interface simples e coesa (API REST).
 *
 * @author Rodrigo
 */
@RestController
@RequestMapping("clientes")
public class ClienteRestController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "204", description = "No Content")
    })
    public ResponseEntity<Iterable<Cliente>> buscarTodos() {
        Iterable<Cliente> clientes = clienteService.buscarTodos();
        if (!clientes.iterator().hasNext()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        try {
            Cliente cliente = clienteService.buscarPorId(id);
            return ResponseEntity.ok(cliente);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client successfully inserted"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Cliente> inserir(@Valid @RequestBody Cliente cliente) {
        try {
            clienteService.inserir(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        try {
            Cliente clienteBd = clienteService.buscarPorId(id);
            clienteService.atualizar(id, cliente);
            return ResponseEntity.ok(cliente);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            clienteService.deletar(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
